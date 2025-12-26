package org.example.model.role.Cliente;

import org.example.model.role.AbstractRole;
import org.example.model.user.User;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends AbstractRole {
    private String ID;
    private List<String> voucherUtilizzati;
    private List<Long> ordiniEffettuati;
    private int punteggio;

    public Cliente(User user, String ID) {

        super(user); // puntatore all'oggetto User
        // il cliente viene puntato correttamente tramite puntatori e non tramite
        // l'utilizzo di chiavi esterne come nel modello relazionale.
        this.ID = ID;
        this.voucherUtilizzati = new ArrayList<>();
        this.ordiniEffettuati = new ArrayList<>();
        this.punteggio = 0;

    }

    @Override
    public Cliente getClienteRole() {
        return this;
    }

    public String getCodiceFiscale() {
        return ID;
    }

    public List<String> getVoucherUtilizzati() {
        return new ArrayList<>(voucherUtilizzati);
    }

    public void aggiungiVoucherUtilizzato(String voucherId) {
        this.voucherUtilizzati.add(voucherId);
    }

    public List<Long> getOrdiniEffettuati() {
        return new ArrayList<>(ordiniEffettuati);
    }

    public void aggiungiOrdine(Long ordineId) {
        this.ordiniEffettuati.add(ordineId);
    }

    public int getPunteggio() {
        return punteggio;
    }

    public void setPunteggio(int punteggio) {
        this.punteggio = punteggio;
    }

    public void aggiornaPunteggio(int punti) {
        this.punteggio += punti;
    }
}
