package org.example.model.voucher;

import java.time.LocalDate;

/**
 * Voucher con sconto fisso.
 * Esempio: €5 di sconto se ordine >= €15.
 */
public class VoucherFisso implements Voucher {

    private Long id;
    private String codice;
    private double importoSconto;
    private double minimoOrdine;
    private LocalDate dataScadenza;
    private boolean attivo;

    /**
     * Costruttore semplice
     */
    public VoucherFisso(String codice, double importoSconto, double minimoOrdine) {
        this.codice = codice;
        this.importoSconto = importoSconto;
        this.minimoOrdine = minimoOrdine;
        this.attivo = true;
    }

    /**
     * Costruttore completo (per caricamento da DB)
     */
    public VoucherFisso(Long id, String codice, double importoSconto, double minimoOrdine, LocalDate dataScadenza) {
        this.id = id;
        this.codice = codice;
        this.importoSconto = importoSconto;
        this.minimoOrdine = minimoOrdine;
        this.dataScadenza = dataScadenza;
        this.attivo = true;
    }

    @Override
    public double calcolaSconto(double totaleOrdine) {
        if (!isValido()) {
            return 0;
        }
        if (totaleOrdine < minimoOrdine) {
            return 0;
        }
        return Math.min(importoSconto, totaleOrdine);
    }

    @Override
    public String getCodice() {
        return codice;
    }

    @Override
    public String getDescrizione() {
        if (minimoOrdine > 0) {
            return "Sconto di €" + importoSconto + " (minimo ordine €" + minimoOrdine + ")";
        }
        return "Sconto di €" + importoSconto;
    }

    @Override
    public boolean isValido() {
        return attivo && (dataScadenza == null || !LocalDate.now().isAfter(dataScadenza));
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getTipoVoucher() {
        return "FISSO";
    }

    @Override
    public LocalDate getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(LocalDate dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    @Override
    public boolean isAttivo() {
        return attivo;
    }

    @Override
    public void setAttivo(boolean attivo) {
        this.attivo = attivo;
    }

    public double getImportoSconto() {
        return importoSconto;
    }

    public void setImportoSconto(double importoSconto) {
        this.importoSconto = importoSconto;
    }

    public double getMinimoOrdine() {
        return minimoOrdine;
    }

    public void setMinimoOrdine(double minimoOrdine) {
        this.minimoOrdine = minimoOrdine;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }
}