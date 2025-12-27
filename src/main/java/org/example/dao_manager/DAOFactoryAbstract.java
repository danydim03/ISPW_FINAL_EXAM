package org.example.dao_manager;

import org.example.PropertiesHandler;
import org.example.enums.ExceptionMessagesEnum;
import org.example.enums.PersistenceTypeEnum;
import org.example.exceptions.PropertyException;
import org.example.exceptions.ResourceNotFoundException;
import org.example.model.role.Cliente.DAO.ClienteDAOInterface;
import org.example.model.ordine.DAO.OrdineDAOInterface;
import org.example.model.role.Kebabbaro.DAO.KebabbaroDAOInterface;
import org.example.model.role.Amministratore.DAO.AmministratoreDAOInterface;
import org.example.model.user.DAO.UserDAOInterface;
import org.example.model.voucher.DAO.VoucherDAOInterface;
import org.example.model.food.DAO.FoodDAOInterface;

public abstract class DAOFactoryAbstract {

    private static DAOFactoryAbstract me = null;

    protected DAOFactoryAbstract() {
    }

    public static synchronized DAOFactoryAbstract getInstance() throws ResourceNotFoundException, PropertyException {
        if (me == null) {
            PersistenceTypeEnum persistenceType = PersistenceTypeEnum
                    .getPersistenceTypeByValue(PropertiesHandler.getInstance().getProperty("persistence_type"));
            if (persistenceType != null)
                switch (persistenceType) {
                    case DB -> me = new DAOFactoryDB();
                    case DEMO -> me = new DAOFactoryDemo();
                    case FS -> me = new DAOFactoryFS();
                    default -> throw new PropertyException(ExceptionMessagesEnum.UNEXPECTED_PROPERTY_NAME.message);
                }
            else
                throw new ResourceNotFoundException(ExceptionMessagesEnum.RESOURCE_NOT_FOUND.message);
        }
        return me;
    }

    public abstract KebabbaroDAOInterface getKebabbaroDAO();

    public abstract AmministratoreDAOInterface getAmministratoreDAO();

    public abstract ClienteDAOInterface getClienteDAO();

    public abstract OrdineDAOInterface getOrdineDAO();

    public abstract VoucherDAOInterface getVoucherDAO();

    public abstract UserDAOInterface getUserDAO();

    public abstract FoodDAOInterface getFoodDAO();
}