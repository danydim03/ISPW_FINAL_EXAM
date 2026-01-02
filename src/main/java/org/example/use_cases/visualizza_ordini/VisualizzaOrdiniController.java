package org.example.use_cases.visualizza_ordini;

import org.example.dao_manager.DAOFactoryAbstract;
import org.example.enums.StatoOrdine;
import org.example.exceptions.*;
import org.example.model.ordine.Ordine;
import org.example.model.ordine.DAO.OrdineDAOInterface;

import java.util.List;

public class VisualizzaOrdiniController {

    public List<Ordine> getOrdiniByStato(StatoOrdine stato) throws DAOException, PropertyException,
            ResourceNotFoundException, UserNotFoundException, UnrecognizedRoleException,
            ObjectNotFoundException, MissingAuthorizationException, WrongListQueryIdentifierValue {
        OrdineDAOInterface dao = DAOFactoryAbstract.getInstance().getOrdineDAO();
        return dao.getOrdiniByStato(stato);
    }

    public void aggiornaStatoOrdine(Long numeroOrdine, StatoOrdine nuovoStato) throws DAOException, PropertyException,
            ResourceNotFoundException, UserNotFoundException, UnrecognizedRoleException,
            ObjectNotFoundException, MissingAuthorizationException, WrongListQueryIdentifierValue {
        OrdineDAOInterface dao = DAOFactoryAbstract.getInstance().getOrdineDAO();
        Ordine ordine = dao.getOrdineByNumero(numeroOrdine);
        ordine.setStato(nuovoStato);
        dao.update(ordine);
    }
}
