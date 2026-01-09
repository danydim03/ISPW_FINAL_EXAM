package org.example.model.ordine.DAO;

import org.example.csv.CSVFileManager;
import org.example.enums.ExceptionMessagesEnum;
import org.example.enums.StatoOrdine;
import org.example.exceptions.*;
import org.example.model.food.Food;
import org.example.model.ordine.Ordine;
import org.example.model.voucher.Voucher;
import org.example.model.voucher.DAO.VoucherDAOFS;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO per Ordine basato su File System (CSV).
 * Gestisce anche la tabella di relazione ordine_prodotti.
 */
public class OrdineDAOFS implements OrdineDAOInterface {

    private static final Logger logger = Logger.getLogger(OrdineDAOFS.class.getName());

    private static OrdineDAOFS instance;
    private final CSVFileManager csvManager;
    private static final String FILENAME = "ordini";
    private static final String PRODOTTI_FILENAME = "ordine_prodotti";
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // CSV columns for ordini: numero_ordine, cliente_id, data_creazione,
    // data_conferma, stato, totale, voucher_id
    private static final String[] HEADER = { "numero_ordine", "cliente_id", "data_creazione", "data_conferma", "stato",
            "totale", "voucher_id" };

    // CSV columns for ordine_prodotti: ordine_id, food_id
    private static final String[] PRODOTTI_HEADER = { "ordine_id", "food_id" };

    private OrdineDAOFS() {
        this.csvManager = CSVFileManager.getInstance();
        initializeFile();
    }

    public static synchronized OrdineDAOFS getInstance() {
        if (instance == null) {
            instance = new OrdineDAOFS();
        }
        return instance;
    }

    private void initializeFile() {
        try {
            csvManager.createFileWithHeader(FILENAME, HEADER);
            csvManager.createFileWithHeader(PRODOTTI_FILENAME, PRODOTTI_HEADER);
        } catch (DAOException e) {
            logger.log(Level.WARNING, "Could not initialize ordini.csv", e);
        }
    }

    @Override
    public Ordine getOrdineByNumero(Long numeroOrdine) throws DAOException, ObjectNotFoundException {
        try {
            List<String[]> allOrdini = csvManager.readAllWithoutHeader(FILENAME);
            for (String[] row : allOrdini) {
                if (row.length >= 1 && Long.parseLong(row[0]) == numeroOrdine) {
                    return buildOrdineFromRow(row);
                }
            }
            throw new ObjectNotFoundException(ExceptionMessagesEnum.OBJ_NOT_FOUND.message);
        } catch (DAOException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    @Override
    public List<Ordine> getOrdiniByCliente(String clienteId) throws DAOException {
        try {
            List<String[]> allOrdini = csvManager.readAllWithoutHeader(FILENAME);
            List<Ordine> result = new ArrayList<>();

            for (String[] row : allOrdini) {
                if (row.length >= 2 && row[1].equalsIgnoreCase(clienteId)) {
                    result.add(buildOrdineFromRow(row));
                }
            }
            return result;
        } catch (DAOException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    @Override
    public List<Ordine> getOrdiniByStato(StatoOrdine stato) throws DAOException {
        try {
            List<String[]> allOrdini = csvManager.readAllWithoutHeader(FILENAME);
            List<Ordine> result = new ArrayList<>();

            for (String[] row : allOrdini) {
                if (row.length >= 5 && row[4].equals(stato.name())) {
                    result.add(buildOrdineFromRow(row));
                }
            }
            return result;
        } catch (DAOException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    @Override
    public Long getNextNumeroOrdine() throws DAOException {
        List<String[]> allOrdini = csvManager.readAllWithoutHeader(FILENAME);
        long maxId = 0;
        for (String[] row : allOrdini) {
            if (row.length >= 1 && !row[0].isEmpty()) {
                long id = Long.parseLong(row[0]);
                if (id > maxId) {
                    maxId = id;
                }
            }
        }
        return maxId + 1;
    }

    @Override
    public void insert(Ordine ordine) throws DAOException {
        if (ordine.getNumeroOrdine() == null) {
            ordine.setNumeroOrdine(getNextNumeroOrdine());
        }
        String[] row = buildRowFromOrdine(ordine);
        csvManager.appendLine(FILENAME, row);

        // Save products relationship
        saveProdotti(ordine);
    }

    @Override
    public void delete(Ordine ordine) throws DAOException {
        try {
            List<String[]> allLines = csvManager.readAll(FILENAME);

            for (int i = 1; i < allLines.size(); i++) { // Skip header
                String[] row = allLines.get(i);
                if (row.length >= 1 && Long.parseLong(row[0]) == ordine.getNumeroOrdine()) {
                    csvManager.deleteLine(FILENAME, i);
                    break;
                }
            }

            // Delete products relationship
            deleteProdotti(ordine.getNumeroOrdine());
        } catch (DAOException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    @Override
    public void update(Ordine ordine) throws DAOException {
        try {
            List<String[]> allLines = csvManager.readAll(FILENAME);

            for (int i = 1; i < allLines.size(); i++) { // Skip header
                String[] row = allLines.get(i);
                if (row.length >= 1 && Long.parseLong(row[0]) == ordine.getNumeroOrdine()) {
                    csvManager.updateLine(FILENAME, i, buildRowFromOrdine(ordine));

                    // Update products: delete and re-add
                    deleteProdotti(ordine.getNumeroOrdine());
                    saveProdotti(ordine);
                    return;
                }
            }
        } catch (DAOException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    private Ordine buildOrdineFromRow(String[] row) {
        Long numeroOrdine = Long.parseLong(row[0]);
        String clienteId = row[1];
        LocalDateTime dataCreazione = row.length > 2 && !row[2].isEmpty() ? LocalDateTime.parse(row[2], DATETIME_FORMAT)
                : LocalDateTime.now();
        LocalDateTime dataConferma = row.length > 3 && !row[3].isEmpty() ? LocalDateTime.parse(row[3], DATETIME_FORMAT)
                : null;
        StatoOrdine stato = row.length > 4 && !row[4].isEmpty() ? StatoOrdine.valueOf(row[4])
                : StatoOrdine.IN_CREAZIONE;
        Double totale = row.length > 5 && !row[5].isEmpty() ? Double.parseDouble(row[5]) : 0.0;

        Ordine ordine = new Ordine(numeroOrdine, clienteId, dataCreazione, dataConferma, stato);
        ordine.setTotaleCached(totale);

        // Load voucher if present
        if (row.length > 6 && !row[6].isEmpty()) {
            try {
                Long voucherId = Long.parseLong(row[6]);
                Voucher voucher = VoucherDAOFS.getInstance().getVoucherById(voucherId);
                ordine.setVoucher(voucher);
            } catch (Exception e) {
                // Log per debugging - il voucher potrebbe non essere valido/trovato
                final Long ordNum = numeroOrdine;
                logger.log(Level.FINE, e,
                        () -> "Voucher not found or invalid for ordine " + ordNum + ": " + e.getMessage());
            }
        }

        // Load products is not done here to avoid complexity
        // Products can be loaded on demand

        return ordine;
    }

    private String[] buildRowFromOrdine(Ordine ordine) {
        return new String[] {
                String.valueOf(ordine.getNumeroOrdine()),
                ordine.getClienteId(),
                ordine.getDataCreazione() != null ? ordine.getDataCreazione().format(DATETIME_FORMAT) : "",
                ordine.getDataConferma() != null ? ordine.getDataConferma().format(DATETIME_FORMAT) : "",
                ordine.getStato().name(),
                String.valueOf(ordine.getTotale()),
                ordine.hasVoucher() && ordine.getVoucher().getId() != null ? String.valueOf(ordine.getVoucher().getId())
                        : ""
        };
    }

    private void saveProdotti(Ordine ordine) throws DAOException {
        for (Food food : ordine.getProdotti()) {
            if (food.getId() != null) {
                csvManager.appendLine(PRODOTTI_FILENAME, new String[] {
                        String.valueOf(ordine.getNumeroOrdine()),
                        String.valueOf(food.getId())
                });
            }
        }
    }

    private void deleteProdotti(Long ordineId) throws DAOException {
        List<String[]> allLines = csvManager.readAll(PRODOTTI_FILENAME);
        List<String[]> toKeep = new ArrayList<>();
        toKeep.add(allLines.get(0)); // Keep header

        for (int i = 1; i < allLines.size(); i++) {
            String[] row = allLines.get(i);
            if (row.length >= 1 && Long.parseLong(row[0]) != ordineId) {
                toKeep.add(row);
            }
        }

        csvManager.writeAll(PRODOTTI_FILENAME, toKeep);
    }
}
