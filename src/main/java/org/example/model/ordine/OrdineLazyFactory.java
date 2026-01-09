package org.example.model.ordine;

import org.example.dao_manager.DAOFactoryAbstract;
import org.example.enums.ExceptionMessagesEnum;
import org.example.enums.StatoOrdine;
import org.example.exceptions.*;

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
     * @throws DAOException            errori durante l'accesso al persistence layer
     * @throws ObjectNotFoundException se l'ordine non viene trovato
     */
    public Ordine getOrdineByNumero(Long numeroOrdine) throws DAOException, ObjectNotFoundException,
            MissingAuthorizationException, WrongListQueryIdentifierValue, UserNotFoundException,
            UnrecognizedRoleException {

        // Cerca prima nella cache
        for (Ordine o : ordiniCache) {
            if (o.getNumeroOrdine() != null && o.getNumeroOrdine().equals(numeroOrdine)) {
                return o;
            }
        }

        // Se non trovato, recupera dal DAO
        try {
            Ordine daoOrdine = DAOFactoryAbstract.getInstance().getOrdineDAO().getOrdineByNumero(numeroOrdine);
            ordiniCache.add(daoOrdine);
            return daoOrdine;
        } catch (PropertyException | ResourceNotFoundException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
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
            MissingAuthorizationException, WrongListQueryIdentifierValue, UserNotFoundException,
            UnrecognizedRoleException {

        try {
            List<Ordine> ordini = DAOFactoryAbstract.getInstance().getOrdineDAO().getOrdiniByCliente(clienteId);

            // Aggiorna cache
            for (Ordine ordine : ordini) {
                if (!isOrdineInCache(ordine.getNumeroOrdine())) {
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
            MissingAuthorizationException, WrongListQueryIdentifierValue, UserNotFoundException,
            UnrecognizedRoleException {

        try {
            List<Ordine> ordini = DAOFactoryAbstract.getInstance().getOrdineDAO().getOrdiniByStato(stato);

            // Aggiorna cache
            for (Ordine ordine : ordini) {
                if (!isOrdineInCache(ordine.getNumeroOrdine())) {
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
     * L'ordine viene creato in stato IN_CREAZIONE e NON viene ancora salvato nel
     * DB.
     *
     * @param clienteId l'ID del cliente
     * @return il nuovo Ordine creato
     */
    public Ordine newOrdine(String clienteId) throws DAOException {
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
            // Inserisci direttamente l'ordine (è nuovo, generato con un numero univoco)
            System.out.println("[DEBUG] Tentativo di salvare ordine #" + ordine.getNumeroOrdine());
            System.out.println("[DEBUG] ClienteId: " + ordine.getClienteId());
            System.out.println("[DEBUG] Stato: " + ordine.getStato());
            System.out.println("[DEBUG] Totale: " + ordine.getTotale());

            DAOFactoryAbstract.getInstance().getOrdineDAO().insert(ordine);
            System.out.println("[DEBUG] Ordine salvato con successo!");

        } catch (DAOException e) {
            System.err.println("[DEBUG ERROR - DAOException] " + e.getMessage());
            e.printStackTrace();
            throw e; // Rilancia l'eccezione originale
        } catch (PropertyException | ResourceNotFoundException e) {
            System.err.println("[DEBUG ERROR] Errore durante il salvataggio: " + e.getClass().getSimpleName() + " - "
                    + e.getMessage());
            e.printStackTrace();
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
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
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
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
            DAOFactoryAbstract.getInstance().getOrdineDAO().delete(ordine);
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
        if (numeroOrdine == null)
            return false;
        for (Ordine o : ordiniCache) {
            if (o.getNumeroOrdine() != null && o.getNumeroOrdine().equals(numeroOrdine)) {
                return true;
            }
        }
        return false;
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
