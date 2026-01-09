package org.example.model.voucher.DAO;

import org.example.csv.CSVFileManager;
import org.example.enums.ExceptionMessagesEnum;
import org.example.exceptions.*;
import org.example.model.voucher.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO per Voucher basato su File System (CSV).
 */
public class VoucherDAOFS implements VoucherDAOInterface {

    private static VoucherDAOFS instance;
    private final CSVFileManager csvManager;
    private static final String FILENAME = "voucher";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    // CSV columns: id, codice, tipo, valore, valore_extra, data_scadenza, attivo,
    // descrizione
    // valore_extra: for FISSO = minimoOrdine, for PERCENTUALE = not used
    private static final String[] HEADER = { "id", "codice", "tipo", "valore", "valore_extra", "data_scadenza",
            "attivo", "descrizione" };

    private VoucherDAOFS() {
        this.csvManager = CSVFileManager.getInstance();
        initializeFile();
    }

    public static synchronized VoucherDAOFS getInstance() {
        if (instance == null) {
            instance = new VoucherDAOFS();
        }
        return instance;
    }

    private void initializeFile() {
        try {
            csvManager.createFileWithHeader(FILENAME, HEADER);
        } catch (DAOException e) {
            System.err.println("Warning: Could not initialize voucher.csv: " + e.getMessage());
        }
    }

    @Override
    public Voucher getVoucherById(Long id) throws DAOException, ObjectNotFoundException {
        try {
            List<String[]> allVouchers = csvManager.readAllWithoutHeader(FILENAME);
            for (String[] row : allVouchers) {
                if (row.length >= 1 && Long.parseLong(row[0]) == id) {
                    return buildVoucherFromRow(row);
                }
            }
            throw new ObjectNotFoundException(ExceptionMessagesEnum.OBJ_NOT_FOUND.message);
        } catch (DAOException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    @Override
    public Voucher getVoucherByCodice(String codice) throws DAOException, ObjectNotFoundException {
        try {
            List<String[]> allVouchers = csvManager.readAllWithoutHeader(FILENAME);
            for (String[] row : allVouchers) {
                if (row.length >= 2 && row[1].equalsIgnoreCase(codice)) {
                    return buildVoucherFromRow(row);
                }
            }
            throw new ObjectNotFoundException(ExceptionMessagesEnum.OBJ_NOT_FOUND.message);
        } catch (DAOException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    @Override
    public List<Voucher> getAllVoucherAttivi() throws DAOException {
        try {
            List<String[]> allVouchers = csvManager.readAllWithoutHeader(FILENAME);
            List<Voucher> result = new ArrayList<>();

            for (String[] row : allVouchers) {
                Voucher v = buildVoucherFromRow(row);
                if (v.isAttivo()) {
                    result.add(v);
                }
            }
            return result;
        } catch (DAOException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    @Override
    public void insert(Voucher voucher) throws DAOException {
        // Generate ID if not present
        if (voucher.getId() == null) {
            voucher.setId(getNextId());
        }
        String[] row = buildRowFromVoucher(voucher);
        csvManager.appendLine(FILENAME, row);
    }

    @Override
    public void delete(Voucher voucher) throws DAOException {
        try {
            List<String[]> allLines = csvManager.readAll(FILENAME);

            for (int i = 1; i < allLines.size(); i++) { // Skip header
                String[] row = allLines.get(i);
                if (row.length >= 1 && Long.parseLong(row[0]) == voucher.getId()) {
                    csvManager.deleteLine(FILENAME, i);
                    return;
                }
            }
        } catch (DAOException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    @Override
    public void update(Voucher voucher) throws DAOException {
        try {
            List<String[]> allLines = csvManager.readAll(FILENAME);

            for (int i = 1; i < allLines.size(); i++) { // Skip header
                String[] row = allLines.get(i);
                if (row.length >= 1 && Long.parseLong(row[0]) == voucher.getId()) {
                    csvManager.updateLine(FILENAME, i, buildRowFromVoucher(voucher));
                    return;
                }
            }
        } catch (DAOException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    private long getNextId() throws DAOException {
        List<String[]> allVouchers = csvManager.readAllWithoutHeader(FILENAME);
        long maxId = 0;
        for (String[] row : allVouchers) {
            if (row.length >= 1 && !row[0].isEmpty()) {
                long id = Long.parseLong(row[0]);
                if (id > maxId) {
                    maxId = id;
                }
            }
        }
        return maxId + 1;
    }

    private Voucher buildVoucherFromRow(String[] row) {
        Long id = Long.parseLong(row[0]);
        String codice = row[1];
        String tipo = row[2];
        double valore = Double.parseDouble(row[3]);
        double valoreExtra = row.length > 4 && !row[4].isEmpty() ? Double.parseDouble(row[4]) : 0;
        LocalDate dataScadenza = row.length > 5 && !row[5].isEmpty() ? LocalDate.parse(row[5], DATE_FORMAT) : null;
        boolean attivo = row.length <= 6 || row[6].isEmpty() || Boolean.parseBoolean(row[6]);

        Voucher voucher;
        if ("PERCENTUALE".equalsIgnoreCase(tipo)) {
            voucher = new VoucherPercentuale(id, codice, valore, dataScadenza);
        } else if ("FISSO".equalsIgnoreCase(tipo)) {
            voucher = new VoucherFisso(id, codice, valore, valoreExtra, dataScadenza);
        } else {
            voucher = new NessunVoucher();
        }
        voucher.setAttivo(attivo);

        return voucher;
    }

    private String[] buildRowFromVoucher(Voucher voucher) {
        String valoreExtra = "";
        double valore = 0;

        if (voucher instanceof VoucherPercentuale vp) {
            valore = vp.getPercentuale();
        } else if (voucher instanceof VoucherFisso vf) {
            valore = vf.getImportoSconto();
            valoreExtra = String.valueOf(vf.getMinimoOrdine());
        }

        return new String[] {
                String.valueOf(voucher.getId()),
                voucher.getCodice(),
                voucher.getTipoVoucher(),
                String.valueOf(valore),
                valoreExtra,
                voucher.getDataScadenza() != null ? voucher.getDataScadenza().format(DATE_FORMAT) : "",
                String.valueOf(voucher.isAttivo()),
                voucher.getDescrizione()
        };
    }
}
