package org.example.model.ristoranti;

public class Habibi_takeaway extends Abstract_Habibi_Point {
    private boolean consegnaDisponibile;
    private double costoConsegna;

    public Habibi_takeaway(String id, String nome, String indirizzo, String telefono,
                           String orarioApertura, String orarioChiusura, boolean aperto,
                           boolean consegnaDisponibile, double costoConsegna) {
        super(id, nome, indirizzo, telefono, orarioApertura, orarioChiusura, aperto);
        this.consegnaDisponibile = consegnaDisponibile;
        this.costoConsegna = costoConsegna;
    }

    // Getters
    public boolean isConsegnaDisponibile() {
        return consegnaDisponibile;
    }


    public double getCostoConsegna() {
        return costoConsegna;
    }

    // Setters
    public void setConsegnaDisponibile(boolean consegnaDisponibile) {
        this.consegnaDisponibile = consegnaDisponibile;
    }


    public void setCostoConsegna(double costoConsegna) {
        this.costoConsegna = costoConsegna;
    }

    @Override
    public String getTipo() {
        return "TAKEAWAY";
    }
}