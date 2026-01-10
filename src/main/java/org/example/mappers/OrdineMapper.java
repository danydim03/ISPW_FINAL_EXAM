package org.example.mappers;

import org.example.model.ordine.Ordine;
import org.example.use_cases.crea_ordine.beans.OrdineBean;

/**
 * Mapper per convertire entity Ordine in OrdineBean.
 * Pattern: Pure Fabrication / Information Expert per la conversione
 */
public class OrdineMapper {

    private OrdineMapper() {
        // Utility class
    }

    /**
     * Converte un'entity Ordine in un OrdineBean.
     * Copia campi essenziali: numeroOrdine, clienteId, data, totale, stato.
     * 
     * @param ordine l'entity da convertire
     * @return bean popolato o null se ordine è null
     */
    public static OrdineBean toBean(Ordine ordine) {
        if (ordine == null) {
            return null;
        }

        OrdineBean bean = new OrdineBean();
        bean.setNumeroOrdine(ordine.getNumeroOrdine());
        bean.setClienteId(ordine.getClienteId());

        // Copia campi opzionali se presenti
        if (ordine.getDataCreazione() != null) {
            bean.setDataCreazione(ordine.getDataCreazione());
        }

        bean.setTotale(ordine.getTotale());

        if (ordine.getStato() != null) {
            bean.setStato(ordine.getStato().toString());
        }

        return bean;
    }
}
