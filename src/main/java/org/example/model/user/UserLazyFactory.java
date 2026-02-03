package org.example.model.user;

import org.example.dao_manager.DAOFactoryAbstract;
import org.example.exceptions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * LazyFactory per la gestione degli User.
 * Implementa il pattern Lazy Initialization con caching locale.
 */
public class UserLazyFactory {
    private static UserLazyFactory instance;
    private final List<User> registeredUsers;

    private UserLazyFactory() {
        registeredUsers = new ArrayList<>();
    }

    public static synchronized UserLazyFactory getInstance() {
        if (instance == null) {
            instance = new UserLazyFactory();
        }
        return instance;
    }

    /**
     * Gets a User by its email.
     * Cerca prima nella cache, poi nel database.
     */
    public User getUserByEmail(String email) throws DAOException, UserNotFoundException, PropertyException,
            ResourceNotFoundException, UnrecognizedRoleException, WrongListQueryIdentifierValue,
            ObjectNotFoundException, MissingAuthorizationException {

        for (User u : registeredUsers) {
            if (u.getEmail().equals(email)) {
                return u;
            }
        }

        User daoUser = DAOFactoryAbstract.getInstance().getUserDAO().getUserByEmail(email);
        registeredUsers.add(daoUser);
        return daoUser;
    }
}
