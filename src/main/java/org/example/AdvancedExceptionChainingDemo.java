package org.example;


import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MASTERCLASS: ESEMPIO COMPLESSO DI EXCEPTION WRAPPING E CHAINING
 * * SCENARIO:
 * Un utente tenta di confermare un ordine. Il sistema deve:
 * 1. Validare i dati (Business Logic)
 * 2. Salvare l'ordine nel DB (Persistence)
 * * Questo file simula l'intera architettura a livelli (BCE) per mostrare
 * come le eccezioni viaggiano e si trasformano.
 */

public class AdvancedExceptionChainingDemo {

    // ==================================================================================
    // 1. DEFINIZIONE DELLE ECCEZIONI (GERARCHIA)
    // ==================================================================================

    /**
     * Root Exception per l'applicazione.
     * Estende Exception (Checked) per forzare i layer superiori a gestirla.
     */
    static abstract class HabibiException extends Exception {
        // Costruttore base
        public HabibiException(String message) {
            super(message);
        }
        // COSTRUTTORE FONDAMENTALE PER IL CHAINING
        // Accetta il messaggio E la causa originale (l'eccezione di livello inferiore)
        public HabibiException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    // Eccezione del livello di Persistenza (DAO)
    static class DAOException extends HabibiException {
        public DAOException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    // Eccezione del livello di Business/Controllo
    static class OrderProcessingException extends HabibiException {
        public OrderProcessingException(String message, Throwable cause) {
            super(message, cause);
        }

        // Overload per errori di business puri senza cause esterne
        public OrderProcessingException(String message) {
            super(message);
        }
    }

    // Eccezione specifica di validazione (Leaf)
    static class InvalidVoucherException extends OrderProcessingException {
        public InvalidVoucherException(String voucherCode) {
            super("Il voucher '" + voucherCode + "' è scaduto o non valido.");
        }
    }

    // ==================================================================================
    // 2. LIVELLO PERSISTENZA (DAO) - DOVE NASCE IL PROBLEMA TECNICO
    // ==================================================================================

    static class OrderDAO {

        // Simula il salvataggio su DB reale
        public void saveOrder(int orderId) throws DAOException {
            try {
                // Simuliamo una operazione JDBC
                simulateDatabaseOperation(orderId);

            } catch (SQLException e) {
                // *** TECNICA DI WRAPPING / CHAINING ***
                // 1. Catturo l'eccezione SQL (basso livello, implementation detail).
                // 2. La avvolgo in una DAOException (alto livello, semanticamente corretta).
                // 3. Passo 'e' come secondo parametro: questo preserva lo STACK TRACE originale!

                throw new DAOException("Errore critico durante il salvataggio dell'ordine ID: " + orderId, e);

                // NOTA: Se facessi 'throw new DAOException("Messaggio");' perderei la causa (SQLException).
                // Sarebbe un CODE SMELL gravissimo.
            }
        }

        private void simulateDatabaseOperation(int orderId) throws SQLException {
            // Simuliamo un timeout del database o una violazione di chiave esterna
            System.out.println("[DAO] Tentativo di connessione al DB...");
            boolean dbIsDown = true; // Flag di simulazione

            if (dbIsDown) {
                // Lancia un'eccezione nativa di Java SQL
                throw new SQLException("ORA-01013: user requested cancel of current operation (Timeout)", "HY000", 1013);
            }
        }
    }

    // ==================================================================================
    // 3. LIVELLO CONTROLLO (CONTROLLER) - LOGICA E ARRICCHIMENTO
    // ==================================================================================

    static class PlaceOrderController {

        private final OrderDAO orderDAO = new OrderDAO();

        public void processOrder(int orderId, String voucherCode) throws OrderProcessingException, HabibiException {
            System.out.println("[CONTROLLER] Inizio elaborazione ordine...");

            // 1. Validazione Business Logic
            if ("SCADUTO".equals(voucherCode)) {
                // Qui lancio un'eccezione di dominio specifica. Non c'è chaining perché è l'origine dell'errore.
                throw new InvalidVoucherException(voucherCode);
            }

            // 2. Chiamata al DAO
            try {
                orderDAO.saveOrder(orderId);

            } catch (DAOException e) {
                // *** CONTEXT ENRICHMENT ***
                // Il Controller riceve un errore generico di salvataggio.
                // Decide che per il caso d'uso "ProcessOrder", questo è un errore di processo.
                // Rilancia aggiungendo contesto (es. l'utente che stava facendo l'operazione).

                String contextInfo = "Fallimento operazione per User: MarioRossi al " + LocalDateTime.now();

                // Chaining: OrderProcessingException -> DAOException -> SQLException
                throw new OrderProcessingException("Impossibile completare l'acquisto. " + contextInfo, e);
            }
        }
    }

    // ==================================================================================
    // 4. LIVELLO BOUNDARY (UI/CLI) - IL PUNTO DI GESTIONE
    // ==================================================================================

    static class OrderView {

        private final PlaceOrderController controller = new PlaceOrderController();
        private static final Logger logger = Logger.getLogger(OrderView.class.getName());

        public void onConfirmButtonDocs(int orderId, String voucherCode) {
            try {
                controller.processOrder(orderId, voucherCode);
                System.out.println("[VIEW] Ordine completato con successo!");

            } catch (InvalidVoucherException e) {
                // GESTIONE SPECIFICA: Errore utente
                // Non serve loggare lo stack trace, è un errore di input.
                System.err.println("[VIEW-USER ALERT] Attenzione: " + e.getMessage());
                System.out.println("[VIEW-LOGIC] Mostra popup: 'Inserisci un voucher valido'");

            } catch (OrderProcessingException e) {
                // GESTIONE GENERICA DI PROCESSO: Errore di sistema mascherato

                // 1. Loggare tutto per gli sviluppatori (Stack Trace completo)
                // Usiamo il logger per salvare la catena delle cause
                logger.log(Level.SEVERE, "Stack Trace Completo per Debugging:", e);

                // 2. Analisi della Root Cause (Opzionale, per logica avanzata)
                Throwable rootCause = e.getCause();
                while (rootCause.getCause() != null) {
                    rootCause = rootCause.getCause();
                }
                System.out.println("[DEBUG INTERNO] La causa radice era: " + rootCause.getClass().getSimpleName());

                // 3. Feedback all'utente (Messaggio pulito, niente jargon tecnico)
                System.err.println("[VIEW-USER ALERT] Ci scusiamo, il sistema è momentaneamente indisponibile.");
                System.err.println("[VIEW-USER ALERT] Codice Errore Rif: " + orderId); // Dare un ID all'utente aiuta il supporto

            } catch (HabibiException e) {
                // Catch-all per altre eccezioni della nostra app
                System.err.println("[VIEW-USER ALERT] Errore generico dell'applicazione.");
            } catch (Exception e) {
                // Catch-all finale per RuntimeException impreviste (NPE, etc.)
                System.err.println("[VIEW-USER ALERT] Fatal Error.");
            }
        }
    }

    // ==================================================================================
    // MAIN - SIMULAZIONE RUNTIME
    // ==================================================================================

    public static void main(String[] args) {
        OrderView view = new OrderView();

        System.out.println("--- TEST 1: Voucher Invalido (Business Logic Error) ---");
        view.onConfirmButtonDocs(101, "SCADUTO");

        System.out.println("\n--- TEST 2: Database Down (Chained System Error) ---");
        view.onConfirmButtonDocs(102, "VALIDO");
    }
}