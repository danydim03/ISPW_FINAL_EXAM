
package org.example.model.user;

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

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAODB extends DAODBAbstract<User> implements UserDAOInterface {
    private static final Logger LOGGER = Logger.getLogger(UserDAODB.class.getName());

    private static final String EMAIL = "email";
    private static final String ID = "ID";
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

    // Metodo helper per testare l'esistenza tramite email
    public boolean existsUserByEmailSafe(String email) {
        try {
            User u = getUserByEmail(email);
            if (u != null) {
                LOGGER.info("Utente trovato: " + u.getID() + " (" + email + ")");
                return true;
            } else {
                LOGGER.info("getUserByEmail ha restituito null per: " + email);
                return false;
            }
        } catch (UserNotFoundException e) {
            LOGGER.info("Utente non trovato: " + email);
            return false;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Errore eseguendo getUserByEmail per: " + email, e);
            return false;
        }
    }

    @Override
    public User getUserByEmail(String email) throws UserNotFoundException, DAOException, PropertyException, ResourceNotFoundException, UnrecognizedRoleException, ObjectNotFoundException, MissingAuthorizationException, WrongListQueryIdentifierValue {
        return getQuery(
                "USER",
                List.of(EMAIL),
                List.of(email),
                null
        );
    }

    @Override
    public User getUserByID(String ID) throws UserNotFoundException, DAOException, PropertyException, ResourceNotFoundException, UnrecognizedRoleException, ObjectNotFoundException, MissingAuthorizationException, WrongListQueryIdentifierValue {
        return getQuery(
                "USER",
                List.of(ID),
                List.of(ID),
                null
        );
    }

    @Override
    protected User queryObjectBuilder(ResultSet rs, List<Object> objects) throws SQLException, DAOException, PropertyException, ResourceNotFoundException, UnrecognizedRoleException, UserNotFoundException, ObjectNotFoundException, MissingAuthorizationException, WrongListQueryIdentifierValue {
        User user = new User(
                rs.getString("ID"),
                rs.getString("name"),
                rs.getString("surname"),
                rs.getString("codice_fiscale"),
                rs.getString("email"),
                rs.getString("password"), // usa il nome colonna reale
                rs.getDate("registration_date").toLocalDate()
        );

        int roleValue = rs.getInt("role");
        if (rs.wasNull()) {
            throw new UnrecognizedRoleException(ExceptionMessagesEnum.UNRECOGNIZED_ROLE.message + " (role column is NULL)");
        }

        UserRoleEnum roleEnum = UserRoleEnum.getUserRoleByType(roleValue);
        if (roleEnum == null) {
            throw new UnrecognizedRoleException(ExceptionMessagesEnum.UNRECOGNIZED_ROLE.message + " (role=" + roleValue + ")");
        }

        setUserRoleByRoleEnum(user, roleEnum);
        return user;
    }

    @Override
    protected String setGetListQueryIdentifiersValue(User user, int valueNumber) throws DAOException {
        return null;
    }

    @Override
    public void insert(User user) throws DAOException, PropertyException, ResourceNotFoundException, MissingAuthorizationException {
        insertQuery(
                "USER",
                List.of(user.getID(), user.getName(), user.getSurname(), user.getPassword(), Date.valueOf(user.getRegistrationDate()), user.getEmail(), user.getRole().getRoleEnumType().type)
        );
    }

    @Override
    public void delete(User user) throws DAOException, PropertyException, ResourceNotFoundException {
        deleteQuery(
                "USER",
                List.of(ID),
                List.of(user.getID())
        );
    }

    @Override
    public void update(User user) throws PropertyException, ResourceNotFoundException, DAOException, MissingAuthorizationException {
        updateQuery(
                "USER",
                List.of("name", "surname", "password", "registration_date", EMAIL, "role"),
                List.of(user.getName(), user.getSurname(), user.getPassword(), Date.valueOf(user.getRegistrationDate()), user.getEmail(), user.getRole().getRoleEnumType().type),
                List.of(ID),
                List.of(user.getID()));
    }

    /**
     * Set the user role by a given role value
     *
     * @param user the User whose role has to be set
     * @param role the role to be set ot the User
     * @throws UnrecognizedRoleException thrown if the role value cannot be cast to any enum value
     * @throws DAOException              thrown if errors occur while retrieving data from persistence layer
     * @throws UserNotFoundException     thrown if the given User cannot be found
     */
    protected void setUserRoleByRoleEnum(User user, UserRoleEnum role) throws UnrecognizedRoleException, DAOException, UserNotFoundException, ObjectNotFoundException, MissingAuthorizationException, WrongListQueryIdentifierValue {
        switch (role) {
            case CLIENTE -> user.setRole(ClienteLazyFactory.getInstance().getClienteByUser(user));
            case KEBABBARO -> user.setRole(KebabbaroLazyFactory.getInstance().getKebabbaroByUser(user));
            case AMMINISTRATORE -> user.setRole(AmministratoreLazyFactory.getInstance().getAmministratoreByUser(user));
            default -> throw new UnrecognizedRoleException(ExceptionMessagesEnum.UNRECOGNIZED_ROLE.message);
        }
    }
}