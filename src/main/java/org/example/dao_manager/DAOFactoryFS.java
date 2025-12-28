package org.example.dao_manager;

import org.example.model.food.DAO.FoodDAOFS;
import org.example.model.food.DAO.FoodDAOInterface;
import org.example.model.ordine.DAO.OrdineDAOFS;
import org.example.model.ordine.DAO.OrdineDAOInterface;
import org.example.model.role.Amministratore.DAO.AmministratoreDAOFS;
import org.example.model.role.Amministratore.DAO.AmministratoreDAOInterface;
import org.example.model.role.Cliente.DAO.ClienteDAOFS;
import org.example.model.role.Cliente.DAO.ClienteDAOInterface;
import org.example.model.role.Kebabbaro.DAO.KebabbaroDAOFS;
import org.example.model.role.Kebabbaro.DAO.KebabbaroDAOInterface;
import org.example.model.user.DAO.UserDAOFS;
import org.example.model.user.DAO.UserDAOInterface;
import org.example.model.voucher.DAO.VoucherDAOFS;
import org.example.model.voucher.DAO.VoucherDAOInterface;

/**
 * DAO Factory implementation for File System (CSV) persistence.
 */
public class DAOFactoryFS extends DAOFactoryAbstract {

    @Override
    public KebabbaroDAOInterface getKebabbaroDAO() {
        return KebabbaroDAOFS.getInstance();
    }

    @Override
    public AmministratoreDAOInterface getAmministratoreDAO() {
        return AmministratoreDAOFS.getInstance();
    }

    @Override
    public ClienteDAOInterface getClienteDAO() {
        return ClienteDAOFS.getInstance();
    }

    @Override
    public OrdineDAOInterface getOrdineDAO() {
        return OrdineDAOFS.getInstance();
    }

    @Override
    public VoucherDAOInterface getVoucherDAO() {
        return VoucherDAOFS.getInstance();
    }

    @Override
    public UserDAOInterface getUserDAO() {
        return UserDAOFS.getInstance();
    }

    @Override
    public FoodDAOInterface getFoodDAO() {
        return FoodDAOFS.getInstance();
    }
}
