package org.example.model.role.Cliente.DAO;

import org.example.csv.CSVFileManager;
import org.example.enums.ExceptionMessagesEnum;
import org.example.exceptions.*;
import org.example.model.role.Cliente.Cliente;
import org.example.model.user.User;

import java.util.List;

/**
 * DAO per Cliente basato su File System (CSV).
 */
public class ClienteDAOFS implements ClienteDAOInterface {

    private static ClienteDAOFS instance;
    private final CSVFileManager csvManager;
    private static final String FILENAME = "clienti";

    // CSV columns: id, user_id, punteggio, voucher_utilizzati, ordini_effettuati
    private static final String[] HEADER = { "id", "user_id", "punteggio", "voucher_utilizzati", "ordini_effettuati" };

    private ClienteDAOFS() {
        this.csvManager = CSVFileManager.getInstance();
        initializeFile();
    }

    public static synchronized ClienteDAOFS getInstance() {
        if (instance == null) {
            instance = new ClienteDAOFS();
        }
        return instance;
    }

    private void initializeFile() {
        try {
            csvManager.createFileWithHeader(FILENAME, HEADER);
        } catch (DAOException e) {
            System.err.println("Warning: Could not initialize clienti.csv: " + e.getMessage());
        }
    }

    @Override
    public Cliente getClienteByUser(User user) throws DAOException {
        try {
            List<String[]> allClienti = csvManager.readAllWithoutHeader(FILENAME);
            for (String[] row : allClienti) {
                if (row.length >= 2 && row[1].equalsIgnoreCase(user.getId())) {
                    return buildClienteFromRow(row, user);
                }
            }
            // If not found, create a new Cliente for this user
            return new Cliente(user, user.getId());
        } catch (DAOException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    @Override
    public void insert(Cliente cliente) throws DAOException {
        String[] row = buildRowFromCliente(cliente);
        csvManager.appendLine(FILENAME, row);
    }

    @Override
    public void delete(Cliente cliente) throws DAOException {
        try {
            List<String[]> allLines = csvManager.readAll(FILENAME);

            for (int i = 1; i < allLines.size(); i++) { // Skip header
                String[] row = allLines.get(i);
                if (row.length >= 1 && row[0].equalsIgnoreCase(cliente.getCodiceFiscale())) {
                    csvManager.deleteLine(FILENAME, i);
                    return;
                }
            }
        } catch (DAOException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    @Override
    public void update(Cliente cliente) throws DAOException {
        try {
            List<String[]> allLines = csvManager.readAll(FILENAME);

            for (int i = 1; i < allLines.size(); i++) { // Skip header
                String[] row = allLines.get(i);
                if (row.length >= 1 && row[0].equalsIgnoreCase(cliente.getCodiceFiscale())) {
                    csvManager.updateLine(FILENAME, i, buildRowFromCliente(cliente));
                    return;
                }
            }
        } catch (DAOException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    private Cliente buildClienteFromRow(String[] row, User user) {
        String id = row[0];
        int punteggio = row.length > 2 ? Integer.parseInt(row[2]) : 0;

        Cliente cliente = new Cliente(user, id);
        cliente.setPunteggio(punteggio);

        // Parse voucher utilizzati (comma-separated)
        if (row.length > 3 && !row[3].isEmpty()) {
            String[] vouchers = row[3].split(";");
            for (String v : vouchers) {
                if (!v.isEmpty()) {
                    cliente.aggiungiVoucherUtilizzato(v);
                }
            }
        }

        // Parse ordini effettuati (semicolon-separated)
        if (row.length > 4 && !row[4].isEmpty()) {
            String[] ordini = row[4].split(";");
            for (String o : ordini) {
                if (!o.isEmpty()) {
                    cliente.aggiungiOrdine(Long.parseLong(o));
                }
            }
        }

        return cliente;
    }

    private String[] buildRowFromCliente(Cliente cliente) {
        String vouchersStr = String.join(";", cliente.getVoucherUtilizzati());
        String ordiniStr = cliente.getOrdiniEffettuati().stream()
                .map(String::valueOf)
                .reduce((a, b) -> a + ";" + b)
                .orElse("");

        return new String[] {
                cliente.getCodiceFiscale(),
                cliente.getUser() != null ? cliente.getUser().getId() : "",
                String.valueOf(cliente.getPunteggio()),
                vouchersStr,
                ordiniStr
        };
    }
}
