package org.example.model.voucher;

import org.example.dao_manager.DAOFactoryAbstract;
import org.example.enums.ExceptionMessagesEnum;
import org.example.exceptions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * LazyFactory per la gestione dei Voucher.
 * Implementa il pattern Lazy Initialization con caching locale.
 */
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
     * Recupera un voucher per codice.
     * Cerca prima nella cache, poi nel database.
     */
    public Voucher getVoucherByCodice(String codice) throws DAOException, ObjectNotFoundException,
            MissingAuthorizationException, WrongListQueryIdentifierValue, UserNotFoundException,
            UnrecognizedRoleException {

        for (Voucher v : voucherCache) {
            if (v.getCodice() != null && v.getCodice().equalsIgnoreCase(codice)) {
                return v;
            }
        }

        try {
            Voucher daoVoucher = DAOFactoryAbstract.getInstance().getVoucherDAO().getVoucherByCodice(codice);
            voucherCache.add(daoVoucher);
            return daoVoucher;
        } catch (PropertyException | ResourceNotFoundException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    /**
     * Crea e salva un nuovo voucher.
     */
    public Voucher newVoucher(Voucher voucher) throws DAOException, MissingAuthorizationException {
        try {
            DAOFactoryAbstract.getInstance().getVoucherDAO().insert(voucher);
        } catch (PropertyException | ResourceNotFoundException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
        voucherCache.add(voucher);
        return voucher;
    }
}