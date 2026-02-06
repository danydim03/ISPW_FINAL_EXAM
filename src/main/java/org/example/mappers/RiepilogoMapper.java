package org.example.mappers;

import org.example.model.food.Food;
import org.example.model.ordine.Ordine;
import org.example.model.voucher.Voucher;
import org.example.use_cases.crea_ordine.beans.RiepilogoOrdineBean;
import org.example.use_cases.crea_ordine.beans.RiepilogoOrdineBean.RigaOrdineBean;

/**
 * Mapper per convertire un Ordine Entity in RiepilogoOrdineBean.
 * 
 * <p>
 * <b>BCE/MVC</b>: i Mapper sono utility del layer Control.
 * Convertono Entity in Bean (DTO) per il layer Boundary.
 * NON contengono logica di business, solo mapping.
 * </p>
 * 
 * <p>
 * Questo mapper migliora la High Cohesion del Controller,
 * estraendo la logica di conversione in una classe dedicata.
 * </p>
 */
public class RiepilogoMapper {

    private RiepilogoMapper() {
        // Utility class - costruttore privato
    }

    /**
     * Converte un Ordine Entity in RiepilogoOrdineBean.
     * 
     * @param ordine l'ordine da convertire
     * @return RiepilogoOrdineBean con tutti i dati calcolati
     */
    public static RiepilogoOrdineBean toBean(Ordine ordine) {
        if (ordine == null) {
            return null;
        }

        RiepilogoOrdineBean riepilogo = new RiepilogoOrdineBean();
        riepilogo.setNumeroOrdine(ordine.getNumeroOrdine());

        // Aggiungi le righe dei prodotti
        for (Food food : ordine.getProdotti()) {
            RigaOrdineBean riga = new RigaOrdineBean(
                    food.getDescrizione(),
                    food.getCosto(),
                    food.getDurata());
            riepilogo.aggiungiRiga(riga);
        }

        // Calcola i totali
        riepilogo.setSubtotale(ordine.getSubtotale());
        riepilogo.setSconto(ordine.getSconto());
        riepilogo.setTotale(ordine.getTotale());
        riepilogo.setDurataTotale(ordine.getDurataTotale());

        // Info voucher
        Voucher v = ordine.getVoucher();
        if (v != null) {
            riepilogo.setVoucherApplicato(true);
            riepilogo.setCodiceVoucher(v.getCodice());
            riepilogo.setDescrizioneVoucher(v.getDescrizione());
        } else {
            riepilogo.setVoucherApplicato(false);
        }

        return riepilogo;
    }
}
