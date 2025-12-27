package org.example.model.role.Kebabbaro.DAO;

import org.example.csv.CSVFileManager;
import org.example.enums.ExceptionMessagesEnum;
import org.example.exceptions.*;
import org.example.model.role.Kebabbaro.Kebabbaro;
import org.example.model.user.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DAO per Kebabbaro basato su File System (CSV).
 */
public class KebabbaroDAOFS implements KebabbaroDAOInterface {

    private static KebabbaroDAOFS instance;
    private final CSVFileManager csvManager;
    private static final String FILENAME = "kebabbari";

    // CSV columns: id, user_id, signature_dishes, max_orders_per_hour
    private static final String[] HEADER = { "id", "user_id", "signature_dishes", "max_orders_per_hour" };

    private KebabbaroDAOFS() {
        this.csvManager = CSVFileManager.getInstance();
        initializeFile();
    }

    public static synchronized KebabbaroDAOFS getInstance() {
        if (instance == null) {
            instance = new KebabbaroDAOFS();
        }
        return instance;
    }

    private void initializeFile() {
        try {
            csvManager.createFileWithHeader(FILENAME, HEADER);
        } catch (DAOException e) {
            System.err.println("Warning: Could not initialize kebabbari.csv: " + e.getMessage());
        }
    }

    @Override
    public Kebabbaro getKebabbaroByUser(User user) throws DAOException {
        try {
            List<String[]> allKebabbari = csvManager.readAllWithoutHeader(FILENAME);
            for (String[] row : allKebabbari) {
                if (row.length >= 2 && row[1].equalsIgnoreCase(user.getId())) {
                    return buildKebabbaroFromRow(row, user);
                }
            }
            // If not found, create a new Kebabbaro for this user
            return new Kebabbaro(user, new ArrayList<>(), 50);
        } catch (DAOException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    @Override
    public void insert(Kebabbaro kebabbaro) throws DAOException {
        String[] row = buildRowFromKebabbaro(kebabbaro);
        csvManager.appendLine(FILENAME, row);
    }

    @Override
    public void delete(Kebabbaro kebabbaro) throws DAOException {
        try {
            List<String[]> allLines = csvManager.readAll(FILENAME);

            for (int i = 1; i < allLines.size(); i++) { // Skip header
                String[] row = allLines.get(i);
                if (row.length >= 2 && row[1].equalsIgnoreCase(kebabbaro.getUser().getId())) {
                    csvManager.deleteLine(FILENAME, i);
                    return;
                }
            }
        } catch (DAOException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    @Override
    public void update(Kebabbaro kebabbaro) throws DAOException {
        try {
            List<String[]> allLines = csvManager.readAll(FILENAME);

            for (int i = 1; i < allLines.size(); i++) { // Skip header
                String[] row = allLines.get(i);
                if (row.length >= 2 && row[1].equalsIgnoreCase(kebabbaro.getUser().getId())) {
                    csvManager.updateLine(FILENAME, i, buildRowFromKebabbaro(kebabbaro));
                    return;
                }
            }
        } catch (DAOException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    private Kebabbaro buildKebabbaroFromRow(String[] row, User user) {
        List<String> signatureDishes = new ArrayList<>();
        if (row.length > 2 && !row[2].isEmpty()) {
            signatureDishes = new ArrayList<>(Arrays.asList(row[2].split(";")));
        }

        int maxOrdersPerHour = row.length > 3 ? Integer.parseInt(row[3]) : 50;

        return new Kebabbaro(user, signatureDishes, maxOrdersPerHour);
    }

    private String[] buildRowFromKebabbaro(Kebabbaro kebabbaro) {
        String dishesStr = String.join(";", kebabbaro.getSignatureDishes());

        return new String[] {
                kebabbaro.getUser() != null ? kebabbaro.getUser().getId() : "",
                kebabbaro.getUser() != null ? kebabbaro.getUser().getId() : "",
                dishesStr,
                String.valueOf(kebabbaro.getMaxOrdersPerHour())
        };
    }
}
