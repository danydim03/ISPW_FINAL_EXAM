package org.example.model.role.Amministratore.DAO;

import org.example.exceptions.*;
import org.example.instances_management_abstracts.DAODBAbstract;
import org.example.model.role.Amministratore.Amministratore;
import org.example.model.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AmministratoreDAODB extends DAODBAbstract<Amministratore> implements AmministratoreDAOInterface {
    private static final String AMMINISTRATORE = "AMMINISTRATORE";
    private static final String CODICE = "ID";
    private static final String DEPARTMENT = "department";
    private static final String ACCESS_LEVEL = "access_level";

    protected static AmministratoreDAOInterface instance;

    private AmministratoreDAODB() {
    }

    public static synchronized AmministratoreDAOInterface getInstance() {
        if (instance == null) {
            instance = new AmministratoreDAODB();
        }
        return instance;
    }

    @Override
    public Amministratore getAmministratoreByUser(User user) throws DAOException, UserNotFoundException,
            PropertyException, ResourceNotFoundException, UnrecognizedRoleException, ObjectNotFoundException,
            MissingAuthorizationException, WrongListQueryIdentifierValue {
        return getQuery(
                AMMINISTRATORE,
                List.of(CODICE),
                List.of(user.getId()),
                List.of(user));
    }

    @Override
    public void insert(Amministratore amministratore)
            throws DAOException, PropertyException, ResourceNotFoundException, MissingAuthorizationException {
        insertQuery(
                AMMINISTRATORE,
                List.of(
                        amministratore.getUser().getCodiceFiscale(),
                        amministratore.getDepartment(),
                        amministratore.getAccessLevel()));
    }

    @Override
    public void delete(Amministratore amministratore)
            throws DAOException, PropertyException, ResourceNotFoundException {
        deleteQuery(
                AMMINISTRATORE,
                List.of(CODICE),
                List.of(amministratore.getUser().getCodiceFiscale()));
    }

    @Override
    public void update(Amministratore amministratore)
            throws PropertyException, ResourceNotFoundException, DAOException, MissingAuthorizationException {
        updateQuery(
                AMMINISTRATORE,
                List.of(DEPARTMENT, ACCESS_LEVEL),
                List.of(amministratore.getDepartment(), amministratore.getAccessLevel()),
                List.of(CODICE),
                List.of(amministratore.getUser().getCodiceFiscale()));
    }

    @Override
    protected Amministratore queryObjectBuilder(ResultSet rs, List<Object> objects) throws SQLException, DAOException,
            PropertyException, ResourceNotFoundException, UserNotFoundException, UnrecognizedRoleException,
            MissingAuthorizationException, WrongListQueryIdentifierValue, ObjectNotFoundException {
        String department = rs.getString(DEPARTMENT);
        int accessLevel = rs.getInt(ACCESS_LEVEL);
        return new Amministratore((User) objects.get(0), department, accessLevel);
    }

    @Override
    protected String setGetListQueryIdentifiersValue(Amministratore amministratore, int valueNumber)
            throws DAOException {
        return null;
    }
}
