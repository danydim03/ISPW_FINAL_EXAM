package org.example.Facades;

import org.example.exceptions.*;
import org.example.session_manager.SessionManager;
import org. example.use_cases.crea_ordine.beans.*;
import org.example.use_cases.crea_ordine.CreaOrdineController;

import java.util.List;

/**
 * Facade per lo Use Case "Crea Ordine".
 * 
 * Questo Facade fornisce un'interfaccia semplificata per i Controller Grafici (GUI/CLI),
 * nascondendo la complessità delle interazioni tra:
 * - CreaOrdineController (Controller Applicativo)
 * - SessionManager
 * - Validazioni
 * - Gestione errori
 * 
 * VANTAGGI:
 * 1. Disaccoppia la Boundary (GUI) dal Control (logica)
 * 2. Fornisce un punto di accesso unico per lo use case
 * 3.  Semplifica l'interfaccia per i controller grafici
 * 4.  Centralizza la gestione degli errori
 * 5. Permette di supportare facilmente GUI e CLI con la stessa logica
 * 
 * Pattern utilizzati:
 * - Facade (questo)
 * - Singleton (istanza unica per sessione)
 */
public class CreaOrdineFacade {
    
    private static CreaOrdineFacade instance;
    
    private final CreaOrdineController controller;
    private boolean ordineInCorso;
    
    /**
     * Costruttore privato - Singleton
     */
    private CreaOrdineFacade() {
        this.controller = new CreaOrdineController();
        this.ordineInCorso = false;
    }
    
    /**
     * Restituisce l'istanza singleton del Facade
     */
    public static synchronized CreaOrdineFacade getInstance() {
        if (instance == null) {
            instance = new CreaOrdineFacade();
        }
        return instance;
    }
    
    /**
     * Resetta l'istanza (utile per testing o logout)
     */
    public static synchronized void resetInstance() {
        instance = null;
    }
    
    // ==================== OPERAZIONI PRINCIPALI ====================
    
    /**
     * Inizia un nuovo ordine per il cliente corrente. 
     * Recupera automaticamente l'ID del cliente dalla sessione.
     * 
     * @return OrdineBean con i dati dell'ordine inizializzato
     * @throws CreaOrdineException se si verifica un errore
     */
    public OrdineBean iniziaNuovoOrdine() throws CreaOrdineException {
        try {
            // Recupera il cliente dalla sessione
            String clienteId = getClienteIdDaSessione();
            
            if (clienteId == null || clienteId.isEmpty()) {
                throw new CreaOrdineException("Utente non autenticato o non è un cliente");
            }
            
            // Inizializza l'ordine tramite il controller
            OrdineBean ordine = controller. inizializzaNuovoOrdine(clienteId);
            ordineInCorso = true;
            
            return ordine;
            
        } catch (DAOException | MissingAuthorizationException e) {
            throw new CreaOrdineException("Errore durante l'inizializzazione dell'ordine: " + e. getMessage(), e);
        }
    }
    
    /**
     * Recupera la lista dei prodotti base disponibili per l'ordine.
     * 
     * @return Lista di FoodBean dei prodotti base
     * @throws CreaOrdineException se si verifica un errore
     */
    public List<FoodBean> getMenuProdottiBase() throws CreaOrdineException {
        try {
            return controller.getProdottiBaseDisponibili();
        } catch (DAOException | ObjectNotFoundException | MissingAuthorizationException | 
                 WrongListQueryIdentifierValue | UserNotFoundException | UnrecognizedRoleException e) {
            throw new CreaOrdineException("Errore nel recupero dei prodotti base: " + e.getMessage(), e);
        }
    }
    
    /**
     * Recupera la lista degli add-on disponibili. 
     * 
     * @return Lista di FoodBean degli add-on
     * @throws CreaOrdineException se si verifica un errore
     */
    public List<FoodBean> getMenuAddOn() throws CreaOrdineException {
        try {
            return controller.getAddOnDisponibili();
        } catch (DAOException | ObjectNotFoundException | MissingAuthorizationException | 
                 WrongListQueryIdentifierValue | UserNotFoundException | UnrecognizedRoleException e) {
            throw new CreaOrdineException("Errore nel recupero degli add-on: " + e. getMessage(), e);
        }
    }
    
    /**
     * Aggiunge un prodotto all'ordine corrente. 
     * Il FoodBean deve contenere il prodotto base e gli add-on selezionati.
     * 
     * @param foodBean il prodotto da aggiungere
     * @return RiepilogoOrdineBean aggiornato
     * @throws CreaOrdineException se si verifica un errore
     */
    public RiepilogoOrdineBean aggiungiProdotto(FoodBean foodBean) throws CreaOrdineException {
        verificaOrdineInCorso();
        
        try {
            boolean success = controller.aggiungiProdottoAOrdine(foodBean);
            
            if (! success) {
                throw new CreaOrdineException("Impossibile aggiungere il prodotto all'ordine");
            }
            
            return controller.getRiepilogoOrdine();
            
        } catch (DAOException e) {
            throw new CreaOrdineException("Errore durante l'aggiunta del prodotto: " + e.getMessage(), e);
        }
    }
    
    /**
     * Rimuove un prodotto dall'ordine corrente.
     * 
     * @param indiceProdotto l'indice del prodotto da rimuovere
     * @return RiepilogoOrdineBean aggiornato
     * @throws CreaOrdineException se si verifica un errore
     */
    public RiepilogoOrdineBean rimuoviProdotto(int indiceProdotto) throws CreaOrdineException {
        verificaOrdineInCorso();
        
        boolean success = controller.rimuoviProdottoDaOrdine(indiceProdotto);
        
        if (!success) {
            throw new CreaOrdineException("Impossibile rimuovere il prodotto dall'ordine");
        }
        
        return controller.getRiepilogoOrdine();
    }
    
    /**
     * Applica un voucher all'ordine corrente.
     * 
     * @param codiceVoucher il codice del voucher da applicare
     * @return RiepilogoOrdineBean aggiornato con lo sconto applicato
     * @throws CreaOrdineException se il voucher non è valido o si verifica un errore
     */
    public RiepilogoOrdineBean applicaVoucher(String codiceVoucher) throws CreaOrdineException {
        verificaOrdineInCorso();
        
        if (codiceVoucher == null || codiceVoucher.trim(). isEmpty()) {
            throw new CreaOrdineException("Codice voucher non valido");
        }
        
        try {
            VoucherBean voucher = controller. applicaVoucher(codiceVoucher);
            
            if (voucher == null) {
                throw new CreaOrdineException("Voucher non trovato o non valido: " + codiceVoucher);
            }
            
            return controller.getRiepilogoOrdine();
            
        } catch (DAOException | ObjectNotFoundException | MissingAuthorizationException | 
                 WrongListQueryIdentifierValue | UserNotFoundException | UnrecognizedRoleException e) {
            throw new CreaOrdineException("Errore durante l'applicazione del voucher: " + e.getMessage(), e);
        }
    }
    
    /**
     * Rimuove il voucher dall'ordine corrente. 
     * 
     * @return RiepilogoOrdineBean aggiornato
     * @throws CreaOrdineException se si verifica un errore
     */
    public RiepilogoOrdineBean rimuoviVoucher() throws CreaOrdineException {
        verificaOrdineInCorso();
        
        controller.rimuoviVoucher();
        return controller.getRiepilogoOrdine();
    }
    
    /**
     * Recupera il riepilogo dell'ordine corrente.
     * 
     * @return RiepilogoOrdineBean con tutti i dati dell'ordine
     * @throws CreaOrdineException se non c'è un ordine in corso
     */
    public RiepilogoOrdineBean getRiepilogo() throws CreaOrdineException {
        verificaOrdineInCorso();
        
        RiepilogoOrdineBean riepilogo = controller.getRiepilogoOrdine();
        
        if (riepilogo == null) {
            throw new CreaOrdineException("Errore nel recupero del riepilogo ordine");
        }
        
        return riepilogo;
    }
    
    /**
     * Conferma l'ordine e procede al pagamento.
     * 
     * @return true se l'ordine è stato confermato con successo
     * @throws CreaOrdineException se si verifica un errore
     */
    public boolean confermaOrdine() throws CreaOrdineException {
        verificaOrdineInCorso();
        
        try {
            boolean success = controller.confermaOrdine();
            
            if (success) {
                ordineInCorso = false;
            }
            
            return success;
            
        } catch (DAOException | MissingAuthorizationException e) {
            throw new CreaOrdineException("Errore durante la conferma dell'ordine: " + e.getMessage(), e);
        }
    }
    
    /**
     * Annulla l'ordine corrente.
     */
    public void annullaOrdine() {
        controller.annullaOrdine();
        ordineInCorso = false;
    }
    
    /**
     * Verifica se c'è un ordine in corso. 
     * 
     * @return true se c'è un ordine in corso
     */
    public boolean isOrdineInCorso() {
        return ordineInCorso;
    }
    
    // ==================== METODI PRIVATI ====================
    
    /**
     * Verifica che ci sia un ordine in corso, altrimenti lancia eccezione
     */
    private void verificaOrdineInCorso() throws CreaOrdineException {
        if (!ordineInCorso) {
            throw new CreaOrdineException("Nessun ordine in corso.  Inizia un nuovo ordine prima.");
        }
    }
    
    /**
     * Recupera l'ID del cliente dalla sessione corrente
     */
    private String getClienteIdDaSessione() {
        try {
            // Assumendo che SessionManager abbia un metodo per recuperare l'utente corrente
            if (SessionManager.getInstance() != null && 
                SessionManager.getInstance().getSessionUser() != null) {
                return SessionManager.getInstance().getSessionUser().getID();
            }
        } catch (Exception e) {
            // Log dell'errore se necessario
        }
        return null;
    }
    
    // ==================== ECCEZIONE CUSTOM ====================
    
    /**
     * Eccezione specifica per lo use case Crea Ordine
     */
    public static class CreaOrdineException extends Exception {
        
        private static final long serialVersionUID = 1L;
        
        public CreaOrdineException(String message) {
            super(message);
        }
        
        public CreaOrdineException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}