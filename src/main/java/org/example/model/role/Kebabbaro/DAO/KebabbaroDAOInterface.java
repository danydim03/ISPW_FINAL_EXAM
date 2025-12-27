package org.example.model.role.Kebabbaro.DAO;

import org.example.exceptions.*;
import org.example.model.role.Kebabbaro.Kebabbaro;
import org.example.model.user.User;

public interface KebabbaroDAOInterface {
    /**
     * Inserts an object into the DB
     * 
     * @param kebabbaro the object to insert
     * @throws DAOException              thrown if errors occur while retrieving
     *                                   data from persistence layer
     * @throws PropertyException         thrown if errors occur while loading
     *                                   properties from .properties file
     * @throws ResourceNotFoundException thrown if the properties resource file
     *                                   cannot be found
     */
    void insert(Kebabbaro kebabbaro)
            throws DAOException, PropertyException, ResourceNotFoundException, MissingAuthorizationException;

    /**
     * Deletes an object from the DB
     * 
     * @param kebabbaro the object to delete
     * @throws DAOException              thrown if errors occur while retrieving
     *                                   data from persistence layer
     * @throws PropertyException         thrown if errors occur while loading
     *                                   properties from .properties file
     * @throws ResourceNotFoundException thrown if the properties resource file
     *                                   cannot be found
     */
    void delete(Kebabbaro kebabbaro) throws DAOException, PropertyException, ResourceNotFoundException;

    /**
     * Updates an object present in the DB to its current state
     * 
     * @param kebabbaro the object to be updated
     * @throws DAOException              thrown if errors occur while retrieving
     *                                   data from persistence layer
     * @throws PropertyException         thrown if errors occur while loading
     *                                   properties from .properties file
     * @throws ResourceNotFoundException thrown if the properties resource file
     *                                   cannot be found
     */
    void update(Kebabbaro kebabbaro)
            throws DAOException, PropertyException, ResourceNotFoundException, MissingAuthorizationException;

    abstract Kebabbaro getKebabbaroByUser(User user) throws DAOException, UserNotFoundException, PropertyException,
            ResourceNotFoundException, UnrecognizedRoleException, ObjectNotFoundException,
            MissingAuthorizationException, WrongListQueryIdentifierValue;
}