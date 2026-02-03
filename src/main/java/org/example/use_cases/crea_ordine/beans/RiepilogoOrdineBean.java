package org.example.use_cases.crea_ordine.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.example.exceptions.ValidationException;

/**
 * Bean per il riepilogo dell'ordine da mostrare nella view.
 * Contiene i dati già calcolati (subtotale, sconto, totale).
 * 
 * Include validazione sintattica nei setter (Fail Fast principle).
 */
public class RiepilogoOrdineBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String CURRENCY_FORMAT = "€%.2f";

    private Long numeroOrdine;
    private List<RigaOrdineBean> righeOrdine;
    private double subtotale;
    private double sconto;
    private double totale;
    private int durataTotale; // minuti
    private String codiceVoucher;
    private String descrizioneVoucher;
    private boolean voucherApplicato;

    public RiepilogoOrdineBean() {
        this.righeOrdine = new ArrayList<>();
    }

    // Classe interna per le righe dell'ordine
    public static class RigaOrdineBean implements Serializable {
        private static final long serialVersionUID = 1L;

        private String descrizione;
        private double prezzo;
        private int durata;

        public RigaOrdineBean() {
        }

        public RigaOrdineBean(String descrizione, double prezzo, int durata) {
            this.descrizione = descrizione;
            this.prezzo = prezzo;
            this.durata = durata;
        }

        public String getDescrizione() {
            return descrizione;
        }

        public int getDurata() {
            return durata;
        }

        public String getPrezzoFormattato() {
            return String.format(CURRENCY_FORMAT, prezzo);
        }
    }

    // Getters e Setters
    public Long getNumeroOrdine() {
        return numeroOrdine;
    }

    public void setNumeroOrdine(Long numeroOrdine) {
        this.numeroOrdine = numeroOrdine;
    }

    public List<RigaOrdineBean> getRigheOrdine() {
        return new ArrayList<>(righeOrdine);
    }

    public void aggiungiRiga(RigaOrdineBean riga) {
        if (riga != null) {
            this.righeOrdine.add(riga);
        }
    }

    /**
     * Imposta il subtotale dell'ordine.
     * 
     * @param subtotale il subtotale (non può essere negativo)
     * @throws ValidationException se il subtotale è negativo
     */
    public void setSubtotale(double subtotale) {
        if (subtotale < 0) {
            throw new ValidationException("Il subtotale non può essere negativo: " + subtotale);
        }
        this.subtotale = subtotale;
    }

    public double getSconto() {
        return sconto;
    }

    /**
     * Imposta lo sconto applicato.
     * 
     * @param sconto lo sconto (non può essere negativo)
     * @throws ValidationException se lo sconto è negativo
     */
    public void setSconto(double sconto) {
        if (sconto < 0) {
            throw new ValidationException("Lo sconto non può essere negativo: " + sconto);
        }
        this.sconto = sconto;
    }

    public double getTotale() {
        return totale;
    }

    /**
     * Imposta il totale dell'ordine.
     * 
     * @param totale il totale (non può essere negativo)
     * @throws ValidationException se il totale è negativo
     */
    public void setTotale(double totale) {
        if (totale < 0) {
            throw new ValidationException("Il totale non può essere negativo: " + totale);
        }
        this.totale = totale;
    }

    /**
     * Imposta la durata totale di preparazione.
     * 
     * @param durataTotale la durata in minuti (non può essere negativa)
     * @throws ValidationException se la durata è negativa
     */
    public void setDurataTotale(int durataTotale) {
        if (durataTotale < 0) {
            throw new ValidationException("La durata non può essere negativa: " + durataTotale);
        }
        this.durataTotale = durataTotale;
    }

    public String getCodiceVoucher() {
        return codiceVoucher;
    }

    public void setCodiceVoucher(String codiceVoucher) {
        this.codiceVoucher = codiceVoucher;
    }

    public String getDescrizioneVoucher() {
        return descrizioneVoucher;
    }

    public void setDescrizioneVoucher(String descrizioneVoucher) {
        this.descrizioneVoucher = descrizioneVoucher;
    }

    public boolean isVoucherApplicato() {
        return voucherApplicato;
    }

    public void setVoucherApplicato(boolean voucherApplicato) {
        this.voucherApplicato = voucherApplicato;
    }

    // Metodi di formattazione per la view
    public String getSubtotaleFormattato() {
        return String.format(CURRENCY_FORMAT, subtotale);
    }

    public String getScontoFormattato() {
        return String.format("-€%.2f", sconto);
    }

    public String getTotaleFormattato() {
        return String.format(CURRENCY_FORMAT, totale);
    }

    public String getDurataFormattata() {
        return String.format("%d min", durataTotale);
    }
}