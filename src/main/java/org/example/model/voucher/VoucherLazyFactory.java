package org.example.model.voucher;

import org.example. dao_manager.DAOFactoryAbstract;
import org.example.enums.ExceptionMessagesEnum;
import org.example.exceptions.*;

import java.util.ArrayList;
import java.util.List;

public class VoucherLazyFactory {
    
    private static VoucherLazyFactory instance;
    private final List<Voucher> voucherCache;
    
    private VoucherLazyFactory() {
        voucherCache = new ArrayList<>();
    }
    
    public static synchronized VoucherLazyFactory getInstance() {
        if (instance == null) {
            instance = new VoucherLazyFactory();
        }
        return instance;
    }
    
    /**
     * Recupera un Voucher per ID, usando la cache se disponibile
     */
    public Voucher getVoucherById(Long id) throws DAOException, ObjectNotFoundException, 
            MissingAuthorizationException, WrongListQueryIdentifierValue, UserNotFoundException, UnrecognizedRoleException {
        
        // Cerca prima nella cache
        for (Voucher v : voucherCache) {
            if (v.getId() != null && v.getId().equals(id)) {
                return v;
            }
        }
        
        // Se non trovato, recupera dal DAO
        try {
            Voucher daoVoucher = DAOFactoryAbstract.getInstance().getVoucherDAO().getVoucherById(id);
            voucherCache.add(daoVoucher);
            return daoVoucher;
        } catch (PropertyException | ResourceNotFoundException e) {
            throw new DAOException(ExceptionMessagesEnum. DAO. message, e);
        }
    }
    
    /**
     * Recupera un Voucher per codice, usando la cache se disponibile
     */
    public Voucher getVoucherByCodice(String codice) throws DAOException, ObjectNotFoundException, 
            MissingAuthorizationException, WrongListQueryIdentifierValue, UserNotFoundException, UnrecognizedRoleException {
        
        // Cerca prima nella cache
        for (Voucher v : voucherCache) {
            if (v.getCodice() != null && v.getCodice().equalsIgnoreCase(codice)) {
                return v;
            }
        }
        
        // Se non trovato, recupera dal DAO
        try {
            Voucher daoVoucher = DAOFactoryAbstract.getInstance().getVoucherDAO().getVoucherByCodice(codice);
            voucherCache.add(daoVoucher);
            return daoVoucher;
        } catch (PropertyException | ResourceNotFoundException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }
    
    /**
     * Recupera tutti i voucher attivi
     */
    public List<Voucher> getAllVoucherAttivi() throws DAOException, ObjectNotFoundException, 
            MissingAuthorizationException, WrongListQueryIdentifierValue, UserNotFoundException, UnrecognizedRoleException {
        try {
            List<Voucher> vouchers = DAOFactoryAbstract.getInstance().getVoucherDAO().getAllVoucherAttivi();
            // Aggiorna cache
            for (Voucher v : vouchers) {
                if (! isVoucherInCache(v. getId())) {
                    voucherCache.add(v);
                }
            }
            return vouchers;
        } catch (PropertyException | ResourceNotFoundException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO. message, e);
        }
    }
    
    /**
     * Crea e salva un nuovo Voucher
     */
    public Voucher newVoucher(Voucher voucher) throws DAOException, MissingAuthorizationException {
        try {
            DAOFactoryAbstract.getInstance().getVoucherDAO().insert(voucher);
        } catch (PropertyException | ResourceNotFoundException e) {
            throw new DAOException(ExceptionMessagesEnum. DAO.message, e);
        }
        voucherCache.add(voucher);
        return voucher;
    }
    
    /**
     * Verifica se un Voucher è già in cache
     */
    private boolean isVoucherInCache(Long id) {
        if (id == null) return false;
        for (Voucher v : voucherCache) {
            if (v.getId() != null && v.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Pulisce la cache
     */
    public void clearCache() {
        voucherCache.clear();
    }
}