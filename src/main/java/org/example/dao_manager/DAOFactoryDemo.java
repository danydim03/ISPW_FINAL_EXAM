package org.example.dao_manager;

import org.example.model.role.Amministratore.DAO.AmministratoreDAOInterface;
import org.example.model.role.Amministratore.DAO.AmministratoreDAODemo;
import org.example.model.role.Cliente.DAO.ClienteDAOInterface;
import org.example.model.role.Cliente.DAO.ClienteDAODemo;
import org.example.model.role.Kebabbaro.DAO.KebabbaroDAOInterface;
import org.example.model.role.Kebabbaro.DAO.KebabbaroDAODemo;
import org.example.model.food.DAO.FoodDAODemo;
import org.example.model.food.DAO.FoodDAOInterface;
import org.example.model.ordine.DAO.OrdineDAODemo;
import org.example.model.ordine.DAO.OrdineDAOInterface;
import org.example.model.user.DAO.UserDAODemo;
import org.example.model.user.DAO.UserDAOInterface;
import org.example.model.voucher.DAO.VoucherDAODemo;
import org.example.model.voucher.DAO.VoucherDAOInterface;

public class DAOFactoryDemo extends DAOFactoryAbstract {

    @Override
    public KebabbaroDAOInterface getKebabbaroDAO() {
        return new KebabbaroDAODemo();
    }

    @Override
    public AmministratoreDAOInterface getAmministratoreDAO() {
        return new AmministratoreDAODemo();
    }

    @Override
    public ClienteDAOInterface getClienteDAO() {
        return new ClienteDAODemo();
    }

    @Override
    public OrdineDAOInterface getOrdineDAO() {
        return new OrdineDAODemo();
    }

    @Override
    public VoucherDAOInterface getVoucherDAO() {
        return new VoucherDAODemo();
    }

    @Override
    public UserDAOInterface getUserDAO() {
        return new UserDAODemo();
    }

    @Override
    public FoodDAOInterface getFoodDAO() {
        return new FoodDAODemo();
    }
}
