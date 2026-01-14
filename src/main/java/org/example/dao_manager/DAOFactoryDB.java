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

/*

DAOFactoryDB
 è una Concrete Factory (Fabbrica Concreta) nel pattern Abstract Factory.

Cosa fa?
Crea e restituisce tutti i DAO specifici per la persistenza su Database H2.
    Implementa le operazioni astratti definiti in DAOFactoryAbstract
    per fornire istanze concrete dei DAO per ogni entità del sistema.
Perché è importante?
    Incapsula la logica di creazione dei DAO per il database,
    permettendo al resto dell'applicazione di essere indipendente
    dalla specifica implementazione di persistenza.

 */

public class DAOFactoryDB extends DAOFactoryAbstract {


    @Override
    public AmministratoreDAOInterface getAmministratoreDAO() {
        return AmministratoreDAODB.getInstance();
    }

    @Override
    public KebabbaroDAOInterface getKebabbaroDAO() {
        return KebabbaroDAODB.getInstance();
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

/*
In sintesi:
KebabbaroDAODB = Classe concreta che gestisce le operazioni CRUD per l'entità
Kebabbaro usando il database H2

essa Estende DAODBAbstract<Kebabbaro> (eredita la logica comune DB) e
Implementa KebabbaroDAOInterface (rispetta il contratto)
Usa il pattern Singleton (getInstance()) per garantire una singola istanza
Fornisce metodi specifici come getKebabbaroByUser per recuperare Kebabbaro
dal DB in base a criteri specifici.

 */