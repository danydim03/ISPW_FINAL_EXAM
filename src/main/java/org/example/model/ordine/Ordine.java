package org.example.model.ordine;

import org.example.model.food.Food;
import org.example.model.voucher.NessunVoucher;
import org.example.model.voucher. Voucher;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import  org.example.enums.StatoOrdine;

/**
 * Entity che rappresenta un Ordine.
 * Contiene una lista di Food (con pattern Decorator per gli add-on)
 * e un Voucher opzionale (con pattern Strategy per lo sconto).
 */
public class Ordine {

    private Long numeroOrdine;
    private String clienteId;
    private List<Food> prodotti;
    private Voucher voucher;
    private LocalDateTime dataCreazione;
    private LocalDateTime dataConferma;
    private StatoOrdine stato;


    /**
     * Costruttore per nuovo ordine
     */
    public Ordine(String clienteId) {
        this.clienteId = clienteId;
        this.prodotti = new ArrayList<>();
        this.voucher = new NessunVoucher();
        this.dataCreazione = LocalDateTime.now();
        this.stato = StatoOrdine.IN_CREAZIONE;
    }

    /**
     * Costruttore completo (per caricamento da DB)
     */
    public Ordine(Long numeroOrdine, String clienteId, LocalDateTime dataCreazione,
                  LocalDateTime dataConferma, StatoOrdine stato) {
        this.numeroOrdine = numeroOrdine;
        this.clienteId = clienteId;
        this.prodotti = new ArrayList<>();
        this.voucher = new NessunVoucher();
        this.dataCreazione = dataCreazione;
        this.dataConferma = dataConferma;
        this. stato = stato;
    }

    // ==================== GESTIONE PRODOTTI ====================

    public void aggiungiProdotto(Food food) {
        if (food != null) {
            prodotti.add(food);
        }
    }

    public void rimuoviProdotto(Food food) {
        prodotti.remove(food);
    }

    public void rimuoviProdotto(int index) {
        if (index >= 0 && index < prodotti.size()) {
            prodotti.remove(index);
        }
    }

    public List<Food> getProdotti() {
        return new ArrayList<>(prodotti);
    }

    public void setProdotti(List<Food> prodotti) {
        this.prodotti = prodotti != null ? new ArrayList<>(prodotti) : new ArrayList<>();
    }

    public int getNumeroProdotti() {
        return prodotti.size();
    }

    public boolean isEmpty() {
        return prodotti.isEmpty();
    }

    // ==================== GESTIONE VOUCHER ====================

    public void applicaVoucher(Voucher voucher) {
        if (voucher != null && voucher.isValido()) {
            this. voucher = voucher;
        }
    }

    public void rimuoviVoucher() {
        this.voucher = new NessunVoucher();
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this. voucher = voucher != null ? voucher : new NessunVoucher();
    }

    public boolean hasVoucher() {
        return !(voucher instanceof NessunVoucher);
    }

    // ==================== CALCOLI ====================

    /**
     * Calcola il subtotale (senza sconto)
     */
    public double getSubtotale() {
        double totale = 0;
        for (Food f : prodotti) {
            totale += f.getCosto();
        }
        return totale;
    }

    /**
     * Calcola l'importo dello sconto
     */
    public double getSconto() {
        return voucher. calcolaSconto(getSubtotale());
    }

    /**
     * Calcola il totale finale (con sconto applicato)
     */
    public double getTotale() {
        return getSubtotale() - getSconto();
    }

    /**
     * Calcola la durata totale di preparazione
     */
    public int getDurataTotale() {
        int durata = 0;
        for (Food f : prodotti) {
            durata += f.getDurata();
        }
        return durata;
    }

    // ==================== GESTIONE STATO ====================

    public void conferma() {
        this.stato = StatoOrdine. CONFERMATO;
        this. dataConferma = LocalDateTime. now();
    }

    public void annulla() {
        this.stato = StatoOrdine.ANNULLATO;
    }

    public void iniziaPreparazione() {
        this.stato = StatoOrdine.IN_PREPARAZIONE;
    }

    public void segnaComePronto() {
        this. stato = StatoOrdine. PRONTO;
    }

    public void segnaConsegnato() {
        this. stato = StatoOrdine. CONSEGNATO;
    }

    public boolean isModificabile() {
        return stato == StatoOrdine.IN_CREAZIONE;
    }

    // ==================== GETTERS E SETTERS ====================

    public Long getNumeroOrdine() {
        return numeroOrdine;
    }

    public void setNumeroOrdine(Long numeroOrdine) {
        this. numeroOrdine = numeroOrdine;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this. clienteId = clienteId;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public LocalDateTime getDataConferma() {
        return dataConferma;
    }

    public void setDataConferma(LocalDateTime dataConferma) {
        this.dataConferma = dataConferma;
    }

    public StatoOrdine getStato() {
        return stato;
    }

    public void setStato(StatoOrdine stato) {
        this.stato = stato;
    }

    // ==================== UTILITY ====================

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Ordine)) return false;
        Ordine other = (Ordine) obj;
        return numeroOrdine != null && numeroOrdine.equals(other.numeroOrdine);
    }

    @Override
    public int hashCode() {
        return numeroOrdine != null ? numeroOrdine.hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.format("Ordine{numero=%d, cliente='%s', prodotti=%d, totale=%.2f, stato=%s}",
                numeroOrdine, clienteId, prodotti.size(), getTotale(), stato);
    }
}