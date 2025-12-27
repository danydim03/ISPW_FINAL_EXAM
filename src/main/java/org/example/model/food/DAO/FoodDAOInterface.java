package org.example.model.food.DAO;

import org.example.exceptions.*;
import org.example.model.food.Food;

import java.util.List;

public interface FoodDAOInterface {

        /**
         * Inserisce un Food nel DB
         * 
         * @param food l'oggetto da inserire
         * @throws DAOException              errori durante l'accesso al persistence
         *                                   layer
         * @throws PropertyException         errori nel caricamento delle properties
         * @throws ResourceNotFoundException risorsa properties non trovata
         */
        void insert(Food food) throws DAOException, PropertyException, ResourceNotFoundException,
                        MissingAuthorizationException;

        /**
         * Elimina un Food dal DB
         * 
         * @param food l'oggetto da eliminare
         * @throws DAOException              errori durante l'accesso al persistence
         *                                   layer
         * @throws PropertyException         errori nel caricamento delle properties
         * @throws ResourceNotFoundException risorsa properties non trovata
         */
        void delete(Food food) throws DAOException, PropertyException, ResourceNotFoundException;

        /**
         * Aggiorna un Food nel DB
         * 
         * @param food l'oggetto da aggiornare
         * @throws DAOException              errori durante l'accesso al persistence
         *                                   layer
         * @throws PropertyException         errori nel caricamento delle properties
         * @throws ResourceNotFoundException risorsa properties non trovata
         */
        void update(Food food) throws DAOException, PropertyException, ResourceNotFoundException,
                        MissingAuthorizationException;

        /**
         * Recupera un Food per ID
         * 
         * @param id l'ID del Food
         * @return l'oggetto Food
         * @throws DAOException              errori durante l'accesso al persistence
         *                                   layer
         * @throws ObjectNotFoundException   se il Food non viene trovato
         * @throws PropertyException         errori nel caricamento delle properties
         * @throws ResourceNotFoundException risorsa properties non trovata
         */
        Food getFoodById(Long id)
                        throws DAOException, ObjectNotFoundException, PropertyException, ResourceNotFoundException,
                        UserNotFoundException, UnrecognizedRoleException, MissingAuthorizationException,
                        WrongListQueryIdentifierValue;

        /**
         * Recupera tutti i Food di tipo BASE (prodotti base)
         * 
         * @return lista di Food base
         * @throws DAOException              errori durante l'accesso al persistence
         *                                   layer
         * @throws PropertyException         errori nel caricamento delle properties
         * @throws ResourceNotFoundException risorsa properties non trovata
         */
        List<Food> getAllFoodBase() throws DAOException, PropertyException, ResourceNotFoundException,
                        UserNotFoundException, UnrecognizedRoleException, ObjectNotFoundException,
                        MissingAuthorizationException, WrongListQueryIdentifierValue;

        /**
         * Recupera tutti i Food di tipo ADDON
         * 
         * @return lista di Food addon
         * @throws DAOException              errori durante l'accesso al persistence
         *                                   layer
         * @throws PropertyException         errori nel caricamento delle properties
         * @throws ResourceNotFoundException risorsa properties non trovata
         */
        List<Food> getAllAddOn() throws DAOException, PropertyException, ResourceNotFoundException,
                        UserNotFoundException, UnrecognizedRoleException, ObjectNotFoundException,
                        MissingAuthorizationException, WrongListQueryIdentifierValue;
}