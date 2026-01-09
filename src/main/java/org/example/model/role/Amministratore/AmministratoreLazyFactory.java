package org.example.model.role.Amministratore;

import org.example.dao_manager.DAOFactoryAbstract;
import org.example.enums.ExceptionMessagesEnum;
import org.example.exceptions.*;
import org.example.model.user.User;

import java.util.ArrayList;
import java.util.List;

public class AmministratoreLazyFactory {
    private static AmministratoreLazyFactory instance;
    private final List<Amministratore> amministratori;

    private AmministratoreLazyFactory() {
        amministratori = new ArrayList<>();
    }

    public static synchronized AmministratoreLazyFactory getInstance() {
        if (instance == null) {
            instance = new AmministratoreLazyFactory();
        }
        return instance;
    }

    public Amministratore getAmministratoreByUser(User user)
            throws DAOException, UserNotFoundException, UnrecognizedRoleException, ObjectNotFoundException,
            MissingAuthorizationException, WrongListQueryIdentifierValue {
        for (Amministratore a : amministratori) {
            if (a.getUser().equals(user)) {
                return a;
            }
        }
        try {
            Amministratore daoAdmin = DAOFactoryAbstract.getInstance().getAmministratoreDAO()
                    .getAmministratoreByUser(user);
            amministratori.add(daoAdmin);
            return daoAdmin;
        } catch (PropertyException | ResourceNotFoundException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    public Amministratore newAmministratore(User user) throws DAOException, MissingAuthorizationException {
        // Usa il costruttore che accetta solo User (usa valori di default)
        Amministratore admin = new Amministratore(user);
        user.setRole(admin);
        try {
            DAOFactoryAbstract.getInstance().getAmministratoreDAO().insert(admin);
        } catch (PropertyException | ResourceNotFoundException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
        amministratori.add(admin);
        return admin;
    }
}
