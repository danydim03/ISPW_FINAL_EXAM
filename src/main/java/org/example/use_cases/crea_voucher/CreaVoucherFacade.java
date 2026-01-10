package org.example.use_cases.crea_voucher;

import org.example.exceptions.*;
import org.example.model.voucher.Voucher;
import org.example.session_manager.SessionManager;
import org.example.use_cases.crea_voucher.beans.CreaVoucherBean;

/**
 * Facade per lo Use Case "Crea Voucher".
 * 
 * Fornisce un'interfaccia semplificata per la creazione di voucher,
 * gestendo autenticazione e autorizzazione.
 * 
 * Solo gli utenti con ruolo AMMINISTRATORE possono creare voucher.
 * 
 * Segue il pattern Facade per nascondere la complessità del sottosistema
 * e applicare il principio GRASP di Low Coupling.
 */
public class CreaVoucherFacade {

    private final CreaVoucherController controller;
    private final org.example.model.user.User sessionUser;

    /**
     * Costruttore del Facade.
     * Verifica l'autenticazione e l'autorizzazione dell'utente.
     * 
     * @param tokenKey chiave del token di sessione
     * @throws MissingAuthorizationException se l'utente non è autenticato o non è
     *                                       amministratore
     */
    public CreaVoucherFacade(String tokenKey) throws MissingAuthorizationException {
        this.sessionUser = SessionManager.getInstance().getSessionUserByTokenKey(tokenKey);

        if (sessionUser == null) {
            throw new MissingAuthorizationException("Sessione non valida o scaduta");
        }

        // Verifica che l'utente abbia il ruolo di Amministratore
        if (sessionUser.getRole() == null || sessionUser.getRole().getAmministratoreRole() == null) {
            throw new MissingAuthorizationException(
                    "Accesso negato: solo gli amministratori possono creare voucher");
        }

        this.controller = new CreaVoucherController();
    }

    /**
     * Crea un nuovo voucher nel sistema.
     * 
     * @param bean il bean contenente i dati del voucher da creare
     * @return il Voucher creato e persistito
     * @throws ValidationException           se i dati non sono validi
     * @throws DAOException                  se si verifica un errore di persistenza
     * @throws PropertyException             se ci sono problemi con le properties
     * @throws ResourceNotFoundException     se una risorsa necessaria non è trovata
     * @throws MissingAuthorizationException se mancano autorizzazioni
     */
    public Voucher creaVoucher(CreaVoucherBean bean)
            throws ValidationException, DAOException, PropertyException,
            ResourceNotFoundException, MissingAuthorizationException {
        return controller.creaVoucher(bean);
    }
}
