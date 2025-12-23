package org.example.dao_manager;

import org.example.model.role.Amministratore.AmministratoreDAOInterface;
import org.example.model.role.Amministratore.AmministratoreDAODemo;
import org.example.model.role.Cliente.ClienteDAOInterface;
import org.example.model.role.Cliente.ClienteDAODemo;
import org.example.model.role.Kebabbaro.KebabbaroDAOInterface;
import org.example.model.role.Kebabbaro.KebabbaroDAODemo;
import org.example.model.food.FoodDAODemo;
import org.example.model.food.FoodDAOInterface;
import org.example.model.ordine.OrdineDAODemo;
import org.example.model.ordine.OrdineDAOInterface;
import org.example.model.user.UserDAODemo;
import org.example.model.user.UserDAOInterface;
import org.example.model.voucher.VoucherDAODemo;
import org.example.model.voucher.VoucherDAOInterface;

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
