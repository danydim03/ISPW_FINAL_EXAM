package org.example.model.role.Amministratore.DAO;

import org.example.csv.CSVFileManager;
import org.example.enums.ExceptionMessagesEnum;
import org.example.exceptions.*;
import org.example.model.role.Amministratore.Amministratore;
import org.example.model.user.User;

import java.util.List;

/**
 * DAO per Amministratore basato su File System (CSV).
 */
public class AmministratoreDAOFS implements AmministratoreDAOInterface {

    private static AmministratoreDAOFS instance;
    private final CSVFileManager csvManager;
    private static final String FILENAME = "amministratori";

    // CSV columns: id, user_id, department, access_level
    private static final String[] HEADER = { "id", "user_id", "department", "access_level" };

    private AmministratoreDAOFS() {
        this.csvManager = CSVFileManager.getInstance();
        initializeFile();
    }

    public static synchronized AmministratoreDAOFS getInstance() {
        if (instance == null) {
            instance = new AmministratoreDAOFS();
        }
        return instance;
    }

    private void initializeFile() {
        try {
            csvManager.createFileWithHeader(FILENAME, HEADER);
        } catch (DAOException e) {
            System.err.println("Warning: Could not initialize amministratori.csv: " + e.getMessage());
        }
    }

    @Override
    public Amministratore getAmministratoreByUser(User user) throws DAOException {
        try {
            List<String[]> allAmministratori = csvManager.readAllWithoutHeader(FILENAME);
            for (String[] row : allAmministratori) {
                if (row.length >= 2 && row[1].equalsIgnoreCase(user.getId())) {
                    return buildAmministratoreFromRow(row, user);
                }
            }
            // If not found, create a new Amministratore for this user
            return new Amministratore(user);
        } catch (DAOException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    @Override
    public void insert(Amministratore amministratore) throws DAOException {
        String[] row = buildRowFromAmministratore(amministratore);
        csvManager.appendLine(FILENAME, row);
    }

    @Override
    public void delete(Amministratore amministratore) throws DAOException {
        try {
            List<String[]> allLines = csvManager.readAll(FILENAME);

            for (int i = 1; i < allLines.size(); i++) { // Skip header
                String[] row = allLines.get(i);
                if (row.length >= 2 && row[1].equalsIgnoreCase(amministratore.getUser().getId())) {
                    csvManager.deleteLine(FILENAME, i);
                    return;
                }
            }
        } catch (DAOException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    @Override
    public void update(Amministratore amministratore) throws DAOException {
        try {
            List<String[]> allLines = csvManager.readAll(FILENAME);

            for (int i = 1; i < allLines.size(); i++) { // Skip header
                String[] row = allLines.get(i);
                if (row.length >= 2 && row[1].equalsIgnoreCase(amministratore.getUser().getId())) {
                    csvManager.updateLine(FILENAME, i, buildRowFromAmministratore(amministratore));
                    return;
                }
            }
        } catch (DAOException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    private Amministratore buildAmministratoreFromRow(String[] row, User user) {
        String department = row.length > 2 ? row[2] : "Generale";
        int accessLevel = row.length > 3 ? Integer.parseInt(row[3]) : 3;

        return new Amministratore(user, department, accessLevel);
    }

    private String[] buildRowFromAmministratore(Amministratore amministratore) {
        return new String[] {
                amministratore.getUser() != null ? amministratore.getUser().getId() : "",
                amministratore.getUser() != null ? amministratore.getUser().getId() : "",
                amministratore.getDepartment(),
                String.valueOf(amministratore.getAccessLevel())
        };
    }
}
