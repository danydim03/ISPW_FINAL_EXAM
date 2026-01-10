package org.example.use_cases.crea_voucher;

import org.example.exceptions.*;
import org.example.model.voucher.*;
import org.example.use_cases.crea_voucher.beans.CreaVoucherBean;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller Applicativo per lo Use Case "Crea Voucher".
 * 
 * Responsabilità:
 * - Orchestrare la logica di business per la creazione di voucher
 * - Validare regole semantiche (unicità codice, business constraints)
 * - Convertire Bean in Entity
 * - Coordinare con DAO per la persistenza
 * 
 * Segue il pattern BCE: questo è il CONTROL.
 * Applica i principi GRASP: Controller, Information Expert, Creator, Low
 * Coupling.
 */
public class CreaVoucherController {

    private static final Logger logger = Logger.getLogger(CreaVoucherController.class.getName());

    public CreaVoucherController() {
        // Controller inizializzato dal Facade
    }

    /**
     * Crea un nuovo voucher nel sistema.
     * 
     * Esegue validazione semantica (business rules):
     * - Verifica unicità del codice voucher
     * - Valida constraints di business
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

        // 1. Validazione sintattica del bean (già fatta nei setter, ma richiamo per
        // sicurezza)
        bean.validate();

        // 2. Validazione semantica: verifica unicità codice
        verificaUnicitaCodice(bean.getCodice());

        // 3. Crea l'entity Voucher appropriata (GRASP: Creator pattern)
        Voucher voucher = creaVoucherEntity(bean);

        // 4. Persisti il voucher tramite DAO
        persistiVoucher(voucher);

        logger.log(Level.INFO, () -> "Voucher creato con successo: " + voucher.getCodice());

        return voucher;
    }

    /**
     * Verifica che il codice voucher non esista già nel sistema.
     * 
     * @param codice il codice da verificare
     * @throws ValidationException se il codice esiste già
     */
    private void verificaUnicitaCodice(String codice) throws ValidationException {
        try {
            // Tento di recuperare un voucher con questo codice
            VoucherLazyFactory.getInstance().getVoucherByCodice(codice);

            // Se arrivo qui, il voucher esiste già
            throw new ValidationException(
                    "Esiste già un voucher con il codice: " + codice);

        } catch (ObjectNotFoundException e) {
            // Il voucher non esiste, ok per procedere
            logger.log(Level.FINE, () -> "Codice voucher disponibile: " + codice);
        } catch (DAOException | MissingAuthorizationException | WrongListQueryIdentifierValue | UserNotFoundException
                | UnrecognizedRoleException e) {
            // Errore durante la verifica, loggo e rilancio come ValidationException
            logger.log(Level.SEVERE, "Errore durante la verifica unicità codice voucher", e);
            throw new ValidationException("Impossibile verificare l'unicità del codice voucher", e);
        }
    }

    /**
     * Crea l'entity Voucher appropriata in base al tipo specificato nel bean.
     * Applica il pattern Creator: questo controller ha le informazioni necessarie
     * per inizializzare il voucher.
     * 
     * @param bean il bean con i dati
     * @return l'entity Voucher creata (non ancora persistita)
     * @throws ValidationException se il tipo di voucher non è riconosciuto
     */
    private Voucher creaVoucherEntity(CreaVoucherBean bean) throws ValidationException {

        String tipo = bean.getTipoVoucher();

        // Pattern matching for instanceof (Java 17) - evita code smell
        if ("PERCENTUALE".equals(tipo)) {
            return creaVoucherPercentuale(bean);
        } else if ("FISSO".equals(tipo)) {
            return creaVoucherFisso(bean);
        } else {
            throw new ValidationException("Tipo voucher non riconosciuto: " + tipo);
        }
    }

    /**
     * Crea un voucher con sconto percentuale.
     * 
     * @param bean il bean con i dati
     * @return VoucherPercentuale creato
     */
    private VoucherPercentuale creaVoucherPercentuale(CreaVoucherBean bean) {
        VoucherPercentuale voucher = new VoucherPercentuale(
                bean.getCodice(),
                bean.getValore());

        // Imposta la data di scadenza se presente
        if (bean.getDataScadenza() != null) {
            voucher.setDataScadenza(bean.getDataScadenza());
        }

        return voucher;
    }

    /**
     * Crea un voucher con sconto fisso.
     * 
     * @param bean il bean con i dati
     * @return VoucherFisso creato
     */
    private VoucherFisso creaVoucherFisso(CreaVoucherBean bean) {
        VoucherFisso voucher = new VoucherFisso(
                bean.getCodice(),
                bean.getValore(),
                bean.getMinimoOrdine());

        // Imposta la data di scadenza se presente
        if (bean.getDataScadenza() != null) {
            voucher.setDataScadenza(bean.getDataScadenza());
        }

        return voucher;
    }

    /**
     * Persiste il voucher tramite il DAO.
     * Applica GRASP Low Coupling: usa l'interfaccia DAO tramite Factory.
     * 
     * @param voucher il voucher da persistere
     * @throws DAOException                  se si verifica un errore durante la
     *                                       persistenza
     * @throws PropertyException             se ci sono problemi con le properties
     * @throws ResourceNotFoundException     se una risorsa necessaria non è trovata
     * @throws MissingAuthorizationException se mancano autorizzazioni
     */
    private void persistiVoucher(Voucher voucher)
            throws DAOException, PropertyException, ResourceNotFoundException,
            MissingAuthorizationException {

        try {
            // Usa il VoucherLazyFactory per persistere il voucher
            // (DB, FileSystem o Demo a seconda della configurazione)
            VoucherLazyFactory.getInstance().newVoucher(voucher);

            logger.log(Level.INFO, () -> "Voucher persistito: " + voucher.getCodice());

        } catch (DAOException | MissingAuthorizationException e) {
            // Loggo l'errore e rilancio l'eccezione con chaining
            logger.log(Level.SEVERE, "Errore durante la persistenza del voucher", e);
            throw e;
        }
    }
}
