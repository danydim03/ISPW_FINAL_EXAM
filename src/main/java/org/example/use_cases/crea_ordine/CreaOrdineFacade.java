// src/main/java/org/example/use_cases/crea_ordine/CreaOrdineFacade.java
package org.example.use_cases.crea_ordine;

import org.example.exceptions.*;
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

    // ═══════════════════════════════════════════════════════════════════════
    // NESSUN CAMPO DI STATO - FACADE STATELESS
    // ═══════════════════════════════════════════════════════════════════════

    private final CreaOrdineController controller;
    private final org.example.model.user.User sessionUser;

    public CreaOrdineFacade(String tokenKey) throws MissingAuthorizationException {
        this.sessionUser = SessionManager.getInstance().getSessionUserByTokenKey(tokenKey);
        if (sessionUser == null || sessionUser.getRole() == null || sessionUser.getRole().getClienteRole() == null) {
            throw new MissingAuthorizationException("Accesso negato: token non autorizzato per ruolo cliente");
        }
        this.controller = new CreaOrdineController();
    }

    /**
     * Inizializza un nuovo ordine per il cliente.
     * 
     * @return OrdineBean con numeroOrdine che sarà usato come ID
     */
    public OrdineBean inizializzaNuovoOrdine() throws DAOException {
        return controller.creaNuovoOrdine(sessionUser.getId());
    }

    public List<FoodBean> getProdottiBaseDisponibili() throws DAOException, ObjectNotFoundException,
            MissingAuthorizationException, WrongListQueryIdentifierValue, UserNotFoundException,
            UnrecognizedRoleException {
        return controller.getProdottiBaseDisponibili();
    }

    public List<FoodBean> getAddOnDisponibili() throws DAOException, ObjectNotFoundException,
            MissingAuthorizationException, WrongListQueryIdentifierValue, UserNotFoundException,
            UnrecognizedRoleException {
        return controller.getAddOnDisponibili();
    }

    /**
     * Aggiunge un prodotto all'ordine.
     * 
     * @param ordineId ID dell'ordine (passato dal GUI Controller)
     * @param foodBean il prodotto da aggiungere
     */
    public boolean aggiungiProdottoAOrdine(String ordineId, FoodBean foodBean) {
        return controller.aggiungiProdottoAOrdine(ordineId, foodBean);
    }

    /**
     * Rimuove un prodotto dall'ordine.
     * 
     * @param ordineId ID dell'ordine
     * @param index    indice del prodotto da rimuovere
     */
    public boolean rimuoviProdottoDaOrdine(String ordineId, int index) {
        return controller.rimuoviProdottoDaOrdine(ordineId, index);
    }

    /**
     * Applica un voucher all'ordine.
     * 
     * @param ordineId      ID dell'ordine
     * @param codiceVoucher codice del voucher
     */
    public VoucherBean applicaVoucher(String ordineId, String codiceVoucher)
            throws DAOException, ObjectNotFoundException, MissingAuthorizationException, WrongListQueryIdentifierValue,
            UserNotFoundException, UnrecognizedRoleException {
        return controller.applicaVoucher(ordineId, codiceVoucher);
    }

    /**
     * Rimuove il voucher dall'ordine.
     * 
     * @param ordineId ID dell'ordine
     */
    public void rimuoviVoucher(String ordineId) {
        controller.rimuoviVoucher(ordineId);
    }

    /**
     * Ottiene il riepilogo dell'ordine.
     * 
     * @param ordineId ID dell'ordine
     */
    public RiepilogoOrdineBean getRiepilogoOrdine(String ordineId) {
        return controller.getRiepilogoOrdine(ordineId);
    }

    /**
     * Conferma l'ordine.
     * 
     * @param ordineId ID dell'ordine
     */
    public boolean confermaOrdine(String ordineId) throws DAOException, MissingAuthorizationException {
        return controller.confermaOrdine(ordineId);
    }

    /**
     * Annulla l'ordine.
     * 
     * @param ordineId ID dell'ordine
     */
    public void annullaOrdine(String ordineId) {
        controller.annullaOrdine(ordineId);
    }
}
