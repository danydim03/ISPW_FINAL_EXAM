package org.example.use_cases.visualizza_ordini;

import org.example.use_cases.crea_ordine.beans.OrdineBean;
import org.example.enums.StatoOrdine;
import org.example.model.ordine.Ordine;

import java.util.ArrayList;
import java.util.List;

public class VisualizzaOrdiniFacade {

    private final VisualizzaOrdiniController controller = new VisualizzaOrdiniController();

    public List<OrdineBean> getOrdiniInCreazione() throws Exception {
        List<Ordine> ordini = controller.getOrdiniByStato(StatoOrdine.IN_CREAZIONE);
        List<OrdineBean> beans = new ArrayList<>();

        for (Ordine o : ordini) {
            OrdineBean bean = new OrdineBean();
            bean.setNumeroOrdine(o.getNumeroOrdine());
            bean.setClienteId(o.getClienteId());
            bean.setDataCreazione(o.getDataCreazione());
            bean.setTotale(o.getTotale());
            // Aggiungi altri campi se necessario
            beans.add(bean);
        }
        return beans;
    }

    public void impostaInConsegna(OrdineBean ordineBean) throws Exception {
        // Recupera l'ordine completo se necessario, o passa solo l'ID
        // Qui assumiamo che il controller possa gestire l'aggiornamento tramite ID o
        // oggetto
        controller.aggiornaStatoOrdine(ordineBean.getNumeroOrdine(), StatoOrdine.IN_CONSEGNA);
    }
}
