package org.example.model.role.Cliente;

import org.example.dao_manager.DAOFactoryAbstract;
import org.example.enums.ExceptionMessagesEnum;
import org.example.exceptions.*;
import org.example.model.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * LazyFactory per la gestione dei Clienti.
 * Implementa il pattern Lazy Initialization con caching locale.
 */
public class ClienteLazyFactory {
    private static ClienteLazyFactory instance;
    private final List<Cliente> clients;

    private ClienteLazyFactory() {
        clients = new ArrayList<>();
    }

    public static synchronized ClienteLazyFactory getInstance() {
        if (instance == null) {
            instance = new ClienteLazyFactory();
        }
        return instance;
    }

    /**
     * Gets a Cliente by its User.
     * Cerca prima nella cache, poi nel database.
     */
    public Cliente getClienteByUser(User user) throws DAOException, UserNotFoundException, UnrecognizedRoleException,
            ObjectNotFoundException, MissingAuthorizationException, WrongListQueryIdentifierValue {
        for (Cliente s : clients) {
            if (s.getUser().equals(user)) {
                return s;
            }
        }
        try {
            Cliente daoCliente = DAOFactoryAbstract.getInstance().getClienteDAO().getClienteByUser(user);
            clients.add(daoCliente);
            return daoCliente;
        } catch (PropertyException | ResourceNotFoundException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }
}
