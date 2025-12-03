package org.example.instances_management_abstracts;

import org.example.exceptions.DAOException;
import org.example.exceptions.MissingAuthorizationException;
import org.example.exceptions.PropertyException;
import org.example.exceptions.ResourceNotFoundException;

import java.util.List;

public abstract class LazyFactoryAbstract <T>{
    protected List<T> factoryObjects;

    /**
     * Deletes an object from System and DB
     * @param t the object to delete
     * @throws DAOException thrown if errors occur while retrieving data from persistence layer
     * @throws PropertyException thrown if errors occur while loading properties from .properties file
     * @throws ResourceNotFoundException thrown if the properties resource file cannot be found
     */
    protected abstract void delete(T t) throws DAOException, PropertyException, ResourceNotFoundException;

    /**
     * Updates an object present in DB to its current state
     * @param t the object to be updated
     * @throws DAOException thrown if errors occur while retrieving data from persistence layer
     * @throws PropertyException thrown if errors occur while loading properties from .properties file
     * @throws ResourceNotFoundException thrown if the properties resource file cannot be found
     */
    protected abstract void update(T t) throws DAOException, PropertyException, ResourceNotFoundException, MissingAuthorizationException;

}
