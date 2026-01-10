package org.example.use_cases.visualizza_ordini;

import org.example.exceptions.*;
import org.example.use_cases.crea_ordine.beans.OrdineBean;
import org.example.enums.StatoOrdine;
import org.example.model.ordine.Ordine;

import java.util.ArrayList;
import java.util.List;

public class VisualizzaOrdiniFacade {

    private final VisualizzaOrdiniController controller = new VisualizzaOrdiniController();

    public List<OrdineBean> getOrdiniInCreazione() throws DAOException, PropertyException,
            ResourceNotFoundException, UserNotFoundException, UnrecognizedRoleException,
            ObjectNotFoundException, MissingAuthorizationException, WrongListQueryIdentifierValue {
        List<Ordine> ordini = controller.getOrdiniByStato(StatoOrdine.IN_CREAZIONE);
        List<OrdineBean> beans = new ArrayList<>();

        for (Ordine o : ordini) {
            OrdineBean bean = org.example.mappers.OrdineMapper.toBean(o);
            if (bean != null) {
                beans.add(bean);
            }
        }
        return beans;
    }

    public void impostaInConsegna(OrdineBean ordineBean) throws DAOException, PropertyException,
            ResourceNotFoundException, UserNotFoundException, UnrecognizedRoleException,
            ObjectNotFoundException, MissingAuthorizationException, WrongListQueryIdentifierValue {
        controller.aggiornaStatoOrdine(ordineBean.getNumeroOrdine(), StatoOrdine.IN_CONSEGNA);
    }
}
