package org.example.model.ordine;

import org. example.dao_manager.DAOFactoryAbstract;
import org.example.enums.ExceptionMessagesEnum;
import org.example.exceptions.*;
import org.example.model.voucher. Voucher;
import org.example.model.voucher.VoucherLazyFactory;
import org.example.enums.StatoOrdine;
import java.util.ArrayList;
import java.util.List;

/**
 * LazyFactory per la gestione degli Ordini.
 * Implementa il pattern Lazy Initialization con caching locale.
 * Segue lo stesso pattern di ClienteLazyFactory, KebabbaroLazyFactory, etc.
 */
public class OrdineLazyFactory {

    private static OrdineLazyFactory instance;
    private final List<Ordine> ordiniCache;

    private OrdineLazyFactory() {
        ordiniCache = new ArrayList<>();
    }

    public static synchronized OrdineLazyFactory getInstance() {
        if (instance == null) {
            instance = new OrdineLazyFactory();
        }
        return instance;
    }

    // ==================== METODI DI RECUPERO ====================

    /**
     * Recupera un ordine per numero ordine.
     * Cerca prima nella cache, poi nel database.
     *
     * @param numeroOrdine il numero dell'ordine
     * @return l'oggetto Ordine
     * @throws DAOException errori durante l'accesso al persistence layer
     * @throws ObjectNotFoundException se l'ordine non viene trovato
     */
    public Ordine getOrdineByNumero(Long numeroOrdine) throws DAOException, ObjectNotFoundException,
            MissingAuthorizationException, WrongListQueryIdentifierValue, UserNotFoundException, UnrecognizedRoleException {

        // Cerca prima nella cache
        for (Ordine o : ordiniCache) {
            if (o.getNumeroOrdine() != null && o.getNumeroOrdine().equals(numeroOrdine)) {
                return o;
            }
        }

        // Se non trovato, recupera dal DAO
        try {
            Ordine daoOrdine = DAOFactoryAbstract.getInstance().getOrdineDAO().getOrdineByNumero(numeroOrdine);

            // Carica il voucher se presente
            caricaVoucherPerOrdine(daoOrdine);

            ordiniCache.add(daoOrdine);
            return daoOrdine;
        } catch (PropertyException | ResourceNotFoundException e) {
            throw new DAOException(ExceptionMessagesEnum. DAO. message, e);
        }
    }

    /**
     * Recupera tutti gli ordini di un cliente.
     *
     * @param clienteId l'ID del cliente
     * @return lista di Ordini del cliente
     * @throws DAOException errori durante l'accesso al persistence layer
     */
    public List<Ordine> getOrdiniByCliente(String clienteId) throws DAOException, ObjectNotFoundException,
            MissingAuthorizationException, WrongListQueryIdentifierValue, UserNotFoundException, UnrecognizedRoleException {

        try {
            List<Ordine> ordini = DAOFactoryAbstract.getInstance().getOrdineDAO().getOrdiniByCliente(clienteId);

            // Aggiorna cache e carica voucher
            for (Ordine ordine : ordini) {
                if (!isOrdineInCache(ordine. getNumeroOrdine())) {
                    caricaVoucherPerOrdine(ordine);
                    ordiniCache.add(ordine);
                }
            }

            return ordini;
        } catch (PropertyException | ResourceNotFoundException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    /**
     * Recupera tutti gli ordini con un determinato stato.
     *
     * @param stato lo stato degli ordini da cercare
     * @return lista di Ordini con lo stato specificato
     * @throws DAOException errori durante l'accesso al persistence layer
     */
    public List<Ordine> getOrdiniByStato(StatoOrdine stato) throws DAOException, ObjectNotFoundException,
            MissingAuthorizationException, WrongListQueryIdentifierValue, UserNotFoundException, UnrecognizedRoleException {

        try {
            List<Ordine> ordini = DAOFactoryAbstract.getInstance().getOrdineDAO().getOrdiniByStato(stato);

            // Aggiorna cache
            for (Ordine ordine : ordini) {
                if (!isOrdineInCache(ordine.getNumeroOrdine())) {
                    caricaVoucherPerOrdine(ordine);
                    ordiniCache.add(ordine);
                }
            }

            return ordini;
        } catch (PropertyException | ResourceNotFoundException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    // ==================== METODI DI CREAZIONE ====================

    /**
     * Crea un nuovo ordine per un cliente.
     * L'ordine viene creato in stato IN_CREAZIONE e NON viene ancora salvato nel DB.
     *
     * @param clienteId l'ID del cliente
     * @return il nuovo Ordine creato
     */
    public Ordine newOrdine(String clienteId) throws DAOException, MissingAuthorizationException {
        try {
            // Genera il numero ordine
            Long numeroOrdine = DAOFactoryAbstract.getInstance().getOrdineDAO().getNextNumeroOrdine();

            // Crea il nuovo ordine
            Ordine ordine = new Ordine(clienteId);
            ordine.setNumeroOrdine(numeroOrdine);

            // Aggiungi alla cache (non ancora salvato nel DB)
            ordiniCache.add(ordine);

            return ordine;

        } catch (PropertyException | ResourceNotFoundException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    // ==================== METODI DI SALVATAGGIO ====================

    /**
     * Salva un ordine nel database.
     * Usato quando l'ordine viene confermato.
     *
     * @param ordine l'ordine da salvare
     * @throws DAOException errori durante l'accesso al persistence layer
     */
    public void salvaOrdine(Ordine ordine) throws DAOException, MissingAuthorizationException {
        try {
            // Verifica se l'ordine esiste già nel DB
            boolean esisteNelDb = false;
            try {
                DAOFactoryAbstract.getInstance().getOrdineDAO().getOrdineByNumero(ordine.getNumeroOrdine());
                esisteNelDb = true;
            } catch (ObjectNotFoundException e) {
                esisteNelDb = false;
            }

            if (esisteNelDb) {
                // Aggiorna
                DAOFactoryAbstract.getInstance().getOrdineDAO().update(ordine);
            } else {
                // Inserisci
                DAOFactoryAbstract.getInstance().getOrdineDAO().insert(ordine);
            }

        } catch (PropertyException | ResourceNotFoundException | UserNotFoundException |
                 UnrecognizedRoleException | WrongListQueryIdentifierValue e) {
            throw new DAOException(ExceptionMessagesEnum. DAO.message, e);
        }
    }

    /**
     * Aggiorna un ordine esistente nel database.
     *
     * @param ordine l'ordine da aggiornare
     * @throws DAOException errori durante l'accesso al persistence layer
     */
    public void aggiornaOrdine(Ordine ordine) throws DAOException, MissingAuthorizationException {
        try {
            DAOFactoryAbstract.getInstance().getOrdineDAO().update(ordine);
        } catch (PropertyException | ResourceNotFoundException e) {
            throw new DAOException(ExceptionMessagesEnum. DAO.message, e);
        }
    }

    /**
     * Elimina un ordine.
     *
     * @param ordine l'ordine da eliminare
     * @throws DAOException errori durante l'accesso al persistence layer
     */
    public void eliminaOrdine(Ordine ordine) throws DAOException {
        try {
            DAOFactoryAbstract.getInstance(). getOrdineDAO().delete(ordine);
            ordiniCache.remove(ordine);
        } catch (PropertyException | ResourceNotFoundException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    // ==================== METODI PRIVATI DI SUPPORTO ====================

    /**
     * Verifica se un ordine è già presente nella cache.
     */
    private boolean isOrdineInCache(Long numeroOrdine) {
        if (numeroOrdine == null) return false;
        for (Ordine o : ordiniCache) {
            if (o.getNumeroOrdine() != null && o.getNumeroOrdine().equals(numeroOrdine)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Carica il voucher associato a un ordine (se presente).
     */
    private void caricaVoucherPerOrdine(Ordine ordine) {
        // Il voucher viene gestito separatamente
        // Se necessario, può essere caricato tramite VoucherLazyFactory
        // Per ora l'ordine ha già NessunVoucher di default
    }

    /**
     * Pulisce la cache (utile per testing o logout).
     */
    public void clearCache() {
        ordiniCache.clear();
    }

    /**
     * Rimuove un ordine specifico dalla cache.
     */
    public void removeFromCache(Ordine ordine) {
        ordiniCache.remove(ordine);
    }
}