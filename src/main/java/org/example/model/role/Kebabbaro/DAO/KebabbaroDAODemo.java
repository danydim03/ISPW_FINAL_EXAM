// java
package org.example.model.role.Kebabbaro.DAO;

import org.example.exceptions.DAOException;
import org.example.model.role.Kebabbaro.Kebabbaro;
import org.example.model.user.User;

public class KebabbaroDAODemo implements KebabbaroDAOInterface {

    @Override
    public Kebabbaro getKebabbaroByUser(User user) throws DAOException {
        // Return a mock Kebabbaro wrapping the user
        // Constructor: Kebabbaro(User user, List<String> signatureDishes, int
        // maxOrdersPerHour)
        return new Kebabbaro(user, new java.util.ArrayList<>(), 50);
    }

    @Override
    public void insert(Kebabbaro kebabbaro) {
        throw new UnsupportedOperationException("Insert not supported in demo mode");
    }

    @Override
    public void delete(Kebabbaro kebabbaro) {
        throw new UnsupportedOperationException("Delete not supported in demo mode");
    }

    @Override
    public void update(Kebabbaro kebabbaro) {
        throw new UnsupportedOperationException("Update not supported in demo mode");
    }
}