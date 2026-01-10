package org.example.use_cases.storico_ordini;

import org.example.exceptions.*;
import org.example.use_cases.crea_ordine.beans.OrdineBean;
import org.example.model.ordine.Ordine;
import org.example.model.user.User;
import org.example.session_manager.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class StoricoOrdiniFacade {

    private final StoricoOrdiniController controller = new StoricoOrdiniController();

    public List<OrdineBean> getStoricoOrdini() throws DAOException, PropertyException,
            ResourceNotFoundException, UserNotFoundException, UnrecognizedRoleException,
            ObjectNotFoundException, MissingAuthorizationException, WrongListQueryIdentifierValue {
        User user = SessionManager.getInstance().getSessionUser();
        if (user == null) {
            throw new MissingAuthorizationException("Nessun utente loggato");
        }

        List<Ordine> ordini = controller.getOrdiniByCliente(user);
        List<OrdineBean> beans = new ArrayList<>();

        for (Ordine o : ordini) {
            OrdineBean bean = org.example.mappers.OrdineMapper.toBean(o);
            if (bean != null) {
                beans.add(bean);
            }
        }
        return beans;
    }
}
