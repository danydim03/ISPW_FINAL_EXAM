package org.example.model.role.Amministratore.DAO;

import org.example.exceptions.DAOException;
import org.example.model.role.Amministratore.Amministratore;
import org.example.model.user.User;

public class AmministratoreDAODemo implements AmministratoreDAOInterface {

    @Override
    public Amministratore getAmministratoreByUser(User user) throws DAOException {
        // Return a mock Amministratore wrapping the user
        // Using valid constructor: Amministratore(User user)
        return new Amministratore(user);
    }

    @Override
    public void insert(Amministratore amministratore) {
    }

    @Override
    public void delete(Amministratore amministratore) {
    }

    @Override
    public void update(Amministratore amministratore) {
    }
}
