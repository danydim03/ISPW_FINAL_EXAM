package org.example.model.ordine;

import org.example.dao_manager.DAOFactoryAbstract;
import org.example.enums.ExceptionMessagesEnum;
import org.example.exceptions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * LazyFactory per la gestione degli Ordini.
 * Implementa il pattern Lazy Initialization con caching locale.
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
            Long numeroOrdine = DAOFactoryAbstract.getInstance().getOrdineDAO().getNextNumeroOrdine();
            Ordine ordine = new Ordine(clienteId);
            ordine.setNumeroOrdine(numeroOrdine);
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
            DAOFactoryAbstract.getInstance().getOrdineDAO().insert(ordine);
        } catch (PropertyException | ResourceNotFoundException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    // ==================== METODI DI RICERCA (Identity Map) ====================

    /**
     * Recupera un ordine in corso tramite il suo ID.
     * Usa la List esistente per semplicità (pattern Identity Map semplificato).
     *
     * @param ordineId il numero dell'ordine da cercare
     * @return l'Ordine se trovato, null altrimenti
     */
    public Ordine getOrdineById(String ordineId) {
        for (Ordine o : ordiniCache) {
            if (String.valueOf(o.getNumeroOrdine()).equals(ordineId)) {
                return o;
            }
        }
        return null;
    }

    /**
     * Rimuove un ordine dalla cache (dopo conferma/annullamento).
     *
     * @param ordineId il numero dell'ordine da rimuovere
     */
    public void rimuoviOrdineInCorso(String ordineId) {
        ordiniCache.removeIf(o -> String.valueOf(o.getNumeroOrdine()).equals(ordineId));
    }
}
