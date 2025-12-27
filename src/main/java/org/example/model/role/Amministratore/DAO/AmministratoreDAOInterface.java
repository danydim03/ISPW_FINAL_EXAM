package org.example.model.role.Amministratore.DAO;

import org.example.exceptions.*;
import org.example.model.role.Amministratore.Amministratore;
import org.example.model.user.User;

public interface AmministratoreDAOInterface {
    /**
     * Inserts an object into the DB
     * 
     * @param amministratore the object to insert
     * @throws DAOException              thrown if errors occur while retrieving
     *                                   data from persistence layer
     * @throws PropertyException         thrown if errors occur while loading
     *                                   properties from .properties file
     * @throws ResourceNotFoundException thrown if the properties resource file
     *                                   cannot be found
     */
    void insert(Amministratore amministratore)
            throws DAOException, PropertyException, ResourceNotFoundException, MissingAuthorizationException;

    /**
     * Deletes an object from the DB
     * 
     * @param amministratore the object to delete
     * @throws DAOException              thrown if errors occur while retrieving
     *                                   data from persistence layer
     * @throws PropertyException         thrown if errors occur while loading
     *                                   properties from .properties file
     * @throws ResourceNotFoundException thrown if the properties resource file
     *                                   cannot be found
     */
    void delete(Amministratore amministratore) throws DAOException, PropertyException, ResourceNotFoundException;

    /**
     * Updates an object present in the DB to its current state
     * 
     * @param amministratore the object to be updated
     * @throws DAOException              thrown if errors occur while retrieving
     *                                   data from persistence layer
     * @throws PropertyException         thrown if errors occur while loading
     *                                   properties from .properties file
     * @throws ResourceNotFoundException thrown if the properties resource file
     *                                   cannot be found
     */
    void update(Amministratore amministratore)
            throws DAOException, PropertyException, ResourceNotFoundException, MissingAuthorizationException;

    /**
     * Retrieves an Amministratore by User
     * 
     * @param user the user associated with the Amministratore
     * @return the Amministratore object
     * @throws DAOException              thrown if errors occur while retrieving
     *                                   data from persistence layer
     * @throws UserNotFoundException     thrown if the user is not found
     * @throws PropertyException         thrown if errors occur while loading
     *                                   properties from .properties file
     * @throws ResourceNotFoundException thrown if the properties resource file
     *                                   cannot be found
     */
    Amministratore getAmministratoreByUser(User user) throws DAOException, UserNotFoundException, PropertyException,
            ResourceNotFoundException, UnrecognizedRoleException, ObjectNotFoundException,
            MissingAuthorizationException, WrongListQueryIdentifierValue;
}