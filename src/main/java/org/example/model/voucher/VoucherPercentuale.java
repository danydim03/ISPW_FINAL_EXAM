package org.example.model.voucher;

import java.time.LocalDate;

/**
 * Voucher con sconto percentuale.
 * Esempio: 10% di sconto sul totale.
 */
public class VoucherPercentuale implements Voucher {

    private Long id;
    private String codice;
    private double percentuale;
    private LocalDate dataScadenza;
    private boolean attivo;

    /**
     * Costruttore semplice
     */
    public VoucherPercentuale(String codice, double percentuale) {
        this.codice = codice;
        this.percentuale = percentuale;
        this.attivo = true;
    }

    /**
     * Costruttore completo (per caricamento da DB)
     */
    public VoucherPercentuale(Long id, String codice, double percentuale, LocalDate dataScadenza) {
        this.id = id;
        this.codice = codice;
        this.percentuale = percentuale;
        this.dataScadenza = dataScadenza;
        this.attivo = true;
    }

    @Override
    public double calcolaSconto(double totaleOrdine) {
        if (!isValido()) {
            return 0;
        }
        return totaleOrdine * (percentuale / 100.0);
    }

    @Override
    public String getCodice() {
        return codice;
    }

    @Override
    public String getDescrizione() {
        return "Sconto del " + percentuale + "%";
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
        return "PERCENTUALE";
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

    public double getPercentuale() {
        return percentuale;
    }

    public void setPercentuale(double percentuale) {
        this.percentuale = percentuale;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }
}