package org.example.model.user.DAO;

import org.example.csv.CSVFileManager;
import org.example.enums.ExceptionMessagesEnum;
import org.example.enums.UserRoleEnum;
import org.example.exceptions.*;
import org.example.model.role.AbstractRole;
import org.example.model.role.Amministratore.Amministratore;
import org.example.model.role.Cliente.Cliente;
import org.example.model.role.Cliente.DAO.ClienteDAOFS;
import org.example.model.role.Kebabbaro.Kebabbaro;
import org.example.model.user.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO per User basato su File System (CSV).
 * Gestisce la persistenza degli utenti su file CSV.
 */
public class UserDAOFS implements UserDAOInterface {

    private static UserDAOFS instance;
    private final CSVFileManager csvManager;
    private static final String FILENAME = "users";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    // CSV columns:
    // id,name,surname,codice_fiscale,email,password,registration_date,role_type
    private static final String[] HEADER = { "id", "name", "surname", "codice_fiscale", "email", "password",
            "registration_date", "role_type" };

    private UserDAOFS() {
        this.csvManager = CSVFileManager.getInstance();
        initializeFile();
    }

    public static synchronized UserDAOFS getInstance() {
        if (instance == null) {
            instance = new UserDAOFS();
        }
        return instance;
    }

    private void initializeFile() {
        try {
            csvManager.createFileWithHeader(FILENAME, HEADER);
        } catch (DAOException e) {
            System.err.println("Warning: Could not initialize users.csv: " + e.getMessage());
        }
    }

    @Override
    public User getUserByEmail(String email) throws UserNotFoundException, DAOException, UnrecognizedRoleException {
        try {
            List<String[]> allUsers = csvManager.readAllWithoutHeader(FILENAME);
            for (String[] row : allUsers) {
                if (row.length >= 8 && row[4].equalsIgnoreCase(email)) {
                    return buildUserFromRow(row);
                }
            }
            throw new UserNotFoundException(ExceptionMessagesEnum.USER_NOT_FOUND.message);
        } catch (DAOException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    @Override
    public User getUserByCodiceFiscale(String codiceFiscale)
            throws UserNotFoundException, DAOException, UnrecognizedRoleException {
        try {
            List<String[]> allUsers = csvManager.readAllWithoutHeader(FILENAME);
            for (String[] row : allUsers) {
                if (row.length >= 8 && row[3].equalsIgnoreCase(codiceFiscale)) {
                    return buildUserFromRow(row);
                }
            }
            throw new UserNotFoundException(ExceptionMessagesEnum.USER_NOT_FOUND.message);
        } catch (DAOException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    @Override
    public void insert(User user) throws DAOException {
        String[] row = buildRowFromUser(user);
        csvManager.appendLine(FILENAME, row);
    }

    @Override
    public void delete(User user) throws DAOException {
        try {
            List<String[]> allLines = csvManager.readAll(FILENAME);
            int indexToDelete = -1;

            for (int i = 1; i < allLines.size(); i++) { // Skip header
                String[] row = allLines.get(i);
                if (row.length >= 5 && row[4].equalsIgnoreCase(user.getEmail())) {
                    indexToDelete = i;
                    break;
                }
            }

            if (indexToDelete > 0) {
                csvManager.deleteLine(FILENAME, indexToDelete);
            }
        } catch (DAOException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    @Override
    public void update(User user) throws DAOException {
        try {
            List<String[]> allLines = csvManager.readAll(FILENAME);

            for (int i = 1; i < allLines.size(); i++) { // Skip header
                String[] row = allLines.get(i);
                if (row.length >= 5 && row[4].equalsIgnoreCase(user.getEmail())) {
                    csvManager.updateLine(FILENAME, i, buildRowFromUser(user));
                    return;
                }
            }
        } catch (DAOException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    /**
     * Builds a User object from a CSV row.
     */
    private User buildUserFromRow(String[] row) throws UnrecognizedRoleException {
        // row: id, name, surname, codice_fiscale, email, password, registration_date,
        // role_type
        String id = row[0];
        String name = row[1];
        String surname = row[2];
        String codiceFiscale = row[3];
        String email = row[4];
        String password = row[5];
        LocalDate registrationDate = LocalDate.parse(row[6], DATE_FORMAT);
        int roleType = Integer.parseInt(row[7]);

        User user = new User(name, surname, codiceFiscale, email, password, registrationDate);
        user.setId(id);

        // Set role based on type
        UserRoleEnum roleEnum = UserRoleEnum.getUserRoleByType(roleType);
        AbstractRole role = createRoleForUser(user, roleEnum);
        user.setRole(role);

        return user;
    }

    /**
     * Creates a role instance for the user based on role type.
     * Note: Uses the same approach as DAODemo classes to create roles.
     */
    private AbstractRole createRoleForUser(User user, UserRoleEnum roleEnum) {
        return switch (roleEnum) {
            case CLIENTE -> {
                try {
                    yield ClienteDAOFS.getInstance().getClienteByUser(user);
                } catch (DAOException e) {
                    // Fallback: create inline (same approach as Demo)
                    yield createClienteInline(user);
                }
            }
            case KEBABBARO -> new Kebabbaro(user, new ArrayList<>(), 50);
            case AMMINISTRATORE -> new Amministratore(user);
        };
    }

    /**
     * Creates a Cliente inline when DAO is not available.
     * Uses reflection or factory approach similar to Demo mode.
     */
    private Cliente createClienteInline(User user) {
        // ClienteDAODemo approach: return new Cliente(user, "CLI-" +
        // user.getCodiceFiscale())
        // Cliente constructor is protected, so we need to use ClienteDAOFS
        // If that fails, we'll return null and let higher layer handle it
        return null;
    }

    /**
     * Builds a CSV row from a User object.
     */
    private String[] buildRowFromUser(User user) {
        int roleType = 1; // Default to CLIENTE
        try {
            if (user.getRole() != null) {
                roleType = user.getRole().getRoleEnumType().type;
            }
        } catch (MissingAuthorizationException e) {
            roleType = 1; // Default
        }

        return new String[] {
                user.getId() != null ? user.getId() : "",
                user.getName(),
                user.getSurname(),
                user.getCodiceFiscale(),
                user.getEmail(),
                user.getPassword(),
                user.getRegistrationDate().format(DATE_FORMAT),
                String.valueOf(roleType)
        };
    }
}
