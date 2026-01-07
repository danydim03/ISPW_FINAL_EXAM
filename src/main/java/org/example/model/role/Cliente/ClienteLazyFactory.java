package org.example.model.role.Cliente;

import org.example.dao_manager.DAOFactoryAbstract;
import org.example.enums.ExceptionMessagesEnum;
import org.example.exceptions.*;
import org.example.model.user.User;

import java.util.ArrayList;
import java.util.List;

public class ClienteLazyFactory {
    private static ClienteLazyFactory instance;
    private final List<Cliente> Clients;

    private ClienteLazyFactory(){
        Clients = new ArrayList<Cliente>();
    }

    public static synchronized ClienteLazyFactory getInstance(){
        if (instance == null){
            instance = new ClienteLazyFactory();
        }
        return instance;
    }

    public Cliente getClienteByUser(User user) throws DAOException, UserNotFoundException, UnrecognizedRoleException, ObjectNotFoundException, MissingAuthorizationException,  WrongListQueryIdentifierValue {
        for (Cliente s : Clients) {
            if (s.getUser().equals(user)) {
                return s;
            }
        }
        try {
            Cliente daoCliente = DAOFactoryAbstract.getInstance().getClienteDAO().getClienteByUser(user);
            Clients.add(daoCliente);
            return daoCliente;
        } catch (PropertyException | ResourceNotFoundException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    public Cliente newCliente(User user, String ID) throws DAOException, MissingAuthorizationException {
        Cliente student = new Cliente(user, ID);
        user.setRole(student);
        try {
            DAOFactoryAbstract.getInstance().getClienteDAO().insert(student);
        } catch (PropertyException | ResourceNotFoundException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
        Clients.add(student);
        return student;
    }
}
