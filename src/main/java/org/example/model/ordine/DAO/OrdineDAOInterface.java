package org.example.model.ordine.DAO;

import org.example.exceptions.*;
import org.example.model.ordine.Ordine;

import java.util.List;
import org.example.enums.StatoOrdine;

/**
 * Interfaccia DAO per la gestione della persistenza degli Ordini.
 */
public interface OrdineDAOInterface {

        /**
         * Inserisce un nuovo Ordine nel DB
         * 
         * @param ordine l'ordine da inserire
         * @throws DAOException              errori durante l'accesso al persistence
         *                                   layer
         * @throws PropertyException         errori nel caricamento delle properties
         * @throws ResourceNotFoundException risorsa properties non trovata
         */
        void insert(Ordine ordine) throws DAOException, PropertyException, ResourceNotFoundException,
                        MissingAuthorizationException;

        /**
         * Elimina un Ordine dal DB
         * 
         * @param ordine l'ordine da eliminare
         * @throws DAOException              errori durante l'accesso al persistence
         *                                   layer
         * @throws PropertyException         errori nel caricamento delle properties
         * @throws ResourceNotFoundException risorsa properties non trovata
         */
        void delete(Ordine ordine) throws DAOException, PropertyException, ResourceNotFoundException;

        /**
         * Aggiorna un Ordine nel DB
         * 
         * @param ordine l'ordine da aggiornare
         * @throws DAOException              errori durante l'accesso al persistence
         *                                   layer
         * @throws PropertyException         errori nel caricamento delle properties
         * @throws ResourceNotFoundException risorsa properties non trovata
         */
        void update(Ordine ordine) throws DAOException, PropertyException, ResourceNotFoundException,
                        MissingAuthorizationException;

        /**
         * Recupera un Ordine per numero ordine
         * 
         * @param numeroOrdine il numero dell'ordine
         * @return l'oggetto Ordine
         * @throws DAOException              errori durante l'accesso al persistence
         *                                   layer
         * @throws ObjectNotFoundException   se l'ordine non viene trovato
         * @throws PropertyException         errori nel caricamento delle properties
         * @throws ResourceNotFoundException risorsa properties non trovata
         */
        Ordine getOrdineByNumero(Long numeroOrdine) throws DAOException, ObjectNotFoundException, PropertyException,
                        ResourceNotFoundException, UserNotFoundException, UnrecognizedRoleException,
                        MissingAuthorizationException, WrongListQueryIdentifierValue;

        /**
         * Recupera tutti gli ordini di un cliente
         * 
         * @param clienteId l'ID del cliente
         * @return lista di Ordini del cliente
         * @throws DAOException              errori durante l'accesso al persistence
         *                                   layer
         * @throws PropertyException         errori nel caricamento delle properties
         * @throws ResourceNotFoundException risorsa properties non trovata
         */
        List<Ordine> getOrdiniByCliente(String clienteId)
                        throws DAOException, PropertyException, ResourceNotFoundException,
                        UserNotFoundException, UnrecognizedRoleException, ObjectNotFoundException,
                        MissingAuthorizationException, WrongListQueryIdentifierValue;

        /**
         * Recupera tutti gli ordini con un determinato stato
         * 
         * @param stato lo stato degli ordini da cercare
         * @return lista di Ordini con lo stato specificato
         * @throws DAOException              errori durante l'accesso al persistence
         *                                   layer
         * @throws PropertyException         errori nel caricamento delle properties
         * @throws ResourceNotFoundException risorsa properties non trovata
         */
        List<Ordine> getOrdiniByStato(StatoOrdine stato)
                        throws DAOException, PropertyException, ResourceNotFoundException,
                        UserNotFoundException, UnrecognizedRoleException, ObjectNotFoundException,
                        MissingAuthorizationException, WrongListQueryIdentifierValue;

        /**
         * Genera il prossimo numero ordine disponibile
         * 
         * @return il prossimo numero ordine
         * @throws DAOException              errori durante l'accesso al persistence
         *                                   layer
         * @throws PropertyException         errori nel caricamento delle properties
         * @throws ResourceNotFoundException risorsa properties non trovata
         */
        Long getNextNumeroOrdine() throws DAOException, PropertyException, ResourceNotFoundException;
}