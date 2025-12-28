// src/main/java/org/example/use_cases/crea_ordine/CreaOrdineFacade.java
package org.example.use_cases.crea_ordine;

import org.example.exceptions.*;
import org.example.session_manager.SessionManager;
import org.example.use_cases.crea_ordine.beans.*;

import java.util.List;

public class CreaOrdineFacade {

    private final CreaOrdineController controller;
    private final org.example.model.user.User sessionUser;

    public CreaOrdineFacade(String tokenKey) throws MissingAuthorizationException {
        this.sessionUser = SessionManager.getInstance().getSessionUserByTokenKey(tokenKey);
        if (sessionUser == null || sessionUser.getRole() == null || sessionUser.getRole().getClienteRole() == null) {
            throw new MissingAuthorizationException("Accesso negato: token non autorizzato per ruolo cliente");
        }
        this.controller = new CreaOrdineController();
    }

    public OrdineBean inizializzaNuovoOrdine(String ignoredClienteId)
            throws DAOException, MissingAuthorizationException {
        // Use actual client ID from session user (database ID), not the passed
        // parameter
        String actualClienteId = sessionUser.getId();
        return controller.inizializzaNuovoOrdine(actualClienteId);
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

    public boolean aggiungiProdottoAOrdine(FoodBean foodBean) throws DAOException {
        return controller.aggiungiProdottoAOrdine(foodBean);
    }

    public boolean rimuoviProdottoDaOrdine(int index) {
        return controller.rimuoviProdottoDaOrdine(index);
    }

    public VoucherBean applicaVoucher(String codiceVoucher)
            throws DAOException, ObjectNotFoundException, MissingAuthorizationException, WrongListQueryIdentifierValue,
            UserNotFoundException, UnrecognizedRoleException {
        return controller.applicaVoucher(codiceVoucher);
    }

    public void rimuoviVoucher() {
        controller.rimuoviVoucher();
    }

    public RiepilogoOrdineBean getRiepilogoOrdine() {
        return controller.getRiepilogoOrdine();
    }

    public boolean confermaOrdine() throws DAOException, MissingAuthorizationException {
        return controller.confermaOrdine();
    }

    public void annullaOrdine() {
        controller.annullaOrdine();
    }
}
