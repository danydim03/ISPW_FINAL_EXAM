// java
package org.example.dao_manager;

import org.example.model.role.Amministratore.AmministratoreDAODB;
import org.example.model.role.Amministratore.AmministratoreDAOInterface;
import org.example.model.role.Cliente.ClientiDAODB;
import org.example.model.role.Kebabbaro.KebabbaroDAODB;
import org.example.model.role.Kebabbaro.KebabbaroDAOInterface;
import org.example.model.user.UserDAODB;
import org.example.model.user.UserDAOInterface;
import org.example.model.role.Cliente.ClienteDAOInterface;

public class DAOFactoryDB extends DAOFactoryAbstract {
    @Override
    public KebabbaroDAOInterface getKebabbaroDAO(){
        return KebabbaroDAODB.getInstance();
    }
    @Override
   public AmministratoreDAOInterface  getAmministratoreDAO(){
        return AmministratoreDAODB.getInstance();
    }
//    @Override
    public ClienteDAOInterface getStudentDAO(){
        return ClientiDAODB.getInstance();
    }

    @Override
    public ClienteDAOInterface getClienteDAO(){
        return ClientiDAODB.getInstance();
    }

    @Override
    public UserDAOInterface getUserDAO(){
        return UserDAODB.getInstance();
    }


}
