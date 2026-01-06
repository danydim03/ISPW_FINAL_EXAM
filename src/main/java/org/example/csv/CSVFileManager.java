package org.example.csv;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import org.example.enums.ExceptionMessagesEnum;
import org.example.exceptions.DAOException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Manager for CSV file operations with support for both read and write.
 * Provides thread-safe access to CSV files with automatic backup functionality.
 */
public class CSVFileManager {

    private static CSVFileManager instance;
    private final String csvDirectory;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    // Default directory for CSV files (relative to user home)
    private static final String DEFAULT_CSV_DIR = System.getProperty("user.home") + "/.habibi/data/";

    private CSVFileManager() {
        this.csvDirectory = DEFAULT_CSV_DIR;
        ensureDirectoryExists();
    }

    private CSVFileManager(String csvDirectory) {
        this.csvDirectory = csvDirectory;
        ensureDirectoryExists();
    }

    public static synchronized CSVFileManager getInstance() {
        if (instance == null) {
            instance = new CSVFileManager();
        }
        return instance;
    }

    public static synchronized CSVFileManager getInstance(String customDirectory) {
        if (instance == null) {
            instance = new CSVFileManager(customDirectory);
        }
        return instance;
    }

    /**
     * Ensures the CSV directory exists, creates it if not.
     */
    private void ensureDirectoryExists() {
        Path dir = Paths.get(csvDirectory);
        if (!Files.exists(dir)) {
            try {
                Files.createDirectories(dir);
            } catch (IOException e) {
                throw new IllegalStateException("Cannot create CSV directory: " + csvDirectory, e);
            }
        }
    }

    /**
     * Gets the full path to a CSV file.
     *
     * @param filename the filename (with or without .csv extension)
     * @return the full path
     */
    public String getFilePath(String filename) {
        if (!filename.endsWith(".csv")) {
            filename = filename + ".csv";
        }
        return csvDirectory + filename;
    }

    /**
     * Reads all lines from a CSV file.
     *
     * @param filename the CSV file name
     * @return list of string arrays, each representing a row
     * @throws DAOException if an error occurs during reading
     */
    public List<String[]> readAll(String filename) throws DAOException {
        lock.readLock().lock();
        try {
            String filePath = getFilePath(filename);
            File file = new File(filePath);

            if (!file.exists()) {
                return new ArrayList<>();
            }

            try (CSVReader reader = new CSVReader(new FileReader(file))) {
                return reader.readAll();
            } catch (IOException | CsvException e) {
                throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Reads all lines, skipping the header row.
     *
     * @param filename the CSV file name
     * @return list of string arrays (excluding header)
     * @throws DAOException if an error occurs during reading
     */
    public List<String[]> readAllWithoutHeader(String filename) throws DAOException {
        List<String[]> allLines = readAll(filename);
        if (!allLines.isEmpty()) {
            allLines.remove(0);
        }
        return allLines;
    }

    /**
     * Writes all lines to a CSV file, overwriting existing content.
     *
     * @param filename the CSV file name
     * @param data     the data to write
     * @throws DAOException if an error occurs during writing
     */
    public void writeAll(String filename, List<String[]> data) throws DAOException {
        lock.writeLock().lock();
        try {
            String filePath = getFilePath(filename);
            createBackup(filePath);

            try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
                writer.writeAll(data);
            } catch (IOException e) {
                throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Appends a single line to a CSV file.
     *
     * @param filename the CSV file name
     * @param line     the line to append
     * @throws DAOException if an error occurs during writing
     */
    public void appendLine(String filename, String[] line) throws DAOException {
        lock.writeLock().lock();
        try {
            String filePath = getFilePath(filename);

            try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, true))) {
                writer.writeNext(line);
            } catch (IOException e) {
                throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Updates a specific line in a CSV file.
     *
     * @param filename  the CSV file name
     * @param lineIndex the index of the line to update (0-based)
     * @param newLine   the new line content
     * @throws DAOException if an error occurs during the operation
     */
    public void updateLine(String filename, int lineIndex, String[] newLine) throws DAOException {
        lock.writeLock().lock();
        try {
            List<String[]> allLines = readAllInternal(filename);

            if (lineIndex >= 0 && lineIndex < allLines.size()) {
                allLines.set(lineIndex, newLine);
                writeAllInternal(filename, allLines);
            } else {
                throw new DAOException("Invalid line index: " + lineIndex);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Deletes a specific line from a CSV file.
     *
     * @param filename  the CSV file name
     * @param lineIndex the index of the line to delete (0-based)
     * @throws DAOException if an error occurs during the operation
     */
    public void deleteLine(String filename, int lineIndex) throws DAOException {
        lock.writeLock().lock();
        try {
            List<String[]> allLines = readAllInternal(filename);

            if (lineIndex >= 0 && lineIndex < allLines.size()) {
                allLines.remove(lineIndex);
                writeAllInternal(filename, allLines);
            } else {
                throw new DAOException("Invalid line index: " + lineIndex);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Creates the CSV file with header if it doesn't exist.
     *
     * @param filename the CSV file name
     * @param header   the header row
     * @throws DAOException if an error occurs
     */
    public void createFileWithHeader(String filename, String[] header) throws DAOException {
        lock.writeLock().lock();
        try {
            String filePath = getFilePath(filename);
            File file = new File(filePath);

            if (!file.exists()) {
                try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
                    writer.writeNext(header);
                } catch (IOException e) {
                    throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Checks if a file exists.
     *
     * @param filename the CSV file name
     * @return true if the file exists
     */
    public boolean fileExists(String filename) {
        return new File(getFilePath(filename)).exists();
    }

    /**
     * Creates a backup of the file before modifying it.
     *
     * @param filePath the file path to backup
     */
    private void createBackup(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                Path source = Paths.get(filePath);
                Path backup = Paths.get(filePath + ".bak");
                Files.copy(source, backup, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                // Backup failure should not block the operation
                System.err.println("Warning: Could not create backup for " + filePath);
            }
        }
    }

    /**
     * Internal read without acquiring lock (used when lock is already held).
     */
    private List<String[]> readAllInternal(String filename) throws DAOException {
        String filePath = getFilePath(filename);
        File file = new File(filePath);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            return new ArrayList<>(reader.readAll());
        } catch (IOException | CsvException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    /**
     * Internal write without acquiring lock (used when lock is already held).
     */
    private void writeAllInternal(String filename, List<String[]> data) throws DAOException {
        String filePath = getFilePath(filename);
        createBackup(filePath);

        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            writer.writeAll(data);
        } catch (IOException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    /**
     * Gets the CSV directory path.
     *
     * @return the directory path
     */
    public String getCsvDirectory() {
        return csvDirectory;
    }
}
