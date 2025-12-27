package org.example.model.ordine.DAO;

import org.example.dao_manager.DBConnection;
import org.example.exceptions.*;
import org.example.instances_management_abstracts.DAODBAbstract;
import org.example.enums.StatoOrdine;
import org.example.model.ordine.Ordine;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Implementazione DAO per Ordine con persistenza su Database.
 * Estende DAODBAbstract seguendo il pattern del progetto.
 */
public class OrdineDAODB extends DAODBAbstract<Ordine> implements OrdineDAOInterface {

    private static final String ORDINE = "ORDINE";
    private static final String NUMERO_ORDINE = "numero_ordine";
    private static final String CLIENTE_ID = "cliente_id";
    private static final String DATA_CREAZIONE = "data_creazione";
    private static final String DATA_CONFERMA = "data_conferma";
    private static final String STATO = "stato";
    private static final String VOUCHER_CODICE = "voucher_codice";
    private static final String TOTALE = "totale";

    protected static OrdineDAOInterface instance;

    private OrdineDAODB() {
    }

    public static synchronized OrdineDAOInterface getInstance() {
        if (instance == null) {
            instance = new OrdineDAODB();
        }
        return instance;
    }

    @Override
    public Ordine getOrdineByNumero(Long numeroOrdine) throws DAOException, ObjectNotFoundException, PropertyException,
            ResourceNotFoundException, UserNotFoundException, UnrecognizedRoleException, MissingAuthorizationException,
            WrongListQueryIdentifierValue {
        return getQuery(
                ORDINE,
                List.of(NUMERO_ORDINE),
                List.of(numeroOrdine),
                List.of());
    }

    @Override
    public List<Ordine> getOrdiniByCliente(String clienteId)
            throws DAOException, PropertyException, ResourceNotFoundException,
            UserNotFoundException, UnrecognizedRoleException, ObjectNotFoundException, MissingAuthorizationException,
            WrongListQueryIdentifierValue {
        return getListQuery(
                ORDINE,
                List.of(CLIENTE_ID),
                List.of(clienteId),
                List.of(),
                List.of(),
                Boolean.FALSE);
    }

    @Override
    public List<Ordine> getOrdiniByStato(StatoOrdine stato)
            throws DAOException, PropertyException, ResourceNotFoundException,
            UserNotFoundException, UnrecognizedRoleException, ObjectNotFoundException, MissingAuthorizationException,
            WrongListQueryIdentifierValue {
        return getListQuery(
                ORDINE,
                List.of(STATO),
                List.of(stato.name()),
                List.of(),
                List.of(),
                Boolean.FALSE);
    }

    @Override
    public void insert(Ordine ordine)
            throws DAOException, PropertyException, ResourceNotFoundException, MissingAuthorizationException {
        // Genera il numero ordine se non presente
        if (ordine.getNumeroOrdine() == null) {
            ordine.setNumeroOrdine(getNextNumeroOrdine());
        }

        // Inserisci l'ordine
        // NOTA: Usiamo Arrays.asList() perché List.of() non permette valori null
        insertQuery(
                ORDINE,
                Arrays.asList(
                        ordine.getNumeroOrdine(),
                        ordine.getClienteId(),
                        Timestamp.valueOf(ordine.getDataCreazione()),
                        ordine.getDataConferma() != null ? Timestamp.valueOf(ordine.getDataConferma()) : null,
                        ordine.getStato().name(),
                        ordine.hasVoucher() ? ordine.getVoucher().getCodice() : null,
                        ordine.getTotale()));
    }

    @Override
    public void delete(Ordine ordine) throws DAOException, PropertyException, ResourceNotFoundException {
        deleteQuery(
                ORDINE,
                List.of(NUMERO_ORDINE),
                List.of(ordine.getNumeroOrdine()));
    }

    @Override
    public void update(Ordine ordine)
            throws DAOException, PropertyException, ResourceNotFoundException, MissingAuthorizationException {
        updateQuery(
                ORDINE,
                List.of(CLIENTE_ID, DATA_CREAZIONE, DATA_CONFERMA, STATO, VOUCHER_CODICE, TOTALE),
                Arrays.asList(
                        ordine.getClienteId(),
                        Timestamp.valueOf(ordine.getDataCreazione()),
                        ordine.getDataConferma() != null ? Timestamp.valueOf(ordine.getDataConferma()) : null,
                        ordine.getStato().name(),
                        ordine.hasVoucher() ? ordine.getVoucher().getCodice() : null,
                        ordine.getTotale()),
                List.of(NUMERO_ORDINE),
                List.of(ordine.getNumeroOrdine()));
    }

    @Override
    public Long getNextNumeroOrdine() throws DAOException, PropertyException, ResourceNotFoundException {
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            String sql = "SELECT COALESCE(MAX(" + NUMERO_ORDINE + "), 0) + 1 AS next_numero FROM " + ORDINE;

            try (PreparedStatement stmt = conn.prepareStatement(sql);
                    ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("next_numero");
                }
                return 1L;
            }
        } catch (SQLException e) {
            throw new DAOException("Errore nel recupero del prossimo numero ordine: " + e.getMessage(), e);
        }
    }

    @Override
    protected Ordine queryObjectBuilder(ResultSet rs, List<Object> objects) throws SQLException, DAOException,
            PropertyException, ResourceNotFoundException, UserNotFoundException, UnrecognizedRoleException,
            ObjectNotFoundException, MissingAuthorizationException, WrongListQueryIdentifierValue {

        Long numOrdine = rs.getLong(NUMERO_ORDINE);
        String clienteId = rs.getString(CLIENTE_ID);

        Timestamp tsCreazione = rs.getTimestamp(DATA_CREAZIONE);
        LocalDateTime dataCreazione = tsCreazione != null ? tsCreazione.toLocalDateTime() : LocalDateTime.now();

        Timestamp tsConferma = rs.getTimestamp(DATA_CONFERMA);
        LocalDateTime dataConferma = tsConferma != null ? tsConferma.toLocalDateTime() : null;

        String statoStr = rs.getString(STATO);
        StatoOrdine stato = StatoOrdine.valueOf(statoStr);

        Ordine ordine = new Ordine(numOrdine, clienteId, dataCreazione, dataConferma, stato);

        // Leggi e imposta il totale dal database
        double totaleDB = rs.getDouble(TOTALE);
        ordine.setTotaleCached(totaleDB);

        // Carica il voucher se presente
        String voucherCodice = rs.getString(VOUCHER_CODICE);
        if (voucherCodice != null && !voucherCodice.isEmpty()) {
            // Il voucher verrà caricato dal VoucherLazyFactory se necessario
            // Per ora lasciamo NessunVoucher, il caricamento completo avverrà nel
            // LazyFactory
        }

        return ordine;
    }

    @Override
    protected String setGetListQueryIdentifiersValue(Ordine ordine, int valueNumber) throws DAOException {
        switch (valueNumber) {
            case 0:
                return ordine.getClienteId();
            case 1:
                return ordine.getStato().name();
            default:
                return null;
        }
    }
}