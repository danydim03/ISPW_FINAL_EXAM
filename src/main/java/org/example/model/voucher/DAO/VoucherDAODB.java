package org.example.model.voucher.DAO;

import org.example.exceptions.*;
import org.example.instances_management_abstracts.DAODBAbstract;
import org.example.model.voucher.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class VoucherDAODB extends DAODBAbstract<Voucher> implements VoucherDAOInterface {

    private static final String VOUCHER = "VOUCHER";
    private static final String ID = "id";
    private static final String CODICE = "codice";
    private static final String DESCRIZIONE = "descrizione";
    private static final String DATA_SCADENZA = "data_scadenza";
    private static final String ATTIVO = "attivo";
    private static final String TIPO_VOUCHER = "tipo_voucher";
    private static final String VALORE = "valore";
    private static final String MINIMO_ORDINE = "minimo_ordine";

    protected static VoucherDAOInterface instance;

    private VoucherDAODB() {
    }

    public static synchronized VoucherDAOInterface getInstance() {
        if (instance == null) {
            instance = new VoucherDAODB();
        }
        return instance;
    }

    @Override
    public Voucher getVoucherById(Long id)
            throws DAOException, ObjectNotFoundException, PropertyException, ResourceNotFoundException,
            UserNotFoundException, UnrecognizedRoleException, MissingAuthorizationException,
            WrongListQueryIdentifierValue {
        return getQuery(
                VOUCHER,
                List.of(ID),
                List.of(id),
                List.of());
    }

    @Override
    public Voucher getVoucherByCodice(String codice)
            throws DAOException, ObjectNotFoundException, PropertyException, ResourceNotFoundException,
            UserNotFoundException, UnrecognizedRoleException, MissingAuthorizationException,
            WrongListQueryIdentifierValue {
        return getQuery(
                VOUCHER,
                List.of(CODICE),
                List.of(codice),
                List.of());
    }

    @Override
    public List<Voucher> getAllVoucherAttivi() throws DAOException, PropertyException, ResourceNotFoundException,
            UserNotFoundException, UnrecognizedRoleException, ObjectNotFoundException, MissingAuthorizationException,
            WrongListQueryIdentifierValue {
        return getListQuery(
                VOUCHER,
                List.of(ATTIVO),
                List.of(true),
                List.of(),
                List.of(),
                Boolean.FALSE);
    }

    @Override
    public void insert(Voucher voucher)
            throws DAOException, PropertyException, ResourceNotFoundException, MissingAuthorizationException {
        double valore = 0;
        double minimoOrdine = 0;

        if (voucher instanceof VoucherPercentuale voucherPercentuale) {
            valore = voucherPercentuale.getPercentuale();
        } else if (voucher instanceof VoucherFisso voucherFisso) {
            valore = voucherFisso.getImportoSconto();
            minimoOrdine = voucherFisso.getMinimoOrdine();
        }

        // Usa insertQueryWithColumns per specificare esplicitamente le colonne
        // (escludendo l'ID che è auto-incrementante)
        insertQueryWithColumns(
                VOUCHER,
                List.of(CODICE, DESCRIZIONE, DATA_SCADENZA, ATTIVO, TIPO_VOUCHER, VALORE, MINIMO_ORDINE),
                List.of(
                        voucher.getCodice(),
                        voucher.getDescrizione(),
                        voucher.getDataScadenza(),
                        voucher.isAttivo(),
                        voucher.getTipoVoucher(),
                        valore,
                        minimoOrdine));
    }

    /**
     * Insert personalizzato che specifica le colonne esplicitamente.
     * Necessario perché la tabella ha ID auto-incrementante.
     * 
     * @param table   nome tabella
     * @param columns nomi delle colonne (senza ID)
     * @param values  valori corrispondenti
     */
    private void insertQueryWithColumns(String table, List<String> columns, List<Object> values)
            throws DAOException, PropertyException, ResourceNotFoundException {
        if (columns.size() != values.size()) {
            throw new DAOException("Column count doesn't match value count");
        }

        // Costruisci la lista di colonne: "col1, col2, col3"
        String columnsList = String.join(", ", columns);

        // Costruisci i placeholder: "?, ?, ?"
        StringBuilder questionMarks = new StringBuilder();
        questionMarks.append("?,".repeat(values.size()));
        questionMarks.deleteCharAt(questionMarks.length() - 1);

        // Query: INSERT INTO table (col1, col2, col3) VALUES (?, ?, ?)
        String query = String.format("INSERT INTO %s (%s) VALUES (%s)", table, columnsList, questionMarks);

        // Esegui la query
        executeInsertQuery(values, query);
    }

    /**
     * Metodo helper per eseguire query INSERT con parametri.
     */
    private void executeInsertQuery(List<Object> values, String query)
            throws DAOException, PropertyException, ResourceNotFoundException {
        java.sql.Connection connection;
        try {
            connection = org.example.dao_manager.DBConnection.getInstance().getConnection();
        } catch (java.sql.SQLException e) {
            throw new DAOException(org.example.enums.ExceptionMessagesEnum.DAO.message, e);
        }
        try (java.sql.PreparedStatement stmt = connection.prepareStatement(query)) {
            setQueryQuestionMarksValue(stmt, values, 1);
            stmt.executeUpdate();
        } catch (java.sql.SQLException e) {
            throw new DAOException(org.example.enums.ExceptionMessagesEnum.DAO.message, e);
        }
    }

    @Override
    public void delete(Voucher voucher) throws DAOException, PropertyException, ResourceNotFoundException {
        deleteQuery(
                VOUCHER,
                List.of(ID),
                List.of(voucher.getId()));
    }

    @Override
    public void update(Voucher voucher)
            throws DAOException, PropertyException, ResourceNotFoundException, MissingAuthorizationException {
        double valore = 0;
        double minimoOrdine = 0;

        if (voucher instanceof VoucherPercentuale voucherPercentuale) {
            valore = voucherPercentuale.getPercentuale();
        } else if (voucher instanceof VoucherFisso voucherFisso) {
            valore = voucherFisso.getImportoSconto();
            minimoOrdine = voucherFisso.getMinimoOrdine();
        }

        updateQuery(
                VOUCHER,
                List.of(CODICE, DESCRIZIONE, DATA_SCADENZA, ATTIVO, TIPO_VOUCHER, VALORE, MINIMO_ORDINE),
                List.of(voucher.getCodice(), voucher.getDescrizione(), voucher.getDataScadenza(),
                        voucher.isAttivo(), voucher.getTipoVoucher(), valore, minimoOrdine),
                List.of(ID),
                List.of(voucher.getId()));
    }

    @Override
    protected Voucher queryObjectBuilder(ResultSet rs, List<Object> objects) throws SQLException, DAOException,
            PropertyException, ResourceNotFoundException, UserNotFoundException, UnrecognizedRoleException,
            ObjectNotFoundException, MissingAuthorizationException, WrongListQueryIdentifierValue {

        Long voucherId = rs.getLong(ID);
        String codice = rs.getString(CODICE);
        String tipoVoucher = rs.getString(TIPO_VOUCHER);
        double valore = rs.getDouble(VALORE);
        double minOrdine = rs.getDouble(MINIMO_ORDINE);
        LocalDate dataScadenza = rs.getDate(DATA_SCADENZA) != null ? rs.getDate(DATA_SCADENZA).toLocalDate() : null;
        boolean attivo = rs.getBoolean(ATTIVO);

        Voucher voucher;

        switch (tipoVoucher) {
            case "PERCENTUALE":
                voucher = new VoucherPercentuale(voucherId, codice, valore, dataScadenza);
                break;
            case "FISSO":
                voucher = new VoucherFisso(voucherId, codice, valore, minOrdine, dataScadenza);
                break;
            case "NESSUNO":
            default:
                voucher = new NessunVoucher();
                voucher.setId(voucherId);
        }

        voucher.setAttivo(attivo);
        return voucher;
    }

    @Override
    protected String setGetListQueryIdentifiersValue(Voucher voucher, int valueNumber) throws DAOException {
        if (valueNumber == 0) {
            return String.valueOf(voucher.isAttivo());
        }
        return null;
    }
}