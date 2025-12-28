package org.example.use_cases.crea_ordine.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Bean per il riepilogo dell'ordine da mostrare nella view.
 * Contiene i dati già calcolati (subtotale, sconto, totale).
 */
public class RiepilogoOrdineBean implements Serializable {

    private static final long serialVersionUID = 1L;

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

        public void setDescrizione(String descrizione) {
            this.descrizione = descrizione;
        }

        public double getPrezzo() {
            return prezzo;
        }

        public void setPrezzo(double prezzo) {
            this.prezzo = prezzo;
        }

        public int getDurata() {
            return durata;
        }

        public void setDurata(int durata) {
            this.durata = durata;
        }

        public String getPrezzoFormattato() {
            return String.format("€%.2f", prezzo);
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

    public void setRigheOrdine(List<RigaOrdineBean> righeOrdine) {
        this.righeOrdine = righeOrdine != null ? new ArrayList<>(righeOrdine) : new ArrayList<>();
    }

    public void aggiungiRiga(RigaOrdineBean riga) {
        if (riga != null) {
            this.righeOrdine.add(riga);
        }
    }

    public double getSubtotale() {
        return subtotale;
    }

    public void setSubtotale(double subtotale) {
        this.subtotale = subtotale;
    }

    public double getSconto() {
        return sconto;
    }

    public void setSconto(double sconto) {
        this.sconto = sconto;
    }

    public double getTotale() {
        return totale;
    }

    public void setTotale(double totale) {
        this.totale = totale;
    }

    public int getDurataTotale() {
        return durataTotale;
    }

    public void setDurataTotale(int durataTotale) {
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
        return String.format("€%.2f", subtotale);
    }

    public String getScontoFormattato() {
        return String.format("-€%.2f", sconto);
    }

    public String getTotaleFormattato() {
        return String.format("€%.2f", totale);
    }

    public String getDurataFormattata() {
        return String.format("%d min", durataTotale);
    }
}