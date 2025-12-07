package org.example.model.role.Kebabbaro;

import org.example.exceptions.*;
import org.example.instances_management_abstracts.DAODBAbstract;
import org.example.model.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class KebabbaroDAODB extends DAODBAbstract<Kebabbaro> implements KebabbaroDAOInterface {
    private static final String KEBABBARO = "KEBABBARO";
    private static final String CODICE = "ID";

    protected static KebabbaroDAOInterface instance;

    private KebabbaroDAODB() {
    }

    public static synchronized KebabbaroDAOInterface getInstance() {
        if (instance == null) {
            instance = new KebabbaroDAODB();
        }
        return instance;
    }

    @Override
    public Kebabbaro getKebabbaroByUser(User user) throws DAOException, UserNotFoundException, PropertyException, ResourceNotFoundException, UnrecognizedRoleException, ObjectNotFoundException, MissingAuthorizationException, WrongListQueryIdentifierValue {
        return getQuery(
                KEBABBARO,
                List.of(CODICE),
                List.of(user.getID()),
                List.of(user)
        );
    }

    @Override
    public void insert(Kebabbaro kebabbaro) throws DAOException, PropertyException, ResourceNotFoundException, MissingAuthorizationException {
        insertQuery(
                KEBABBARO,
                List.of(kebabbaro.getUser().getID())
        );
    }

    @Override
    public void delete(Kebabbaro kebabbaro) throws DAOException, PropertyException, ResourceNotFoundException {
        deleteQuery(
                KEBABBARO,
                List.of(CODICE),
                List.of(kebabbaro.getUser().getID())
        );
    }

    @Override
    public void update(Kebabbaro kebabbaro) throws PropertyException, ResourceNotFoundException, DAOException, MissingAuthorizationException {
        updateQuery(
                KEBABBARO,
                List.of(),
                List.of(),
                List.of(CODICE),
                List.of(kebabbaro.getUser().getID())
        );
    }

    @Override
    protected Kebabbaro queryObjectBuilder(ResultSet rs, List<Object> objects) throws SQLException, DAOException, PropertyException, ResourceNotFoundException, UserNotFoundException, UnrecognizedRoleException, MissingAuthorizationException, WrongListQueryIdentifierValue, ObjectNotFoundException {
        return new Kebabbaro((User) objects.get(0), new ArrayList<>(), 10);
    }

    @Override
    protected String setGetListQueryIdentifiersValue(Kebabbaro kebabbaro, int valueNumber) throws DAOException {
        return null;
    }
}