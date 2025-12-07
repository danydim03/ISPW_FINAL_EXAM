package org.example.model.voucher;

import org.example.exceptions.*;
import org.example.instances_management_abstracts.DAODBAbstract;

import java.sql. ResultSet;
import java.sql. SQLException;
import java.time.LocalDate;
import java. util.List;

public class VoucherDAODB extends DAODBAbstract<Voucher> implements VoucherDAOInterface {
    
    private static final String VOUCHER = "VOUCHER";
    private static final String ID = "id";
    private static final String CODICE = "codice";
    private static final String DESCRIZIONE = "descrizione";
    private static final String DATA_SCADENZA = "data_scadenza";
    private static final String ATTIVO = "attivo";
    private static final String TIPO_VOUCHER = "tipo_voucher";
    private static final String VALORE = "valore"; // percentuale o importo fisso
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
    public Voucher getVoucherById(Long id) throws DAOException, ObjectNotFoundException, PropertyException, ResourceNotFoundException, 
            UserNotFoundException, UnrecognizedRoleException, MissingAuthorizationException, WrongListQueryIdentifierValue {
        return getQuery(
                VOUCHER,
                List.of(ID),
                List.of(id),
                List.of()
        );
    }
    
    @Override
    public Voucher getVoucherByCodice(String codice) throws DAOException, ObjectNotFoundException, PropertyException, ResourceNotFoundException, 
            UserNotFoundException, UnrecognizedRoleException, MissingAuthorizationException, WrongListQueryIdentifierValue {
        return getQuery(
                VOUCHER,
                List. of(CODICE),
                List.of(codice),
                List.of()
        );
    }
    
    @Override
    public List<Voucher> getAllVoucherAttivi() throws DAOException, PropertyException, ResourceNotFoundException, 
            UserNotFoundException, UnrecognizedRoleException, ObjectNotFoundException, MissingAuthorizationException, WrongListQueryIdentifierValue {
        return getListQuery(
                VOUCHER,
                List.of(ATTIVO),
                List.of(true),
                List.of()
        );
    }
    
    @Override
    public void insert(Voucher voucher) throws DAOException, PropertyException, ResourceNotFoundException, MissingAuthorizationException {
        double valore = 0;
        double minimoOrdine = 0;
        
        if (voucher instanceof VoucherPercentuale) {
            valore = ((VoucherPercentuale) voucher). getPercentuale();
        } else if (voucher instanceof VoucherFisso) {
            valore = ((VoucherFisso) voucher).getImportoSconto();
            minimoOrdine = ((VoucherFisso) voucher).getMinimoOrdine();
        }
        
        insertQuery(
                VOUCHER,
                List.of(
                        voucher.getCodice(),
                        voucher.getDescrizione(),
                        voucher.getDataScadenza(),
                        voucher. isAttivo(),
                        voucher.getTipoVoucher(),
                        valore,
                        minimoOrdine
                )
        );
    }
    
    @Override
    public void delete(Voucher voucher) throws DAOException, PropertyException, ResourceNotFoundException {
        deleteQuery(
                VOUCHER,
                List.of(ID),
                List.of(voucher.getId())
        );
    }
    
    @Override
    public void update(Voucher voucher) throws DAOException, PropertyException, ResourceNotFoundException, MissingAuthorizationException {
        double valore = 0;
        double minimoOrdine = 0;
        
        if (voucher instanceof VoucherPercentuale) {
            valore = ((VoucherPercentuale) voucher).getPercentuale();
        } else if (voucher instanceof VoucherFisso) {
            valore = ((VoucherFisso) voucher).getImportoSconto();
            minimoOrdine = ((VoucherFisso) voucher).getMinimoOrdine();
        }
        
        updateQuery(
                VOUCHER,
                List.of(CODICE, DESCRIZIONE, DATA_SCADENZA, ATTIVO, TIPO_VOUCHER, VALORE, MINIMO_ORDINE),
                List.of(voucher.getCodice(), voucher.getDescrizione(), voucher.getDataScadenza(), 
                        voucher.isAttivo(), voucher.getTipoVoucher(), valore, minimoOrdine),
                List.of(ID),
                List.of(voucher.getId())
        );
    }
    
    @Override
    protected Voucher queryObjectBuilder(ResultSet rs, List<Object> objects) throws SQLException, DAOException, 
            PropertyException, ResourceNotFoundException, UserNotFoundException, UnrecognizedRoleException, 
            ObjectNotFoundException, MissingAuthorizationException, WrongListQueryIdentifierValue {
        
        Long voucherId = rs.getLong(ID);
        String codice = rs.getString(CODICE);
        String tipoVoucher = rs.getString(TIPO_VOUCHER);
        double valore = rs. getDouble(VALORE);
        double minimoOrdine = rs.getDouble(MINIMO_ORDINE);
        LocalDate dataScadenza = rs. getDate(DATA_SCADENZA) != null ? rs.getDate(DATA_SCADENZA).toLocalDate() : null;
        boolean attivo = rs.getBoolean(ATTIVO);
        
        Voucher voucher;
        
        switch (tipoVoucher) {
            case "PERCENTUALE":
                voucher = new VoucherPercentuale(voucherId, codice, valore, dataScadenza);
                break;
            case "FISSO":
                voucher = new VoucherFisso(voucherId, codice, valore, minimoOrdine, dataScadenza);
                break;
            case "NESSUNO":
            default:
                voucher = new NessunVoucher();
                voucher.setId(voucherId);
        }
        
        voucher. setAttivo(attivo);
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