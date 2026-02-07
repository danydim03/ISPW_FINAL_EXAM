// src/main/java/org/example/use_cases/crea_ordine/CreaOrdineFacade.java
package org.example.use_cases.crea_ordine;

import org.example.exceptions.*;
import org.example.model.user.User;
import org.example.session_manager.SessionManager;
import org.example.use_cases.crea_ordine.beans.*;

import java.util.List;

/**
 * Facade per lo Use Case "Crea Ordine".
 * 
 * <p>
 * <b>STATELESS</b>: non mantiene stato di business.
 * Riceve ordineId come parametro e lo passa al Controller.
 * </p>
 * 
 * Responsabilità:
 * - Esporre un'interfaccia semplificata al layer Boundary (GUI)
 * - Verificare autorizzazioni tramite SessionManager
 * - Delegare la logica al Controller
 */
public class CreaOrdineFacade {

    /**
     * Metodo helper per ottenere un'istanza di CreaOrdineController.
     * Creato on-demand per mantenere il facade stateless.
     * 
     * @return nuova istanza di CreaOrdineController
     */
    private CreaOrdineController getController() {
        return new CreaOrdineController();
    }

    /**
     * Recupera e valida l'utente dalla sessione.
     * 
     * @param tokenKey chiave del token di sessione
     * @return l'utente autenticato
     * @throws MissingAuthorizationException se l'utente non è autorizzato
     */
    private User getSessionUser(String tokenKey) throws MissingAuthorizationException {
        User user = SessionManager.getInstance().getSessionUserByTokenKey(tokenKey);
        if (user == null || user.getRole() == null || user.getRole().getClienteRole() == null) {
            throw new MissingAuthorizationException("Accesso negato: token non autorizzato per ruolo cliente");
        }
        return user;
    }

    /**
     * Inizializza un nuovo ordine per il cliente.
     * 
     * @param tokenKey chiave del token di sessione
     * @return OrdineBean con numeroOrdine che sarà usato come ID
     */
    public OrdineBean inizializzaNuovoOrdine(String tokenKey) throws DAOException, MissingAuthorizationException {
        User sessionUser = getSessionUser(tokenKey);
        return getController().creaNuovoOrdine(sessionUser.getId());
    }

    public List<FoodBean> getProdottiBaseDisponibili(String tokenKey) throws DAOException, ObjectNotFoundException,
            MissingAuthorizationException, WrongListQueryIdentifierValue, UserNotFoundException,
            UnrecognizedRoleException {
        getSessionUser(tokenKey); // Verifica autorizzazione
        return getController().getProdottiBaseDisponibili();
    }

    public List<FoodBean> getAddOnDisponibili(String tokenKey) throws DAOException, ObjectNotFoundException,
            MissingAuthorizationException, WrongListQueryIdentifierValue, UserNotFoundException,
            UnrecognizedRoleException {
        getSessionUser(tokenKey); // Verifica autorizzazione
        return getController().getAddOnDisponibili();
    }

    /**
     * Aggiunge un prodotto all'ordine.
     * 
     * @param tokenKey chiave del token di sessione
     * @param ordineId ID dell'ordine (passato dal GUI Controller)
     * @param foodBean il prodotto da aggiungere
     */
    public boolean aggiungiProdottoAOrdine(String tokenKey, String ordineId, FoodBean foodBean)
            throws MissingAuthorizationException {
        getSessionUser(tokenKey); // Verifica autorizzazione
        return getController().aggiungiProdottoAOrdine(ordineId, foodBean);
    }

    /**
     * Rimuove un prodotto dall'ordine.
     * 
     * @param tokenKey chiave del token di sessione
     * @param ordineId ID dell'ordine
     * @param index    indice del prodotto da rimuovere
     */
    public boolean rimuoviProdottoDaOrdine(String tokenKey, String ordineId, int index)
            throws MissingAuthorizationException {
        getSessionUser(tokenKey); // Verifica autorizzazione
        return getController().rimuoviProdottoDaOrdine(ordineId, index);
    }

    /**
     * Applica un voucher all'ordine.
     * 
     * @param tokenKey      chiave del token di sessione
     * @param ordineId      ID dell'ordine
     * @param codiceVoucher codice del voucher
     */
    public VoucherBean applicaVoucher(String tokenKey, String ordineId, String codiceVoucher)
            throws DAOException, ObjectNotFoundException, MissingAuthorizationException, WrongListQueryIdentifierValue,
            UserNotFoundException, UnrecognizedRoleException {
        getSessionUser(tokenKey); // Verifica autorizzazione
        return getController().applicaVoucher(ordineId, codiceVoucher);
    }

    /**
     * Rimuove il voucher dall'ordine.
     * 
     * @param tokenKey chiave del token di sessione
     * @param ordineId ID dell'ordine
     */
    public void rimuoviVoucher(String tokenKey, String ordineId) throws MissingAuthorizationException {
        getSessionUser(tokenKey); // Verifica autorizzazione
        getController().rimuoviVoucher(ordineId);
    }

    /**
     * Ottiene il riepilogo dell'ordine.
     * 
     * @param tokenKey chiave del token di sessione
     * @param ordineId ID dell'ordine
     */
    public RiepilogoOrdineBean getRiepilogoOrdine(String tokenKey, String ordineId)
            throws MissingAuthorizationException {
        getSessionUser(tokenKey); // Verifica autorizzazione
        return getController().getRiepilogoOrdine(ordineId);
    }

    /**
     * Conferma l'ordine.
     * 
     * @param tokenKey chiave del token di sessione
     * @param ordineId ID dell'ordine
     */
    public boolean confermaOrdine(String tokenKey, String ordineId) throws DAOException, MissingAuthorizationException {
        getSessionUser(tokenKey); // Verifica autorizzazione
        return getController().confermaOrdine(ordineId);
    }

    /**
     * Annulla l'ordine.
     * 
     * @param tokenKey chiave del token di sessione
     * @param ordineId ID dell'ordine
     */
    public void annullaOrdine(String tokenKey, String ordineId) throws MissingAuthorizationException {
        getSessionUser(tokenKey); // Verifica autorizzazione
        getController().annullaOrdine(ordineId);
    }
}
