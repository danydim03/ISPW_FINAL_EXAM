package org.example.model.role.Cliente;

import org.example.dao_manager.DAOFactoryAbstract;
import org.example.enums.ExceptionMessagesEnum;
import org.example.exceptions.*;
import org.example.model.user.User;

import java.util.ArrayList;
import java.util.List;

public class ClienteLazyFactory {
    private static ClienteLazyFactory instance;
    private final List<Cliente> clients;

    private ClienteLazyFactory() {
        clients = new ArrayList<Cliente>();
    }

    public static synchronized ClienteLazyFactory getInstance() {
        if (instance == null) {
            instance = new ClienteLazyFactory();
        }
        return instance;
    }

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

    public Cliente newCliente(User user, String id) throws DAOException, MissingAuthorizationException {
        Cliente student = new Cliente(user, id);
        user.setRole(student);
        try {
            DAOFactoryAbstract.getInstance().getClienteDAO().insert(student);
        } catch (PropertyException | ResourceNotFoundException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
        clients.add(student);
        return student;
    }
}
