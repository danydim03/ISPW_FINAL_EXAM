package org.example.model.role.Cliente.DAO;

import org.example.exceptions.*;
import org.example.instances_management_abstracts.DAODBAbstract;
import org.example.model.role.Cliente.Cliente;
import org.example.model.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ClientiDAODB extends DAODBAbstract<Cliente> implements ClienteDAOInterface {
    private static final String CLIENTE = "CLIENTE";
    private static final String CODICE = "ID";
    private static final String PUNTEGGIO = "punteggio";

    protected static ClienteDAOInterface instance;

    private ClientiDAODB() {
    }

    public static synchronized ClienteDAOInterface getInstance() {
        if (instance == null) {
            instance = new ClientiDAODB();
        }
        return instance;
    }

    /**
     * Returns the Cliente object correlated to a given User
     *
     * @param user the User whose Cliente's data have to be retrieved
     * @return a Cliente object
     * @throws DAOException          thrown if errors occur while retrieving data
     *                               from persistence layer
     * @throws UserNotFoundException thrown if the given User has not a Cliente role
     */
    @Override
    public Cliente getClienteByUser(User user) throws DAOException, UserNotFoundException, PropertyException,
            ResourceNotFoundException, UnrecognizedRoleException, ObjectNotFoundException,
            MissingAuthorizationException, WrongListQueryIdentifierValue {
        return getQuery(
                CLIENTE,
                List.of(CODICE),
                List.of(user.getId()),
                List.of(user));
    }

    @Override
    public void insert(Cliente cliente)
            throws DAOException, PropertyException, ResourceNotFoundException, MissingAuthorizationException {
        insertQuery(
                CLIENTE,
                List.of(
                        cliente.getUser().getCodiceFiscale(),
                        cliente.getPunteggio()));
    }

    @Override
    public void delete(Cliente cliente) throws DAOException, PropertyException, ResourceNotFoundException {
        deleteQuery(
                CLIENTE,
                List.of(CODICE),
                List.of(cliente.getUser().getCodiceFiscale()));
    }

    @Override
    public void update(Cliente cliente)
            throws PropertyException, ResourceNotFoundException, DAOException, MissingAuthorizationException {
        updateQuery(
                CLIENTE,
                List.of(PUNTEGGIO),
                List.of(cliente.getPunteggio()),
                List.of(CODICE),
                List.of(cliente.getUser().getCodiceFiscale()));
    }

    @Override
    protected Cliente queryObjectBuilder(ResultSet rs, List<Object> objects) throws SQLException, DAOException,
            PropertyException, ResourceNotFoundException, UserNotFoundException, UnrecognizedRoleException,
            MissingAuthorizationException, WrongListQueryIdentifierValue, ObjectNotFoundException {
        Cliente cliente = new Cliente((User) objects.get(0), rs.getString(CODICE));
        // Carica il punteggio dal database se presente
        try {
            cliente.setPunteggio(rs.getInt(PUNTEGGIO));
        } catch (SQLException e) {
            // Il campo punteggio potrebbe non esistere, usa default 0
        }
        return cliente;
    }

    @Override
    protected String setGetListQueryIdentifiersValue(Cliente cliente, int valueNumber) throws DAOException {
        return null;
    }
}
