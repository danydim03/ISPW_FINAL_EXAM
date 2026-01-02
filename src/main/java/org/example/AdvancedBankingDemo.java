package org.example;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MASTERCLASS: ARCHITETTURA DI GESTIONE ECCEZIONI COMPLESSA
 * * SCENARIO: Processo di Trasferimento Fondi.
 * FLUSSO: View -> Controller (Service) -> DAO -> Database/Network
 */
public class AdvancedBankingDemo {

    // Logger centralizzato (Best Practice: non usare System.out.println per i log di errore)
    private static final Logger logger = Logger.getLogger("BankingSystem");

    // ==================================================================================
    // 1. GERARCHIA DELLE ECCEZIONI (CUSTOM EXCEPTIONS)
    // ==================================================================================

    // ROOT EXCEPTION (CHECKED)
    // Forza tutti i layer a gestire o dichiarare i fallimenti del sistema bancario.
    abstract static class BankingException extends Exception {
        private final String errorCode; // Esempio di arricchimento dati strutturato

        public BankingException(String message, String errorCode) {
            super(message);
            this.errorCode = errorCode;
        }

        public BankingException(String message, Throwable cause, String errorCode) {
            super(message, cause); // CHAINING: Passo la causa alla superclasse
            this.errorCode = errorCode;
        }

        public String getErrorCode() { return errorCode; }
    }

    // LAYER EXCEPTION: PERSISTENZA (DAO)
    static class PersistenceException extends BankingException {
        public PersistenceException(String message, Throwable cause) {
            super(message, cause, "DB_ERR_001");
        }
    }

    // LAYER EXCEPTION: BUSINESS LOGIC / SERVICE
    static class TransactionException extends BankingException {
        public TransactionException(String message, Throwable cause) {
            super(message, cause, "TRX_ERR_GEN");
        }
        public TransactionException(String message, String errorCode) {
            super(message, errorCode);
        }
    }

    // LEAF EXCEPTION: Business Rule Specifica (Nessuna causa tecnica sotto)
    static class InsufficientFundsException extends TransactionException {
        public InsufficientFundsException(double currentBalance, double required) {
            super("Saldo insufficiente. Attuale: " + currentBalance + ", Richiesto: " + required, "TRX_ERR_FUNDS");
        }
    }

    // UNCHECKED EXCEPTION (Runtime)
    // Per errori di configurazione irrecuperabili (es. DB URL mancante).
    // Non costringe al try-catch perché è un bug del programmatore/sistemista.
    static class ConfigurationException extends RuntimeException {
        public ConfigurationException(String message) {
            super(message);
        }
    }

    // ==================================================================================
    // 2. LIVELLO INFRASTRUTTURA / DAO (Data Access Object)
    // ==================================================================================

    static class AccountDAO {

        // Simula la lettura della configurazione
        private String dbUrl = "jdbc:mysql://localhost:3306/bank";

        public void withdraw(String accountId, double amount) throws PersistenceException {
            if (dbUrl == null) {
                throw new ConfigurationException("URL del Database non configurato! Impossibile avviare il DAO.");
            }

            try {
                // Simuliamo logica DB complessa
                performLowLevelDatabaseOp(accountId);

            } catch (SQLException | IOException e) {
                // *** TECNICA AVANZATA: EXCEPTION TRANSLATION ***
                // Il DAO cattura eccezioni eterogenee (SQL, IO) che sono dettagli implementativi.
                // Le converte in un'unica eccezione semantica del Layer (PersistenceException).
                // IL CHAINING È VITALE QUI.

                throw new PersistenceException("Fallimento accesso dati per account: " + accountId, e);
            }
        }

        private void performLowLevelDatabaseOp(String accountId) throws SQLException, IOException {
            // Simuliamo diversi scenari di fallimento basati sull'ID
            if ("USER_TIMEOUT".equals(accountId)) {
                throw new SQLException("ORA-12170: TNS:Connect timeout occurred", "08000", 12170);
            } else if ("USER_IO_ERROR".equals(accountId)) {
                throw new IOException("Disk quota exceeded on /var/lib/mysql");
            }
            System.out.println("[DAO] Prelievo registrato su DB per " + accountId);
        }
    }

    // ==================================================================================
    // 3. LIVELLO CONTROLLER / SERVICE (Orchestratore)
    // ==================================================================================

    static class TransferController {

        private final AccountDAO accountDAO = new AccountDAO();

        public void executeTransfer(String userId, double amount) throws TransactionException, BankingException {
            String transactionId = UUID.randomUUID().toString();
            logger.info("Inizio transazione " + transactionId + " per utente " + userId);

            try {
                // 1. Validazione Business Logic (Fail Fast)
                if (amount > 10000) {
                    // Eccezione di Business Pura (Root Cause è la logica, non un errore tecnico)
                    throw new InsufficientFundsException(500.00, amount);
                }

                // 2. Chiamata al Layer Inferiore
                accountDAO.withdraw(userId, amount);

            } catch (PersistenceException e) {
                // *** TECNICA: CONTEXT ENRICHMENT (Arricchimento) ***
                // Catturo l'errore del DAO. Il Controller SA cosa stava facendo (un Bonifico).
                // Il DAO non sapeva del bonifico, sapeva solo "prelievo".
                // Avvolgo l'eccezione aggiungendo il TransactionID per facilitare il debug.

                String contextMsg = String.format("Impossibile completare la transazione %s. Step: Prelievo Fondi.", transactionId);

                // CHAINING COMPLETO: TransactionException -> PersistenceException -> SQLException
                throw new TransactionException(contextMsg, e);
            }
        }
    }

    // ==================================================================================
    // 4. LIVELLO BOUNDARY (UI) - Gestione Finale
    // ==================================================================================

    static class BankingAppView {

        private final TransferController controller = new TransferController();

        public void onPayCheckClick(String userId, double amount) {
            System.out.println("\n--- Tentativo di pagamento per: " + userId + " ---");

            try {
                controller.executeTransfer(userId, amount);
                System.out.println("[UI] Successo! Trasferimento completato.");

            } catch (InsufficientFundsException e) {
                // GESTIONE 1: Errore Funzionale (User Error)
                // Messaggio gentile, niente stack trace. L'utente può risolvere il problema (ricaricando).
                System.err.println("[UI-POPUP] ERRORE: " + e.getMessage());
                System.err.println("[UI-HINT] Controlla il tuo saldo.");

            } catch (TransactionException e) {
                // GESTIONE 2: Errore di Sistema Critico (System Error)
                // L'utente non può farci nulla. Dobbiamo loggare tutto per gli sviluppatori.

                System.err.println("[UI-POPUP] Servizio momentaneamente non disponibile. Codice Errore: " + e.getErrorCode());

                // LOGGING DELLO STACK TRACE (Fondamentale per noi dev)
                logger.log(Level.SEVERE, "CRITICAL FAILURE IN TRANSACTION", e);

                // ESEMPIO DI ANALISI DELLA CAUSA (Debugging automatico)
                Throwable cause = e.getCause();
                if (cause instanceof PersistenceException) {
                    Throwable rootCause = cause.getCause(); // Scendo al livello 3 (SQL/IO)
                    System.out.println("[DEV-DEBUG] Causa radice tecnica: " + rootCause.getClass().getSimpleName() + " -> " + rootCause.getMessage());
                }

            } catch (ConfigurationException e) {
                // GESTIONE 3: Unchecked (Bug Fatale)
                System.err.println("[UI-FATAL] L'applicazione è configurata male. Contattare l'assistenza.");
                logger.log(Level.SEVERE, "Configurazione mancante", e);
            } catch (Exception e) {
                // GESTIONE 4: Catch-All di sicurezza
                System.err.println("[UI-ERROR] Errore imprevisto.");
            }
        }
    }

    // ==================================================================================
    // MAIN DI TEST
    // ==================================================================================

    public static void main(String[] args) {
        BankingAppView view = new BankingAppView();

        // CASO 1: Errore Business (Saldo)
        view.onPayCheckClick("MARIO_ROSSI", 50000.0);

        // CASO 2: Errore Tecnico DB (Simuliamo Timeout SQL) -> Genera Chaining
        view.onPayCheckClick("USER_TIMEOUT", 100.0);

        // CASO 3: Errore Tecnico IO (Simuliamo Disco Pieno) -> Genera Chaining
        view.onPayCheckClick("USER_IO_ERROR", 200.0);

        // CASO 4: Successo
        view.onPayCheckClick("USER_OK", 50.0);
    }
}