package org.example.model.role.Cliente;

import org.example.exceptions.DAOException;
import org.example.model.role.Cliente.ClienteDAOInterface;
import org.example.model.user.User;

public class ClienteDAODemo implements ClienteDAOInterface {

    @Override
    public Cliente getClienteByUser(User user) throws DAOException {
        // Return a mock Cliente wrapping the user
        return new Cliente(user, "CLI-" + user.getCodiceFiscale());
    }

    @Override
    public void insert(Cliente cliente) {
    }

    @Override
    public void delete(Cliente cliente) {
    }

    @Override
    public void update(Cliente cliente) {
    }
}
