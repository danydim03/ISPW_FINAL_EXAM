package org.example.model.ordine;


import org.example.model.Food.Food;
import org.example.model.voucher.NessunVoucher;
import org.example.model. voucher. Voucher;
import java.util.ArrayList;
import java.util.List;

public class Ordine {

    private List<Food> prodotti;
    private int numeroOrdine;
    private Voucher voucher; // <-- NUOVO: Strategy per lo sconto

    public Ordine(int numeroOrdine) {
        this.numeroOrdine = numeroOrdine;
        this.prodotti = new ArrayList<>();
        this.voucher = new NessunVoucher(); // Default: nessuno sconto
    }

    public void aggiungiProdotto(Food food) {
        prodotti.add(food);
    }

    public void rimuoviProdotto(Food food) {
        prodotti.remove(food);
    }

    // Totale SENZA sconto
    public double getSubtotale() {
        double totale = 0;
        for (Food f : prodotti) {
            totale += f.getCosto();
        }
        return totale;
    }

    // Importo dello sconto
    public double getSconto() {
        return voucher.calcolaSconto(getSubtotale());
    }

    // Totale FINALE con sconto applicato
    public double getTotale() {
        return getSubtotale() - getSconto();
    }

    public int getDurataTotale() {
        int durata = 0;
        for (Food f : prodotti) {
            durata += f. getDurata();
        }
        return durata;
    }

    // Metodi per il Voucher
    public void applicaVoucher(Voucher voucher) {
        if (voucher != null && voucher.isValido()) {
            this.voucher = voucher;
        }
    }

    public void rimuoviVoucher() {
        this.voucher = new NessunVoucher();
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public boolean hasVoucher() {
        return !(voucher instanceof NessunVoucher);
    }

    public List<Food> getProdotti() {
        return prodotti;
    }

    public int getNumeroOrdine() {
        return numeroOrdine;
    }
}