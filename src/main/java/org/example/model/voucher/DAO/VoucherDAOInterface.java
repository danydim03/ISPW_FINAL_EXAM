package org.example.model.voucher.DAO;

import org.example.exceptions.*;
import org.example.model.voucher.Voucher;

import java.util.List;

public interface VoucherDAOInterface {

        void insert(Voucher voucher) throws DAOException, PropertyException, ResourceNotFoundException,
                        MissingAuthorizationException;

        void delete(Voucher voucher) throws DAOException, PropertyException, ResourceNotFoundException;

        void update(Voucher voucher) throws DAOException, PropertyException, ResourceNotFoundException,
                        MissingAuthorizationException;

        Voucher getVoucherById(Long id)
                        throws DAOException, ObjectNotFoundException, PropertyException, ResourceNotFoundException,
                        UserNotFoundException, UnrecognizedRoleException, MissingAuthorizationException,
                        WrongListQueryIdentifierValue;

        Voucher getVoucherByCodice(String codice)
                        throws DAOException, ObjectNotFoundException, PropertyException, ResourceNotFoundException,
                        UserNotFoundException, UnrecognizedRoleException, MissingAuthorizationException,
                        WrongListQueryIdentifierValue;

        List<Voucher> getAllVoucherAttivi() throws DAOException, PropertyException, ResourceNotFoundException,
                        UserNotFoundException, UnrecognizedRoleException, ObjectNotFoundException,
                        MissingAuthorizationException, WrongListQueryIdentifierValue;
}