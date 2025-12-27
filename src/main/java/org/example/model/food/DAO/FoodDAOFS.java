package org.example.model.food.DAO;

import org.example.csv.CSVFileManager;
import org.example.enums.ExceptionMessagesEnum;
import org.example.exceptions.*;
import org.example.model.food.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DAO per Food basato su File System (CSV).
 * Nota: Il catalogo Food è principalmente read-only poiché i prodotti
 * sono definiti come classi concrete (PaninoDonerKebab, etc.)
 */
public class FoodDAOFS implements FoodDAOInterface {

    private static FoodDAOFS instance;
    private final CSVFileManager csvManager;
    private static final String FILENAME = "food";

    // CSV columns: id, descrizione, tipo, costo, durata, classe_java
    private static final String[] HEADER = { "id", "descrizione", "tipo", "costo", "durata", "classe_java" };

    private FoodDAOFS() {
        this.csvManager = CSVFileManager.getInstance();
        initializeFile();
    }

    public static synchronized FoodDAOFS getInstance() {
        if (instance == null) {
            instance = new FoodDAOFS();
        }
        return instance;
    }

    private void initializeFile() {
        try {
            if (!csvManager.fileExists(FILENAME)) {
                csvManager.createFileWithHeader(FILENAME, HEADER);
                // Initialize with default food items
                initializeDefaultFood();
            }
        } catch (DAOException e) {
            System.err.println("Warning: Could not initialize food.csv: " + e.getMessage());
        }
    }

    private void initializeDefaultFood() {
        try {
            // Add base foods
            csvManager.appendLine(FILENAME,
                    new String[] { "1", "Panino Doner Kebab", "BASE", "5.50", "5", "PaninoDonerKebab" });
            csvManager.appendLine(FILENAME,
                    new String[] { "2", "Piadina Doner Kebab", "BASE", "6.00", "6", "PiadinaDonerKebab" });
            csvManager.appendLine(FILENAME,
                    new String[] { "3", "Kebab al Piatto", "BASE", "7.50", "8", "KebabAlPiatto" });

            // Add add-ons
            csvManager.appendLine(FILENAME, new String[] { "4", "Cipolla", "ADDON", "0.50", "0", "Cipolla" });
            csvManager.appendLine(FILENAME, new String[] { "5", "Patatine", "ADDON", "1.50", "3", "Patatine" });
            csvManager.appendLine(FILENAME, new String[] { "6", "Salsa Yogurt", "ADDON", "0.50", "0", "SalsaYogurt" });
            csvManager.appendLine(FILENAME,
                    new String[] { "7", "Mix Verdure Grigliate", "ADDON", "1.00", "2", "MixVerdureGrigliate" });
        } catch (DAOException e) {
            System.err.println("Warning: Could not initialize default food: " + e.getMessage());
        }
    }

    @Override
    public Food getFoodById(Long id) throws DAOException, ObjectNotFoundException {
        try {
            List<String[]> allFood = csvManager.readAllWithoutHeader(FILENAME);
            for (String[] row : allFood) {
                if (row.length >= 1 && Long.parseLong(row[0]) == id) {
                    return buildFoodFromRow(row);
                }
            }
            throw new ObjectNotFoundException(ExceptionMessagesEnum.OBJ_NOT_FOUND.message);
        } catch (DAOException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    @Override
    public List<Food> getAllFoodBase() throws DAOException {
        try {
            List<String[]> allFood = csvManager.readAllWithoutHeader(FILENAME);
            return allFood.stream()
                    .filter(row -> row.length > 2 && "BASE".equalsIgnoreCase(row[2]))
                    .map(this::buildFoodFromRow)
                    .collect(Collectors.toList());
        } catch (DAOException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    @Override
    public List<Food> getAllAddOn() throws DAOException {
        try {
            List<String[]> allFood = csvManager.readAllWithoutHeader(FILENAME);
            return allFood.stream()
                    .filter(row -> row.length > 2 && "ADDON".equalsIgnoreCase(row[2]))
                    .map(this::buildFoodFromRow)
                    .collect(Collectors.toList());
        } catch (DAOException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    @Override
    public void insert(Food food) throws DAOException {
        if (food.getId() == null) {
            food.setId(getNextId());
        }
        String[] row = buildRowFromFood(food);
        csvManager.appendLine(FILENAME, row);
    }

    @Override
    public void delete(Food food) throws DAOException {
        try {
            List<String[]> allLines = csvManager.readAll(FILENAME);

            for (int i = 1; i < allLines.size(); i++) { // Skip header
                String[] row = allLines.get(i);
                if (row.length >= 1 && Long.parseLong(row[0]) == food.getId()) {
                    csvManager.deleteLine(FILENAME, i);
                    return;
                }
            }
        } catch (DAOException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    @Override
    public void update(Food food) throws DAOException {
        try {
            List<String[]> allLines = csvManager.readAll(FILENAME);

            for (int i = 1; i < allLines.size(); i++) { // Skip header
                String[] row = allLines.get(i);
                if (row.length >= 1 && Long.parseLong(row[0]) == food.getId()) {
                    csvManager.updateLine(FILENAME, i, buildRowFromFood(food));
                    return;
                }
            }
        } catch (DAOException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    private long getNextId() throws DAOException {
        List<String[]> allFood = csvManager.readAllWithoutHeader(FILENAME);
        long maxId = 0;
        for (String[] row : allFood) {
            if (row.length >= 1 && !row[0].isEmpty()) {
                long id = Long.parseLong(row[0]);
                if (id > maxId) {
                    maxId = id;
                }
            }
        }
        return maxId + 1;
    }

    /**
     * Builds a Food object from CSV row.
     * Uses FoodLazyFactory pattern to create concrete instances.
     */
    private Food buildFoodFromRow(String[] row) {
        Long id = Long.parseLong(row[0]);
        String descrizione = row[1];
        String tipo = row[2];
        String classeJava = row.length > 5 ? row[5] : "";

        // Try to create specific class based on className
        Food food = createFoodByClassName(classeJava, id);
        if (food != null) {
            return food;
        }

        // Fallback: create generic Food based on type
        if ("BASE".equalsIgnoreCase(tipo)) {
            return new PaninoDonerKebab(id);
        } else {
            // For add-ons, we can't create them without a base food
            // Return info-only instance
            return new PaninoDonerKebab(id);
        }
    }

    private Food createFoodByClassName(String className, Long id) {
        return switch (className) {
            case "PaninoDonerKebab" -> new PaninoDonerKebab(id);
            case "PiadinaDonerKebab" -> new PiadinaDonerKebab(id);
            case "KebabAlPiatto" -> new KebabAlPiatto(id);
            default -> null;
        };
    }

    private String[] buildRowFromFood(Food food) {
        return new String[] {
                String.valueOf(food.getId()),
                food.getDescrizione(),
                food.getTipo(),
                String.valueOf(food.getCosto()),
                String.valueOf(food.getDurata()),
                food.getClass().getSimpleName()
        };
    }
}
