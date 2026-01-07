package org.example.use_cases.crea_ordine.beans;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Bean per il trasporto dei dati di un Voucher tra Boundary e Control.
 * Segue il pattern BCE.
 */
public class VoucherBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String codice;
    private String descrizione;
    private String tipoVoucher; // "PERCENTUALE", "FISSO", "NESSUNO"
    private double valore; // percentuale o importo fisso
    private double minimoOrdine;
    private LocalDate dataScadenza;
    private boolean valido;

    public VoucherBean() {
    }

    public VoucherBean(String codice) {
        this.codice = codice;
    }

    public VoucherBean(Long id, String codice, String descrizione, String tipoVoucher,
            double valore, double minimoOrdine, LocalDate dataScadenza, boolean valido) {
        this.id = id;
        this.codice = codice;
        this.descrizione = descrizione;
        this.tipoVoucher = tipoVoucher;
        this.valore = valore;
        this.minimoOrdine = minimoOrdine;
        this.dataScadenza = dataScadenza;
        this.valido = valido;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getTipoVoucher() {
        return tipoVoucher;
    }

    public void setTipoVoucher(String tipoVoucher) {
        this.tipoVoucher = tipoVoucher;
    }

    public double getValore() {
        return valore;
    }

    public void setValore(double valore) {
        this.valore = valore;
    }

    public double getMinimoOrdine() {
        return minimoOrdine;
    }

    public void setMinimoOrdine(double minimoOrdine) {
        this.minimoOrdine = minimoOrdine;
    }

    public LocalDate getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(LocalDate dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public boolean isValido() {
        return valido;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }

    @Override
    public String toString() {
        return String.format("VoucherBean{codice='%s', tipo='%s', valore=%.2f, valido=%b}",
                codice, tipoVoucher, valore, valido);
    }
}