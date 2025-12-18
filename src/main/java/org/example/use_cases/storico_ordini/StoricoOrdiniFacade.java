package org.example.use_cases.storico_ordini;

import org.example.use_cases.crea_ordine.beans.OrdineBean;
import org.example.model.ordine.Ordine;
import org.example.model.user.User;
import org.example.session_manager.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class StoricoOrdiniFacade {

    private final StoricoOrdiniController controller = new StoricoOrdiniController();

    public List<OrdineBean> getStoricoOrdini() throws Exception {
        User user = SessionManager.getInstance().getSessionUser();
        if (user == null) {
            throw new Exception("Nessun utente loggato");
        }

        List<Ordine> ordini = controller.getOrdiniByCliente(user);
        List<OrdineBean> beans = new ArrayList<>();

        for (Ordine o : ordini) {
            OrdineBean bean = new OrdineBean();
            bean.setNumeroOrdine(o.getNumeroOrdine());
            bean.setClienteId(o.getClienteId());
            bean.setDataCreazione(o.getDataCreazione()); // Check if type matches. User bean has LocalDateTime, Model
                                                         // might have LocalDateTime.
            bean.setTotale(o.getTotale());
            if (o.getStato() != null) {
                bean.setStato(o.getStato().toString());
            }
            beans.add(bean);
        }
        return beans;
    }
}
