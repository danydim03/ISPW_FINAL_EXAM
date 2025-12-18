package org.example.use_cases.crea_ordine.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Bean per il trasporto dei dati di un Ordine tra Boundary e Control.
 * Contiene i prodotti selezionati e il voucher applicato.
 */
public class OrdineBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long numeroOrdine;
    private List<FoodBean> prodotti;
    private VoucherBean voucherApplicato;
    private String clienteId;
    private java.time.LocalDateTime dataCreazione;
    private double totale;
    private String stato;

    public OrdineBean() {
        this.prodotti = new ArrayList<>();
    }

    public OrdineBean(Long numeroOrdine, String clienteId) {
        this();
        this.numeroOrdine = numeroOrdine;
        this.clienteId = clienteId;
    }

    // Getters e Setters
    public Long getNumeroOrdine() {
        return numeroOrdine;
    }

    public void setNumeroOrdine(Long numeroOrdine) {
        this.numeroOrdine = numeroOrdine;
    }

    public List<FoodBean> getProdotti() {
        return new ArrayList<>(prodotti);
    }

    public void setProdotti(List<FoodBean> prodotti) {
        this.prodotti = prodotti != null ? new ArrayList<>(prodotti) : new ArrayList<>();
    }

    public void aggiungiProdotto(FoodBean foodBean) {
        if (foodBean != null) {
            this.prodotti.add(foodBean);
        }
    }

    public void rimuoviProdotto(FoodBean foodBean) {
        this.prodotti.remove(foodBean);
    }

    public void rimuoviProdotto(int index) {
        if (index >= 0 && index < prodotti.size()) {
            this.prodotti.remove(index);
        }
    }

    public VoucherBean getVoucherApplicato() {
        return voucherApplicato;
    }

    public void setVoucherApplicato(VoucherBean voucherApplicato) {
        this.voucherApplicato = voucherApplicato;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public java.time.LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(java.time.LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public double getTotale() {
        return totale;
    }

    public void setTotale(double totale) {
        this.totale = totale;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public boolean hasVoucher() {
        return voucherApplicato != null && voucherApplicato.getCodice() != null
                && !voucherApplicato.getCodice().isEmpty();
    }

    public int getNumeroProdotti() {
        return prodotti.size();
    }

    @Override
    public String toString() {
        return String.format("OrdineBean{numero=%d, prodotti=%d, voucher=%s}",
                numeroOrdine, prodotti.size(),
                hasVoucher() ? voucherApplicato.getCodice() : "nessuno");
    }
}