package org.example.model.role.Kebabbaro;

import org.example.dao_manager.DAOFactoryAbstract;
import org.example.enums.ExceptionMessagesEnum;
import org.example.exceptions.*;
import org.example.model.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * LazyFactory per la gestione dei Kebabbari.
 * Implementa il pattern Lazy Initialization con caching locale.
 */
public class KebabbaroLazyFactory {
    private static KebabbaroLazyFactory instance;
    private final List<Kebabbaro> kebabbari;

    private KebabbaroLazyFactory() {
        kebabbari = new ArrayList<>();
    }

    public static synchronized KebabbaroLazyFactory getInstance() {
        if (instance == null) {
            instance = new KebabbaroLazyFactory();
        }
        return instance;
    }

    /**
     * Gets a Kebabbaro by its User.
     * Cerca prima nella cache, poi nel database.
     */
    public Kebabbaro getKebabbaroByUser(User user)
            throws DAOException, UserNotFoundException, UnrecognizedRoleException,
            ObjectNotFoundException, MissingAuthorizationException, WrongListQueryIdentifierValue {
        for (Kebabbaro k : kebabbari) {
            if (k.getUser().equals(user)) {
                return k;
            }
        }
        try {
            Kebabbaro daoKebabbaro = DAOFactoryAbstract.getInstance().getKebabbaroDAO().getKebabbaroByUser(user);
            kebabbari.add(daoKebabbaro);
            return daoKebabbaro;
        } catch (PropertyException | ResourceNotFoundException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }
}