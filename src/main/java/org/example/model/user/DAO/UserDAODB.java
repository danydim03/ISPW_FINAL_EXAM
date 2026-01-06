
package org.example.model.user.DAO;

import org.example.enums.ExceptionMessagesEnum;
import org.example.enums.UserRoleEnum;
import org.example.exceptions.UserNotFoundException;
import org.example.exceptions.DAOException;
import org.example.exceptions.PropertyException;
import org.example.exceptions.ResourceNotFoundException;
import org.example.exceptions.UnrecognizedRoleException;
import org.example.exceptions.ObjectNotFoundException;
import org.example.exceptions.MissingAuthorizationException;
import org.example.exceptions.WrongListQueryIdentifierValue;
import org.example.instances_management_abstracts.DAODBAbstract;
import org.example.model.role.Amministratore.AmministratoreLazyFactory;
import org.example.model.role.Cliente.ClienteLazyFactory;
import org.example.model.role.Kebabbaro.KebabbaroLazyFactory;
import org.example.model.user.User;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAODB extends DAODBAbstract<User> implements UserDAOInterface {
    private static final String EMAIL = "email";
    private static final String CODICE_FISCALE = "codice_fiscale";
    protected static UserDAOInterface instance;

    private UserDAODB() {
        super();
    }

    public static synchronized UserDAOInterface getInstance() {
        if (instance == null) {
            instance = new UserDAODB();
        }
        return instance;
    }

    @Override
    public User getUserByEmail(String email) throws UserNotFoundException, DAOException, PropertyException,
            ResourceNotFoundException, UnrecognizedRoleException, ObjectNotFoundException,
            MissingAuthorizationException, WrongListQueryIdentifierValue {
        return getQuery(
                "USER",
                List.of(EMAIL),
                List.of(email),
                null);
    }

    @Override
    public User getUserByCodiceFiscale(String codiceFiscale) throws UserNotFoundException, DAOException,
            PropertyException, ResourceNotFoundException, UnrecognizedRoleException, ObjectNotFoundException,
            MissingAuthorizationException, WrongListQueryIdentifierValue {
        return getQuery(
                "USER",
                List.of(CODICE_FISCALE),
                List.of(codiceFiscale),
                null);
    }

    @Override
    protected User queryObjectBuilder(ResultSet rs, List<Object> objects) throws SQLException, DAOException,
            PropertyException, ResourceNotFoundException, UnrecognizedRoleException, UserNotFoundException,
            ObjectNotFoundException, MissingAuthorizationException, WrongListQueryIdentifierValue {
        User user = new User(
                rs.getString("name"),
                rs.getString("surname"),
                rs.getString(CODICE_FISCALE),
                rs.getString(EMAIL),
                rs.getString("password"),
                rs.getDate("registration_date").toLocalDate());
        // Imposta l'ID dal database (es. CLI001)
        user.setId(rs.getString("ID"));
        setUserRoleByRoleEnum(user, UserRoleEnum.getUserRoleByType(rs.getInt("role")));
        return user;
    }

    @Override
    protected String setGetListQueryIdentifiersValue(User user, int valueNumber) throws DAOException {
        return null;
    }

    @Override
    public void insert(User user)
            throws DAOException, PropertyException, ResourceNotFoundException, MissingAuthorizationException {
        insertQuery(
                "USER",
                List.of(user.getCodiceFiscale(), user.getName(), user.getSurname(), user.getPassword(),
                        Date.valueOf(user.getRegistrationDate()), user.getEmail(),
                        user.getRole().getRoleEnumType().type));
    }

    @Override
    public void delete(User user) throws DAOException, PropertyException, ResourceNotFoundException {
        deleteQuery(
                "USER",
                List.of(CODICE_FISCALE),
                List.of(user.getCodiceFiscale()));
    }

    @Override
    public void update(User user)
            throws PropertyException, ResourceNotFoundException, DAOException, MissingAuthorizationException {
        updateQuery(
                "USER",
                List.of("name", "surname", "password", "registration_date", EMAIL, "role"),
                List.of(user.getName(), user.getSurname(), user.getPassword(), Date.valueOf(user.getRegistrationDate()),
                        user.getEmail(), user.getRole().getRoleEnumType().type),
                List.of(CODICE_FISCALE),
                List.of(user.getCodiceFiscale()));
    }

    /**
     * Set the user role by a given role value
     *
     * @param user the User whose role has to be set
     * @param role the role to be set ot the User
     * @throws UnrecognizedRoleException thrown if the role value cannot be cast to
     *                                   any enum value
     * @throws DAOException              thrown if errors occur while retrieving
     *                                   data from persistence layer
     * @throws UserNotFoundException     thrown if the given User cannot be found
     */
    protected void setUserRoleByRoleEnum(User user, UserRoleEnum role)
            throws UnrecognizedRoleException, DAOException, UserNotFoundException, ObjectNotFoundException,
            MissingAuthorizationException, WrongListQueryIdentifierValue {
        switch (role) {
            case CLIENTE -> user.setRole(ClienteLazyFactory.getInstance().getClienteByUser(user));
            case KEBABBARO -> user.setRole(KebabbaroLazyFactory.getInstance().getKebabbaroByUser(user));
            case AMMINISTRATORE -> user.setRole(AmministratoreLazyFactory.getInstance().getAmministratoreByUser(user));
            default -> throw new UnrecognizedRoleException(ExceptionMessagesEnum.UNRECOGNIZED_ROLE.message);
        }
    }
}
