package org.example.dao_manager;

import org.example.model.food.DAO.FoodDAODB;
import org.example.model.food.DAO.FoodDAOInterface;
import org.example.model.role.Amministratore.DAO.AmministratoreDAODB;
import org.example.model.role.Amministratore.DAO.AmministratoreDAOInterface;
import org.example.model.role.Cliente.DAO.ClientiDAODB;
import org.example.model.role.Cliente.DAO.ClienteDAOInterface;
import org.example.model.role.Kebabbaro.DAO.KebabbaroDAODB;
import org.example.model.role.Kebabbaro.DAO.KebabbaroDAOInterface;
import org.example.model.user.DAO.UserDAODB;
import org.example.model.user.DAO.UserDAOInterface;
import org.example.model.voucher.DAO.VoucherDAODB;
import org.example.model.voucher.DAO.VoucherDAOInterface;
import org.example.model.ordine.DAO.OrdineDAODB;
import org.example.model.ordine.DAO.OrdineDAOInterface;

public class DAOFactoryDB extends DAOFactoryAbstract {

    @Override
    public KebabbaroDAOInterface getKebabbaroDAO() {
        return KebabbaroDAODB.getInstance();
    }

    @Override
    public AmministratoreDAOInterface getAmministratoreDAO() {
        return AmministratoreDAODB.getInstance();
    }

    @Override
    public ClienteDAOInterface getClienteDAO() {
        return ClientiDAODB.getInstance();
    }

    @Override
    public UserDAOInterface getUserDAO() {
        return UserDAODB.getInstance();
    }

    @Override
    public VoucherDAOInterface getVoucherDAO() {
        return VoucherDAODB.getInstance();
    }

    @Override
    public OrdineDAOInterface getOrdineDAO() {
        return OrdineDAODB.getInstance();
    }

    @Override
    public FoodDAOInterface getFoodDAO() {
        return FoodDAODB.getInstance();
    }

}