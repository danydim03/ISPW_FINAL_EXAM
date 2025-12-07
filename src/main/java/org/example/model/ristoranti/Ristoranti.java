package org.example.model.ristoranti;

public class Ristoranti extends Abstract_Habibi_Point {
    private int numeroPosti;
    private boolean accettaPrenotazioni;

    public Ristoranti(String id, String nome, String indirizzo, String telefono,
                      String orarioApertura, String orarioChiusura, boolean aperto,
                      int numeroPosti, boolean accettaPrenotazioni) {
        super(id, nome, indirizzo, telefono, orarioApertura, orarioChiusura, aperto);
        this.numeroPosti = numeroPosti;
        this.accettaPrenotazioni = accettaPrenotazioni;
    }

    // Getters
    public int getNumeroPosti() {
        return numeroPosti;
    }

    public boolean isAccettaPrenotazioni() {
        return accettaPrenotazioni;
    }

    // Setters
    public void setNumeroPosti(int numeroPosti) {
        this.numeroPosti = numeroPosti;
    }

    public void setAccettaPrenotazioni(boolean accettaPrenotazioni) {
        this.accettaPrenotazioni = accettaPrenotazioni;
    }

    @Override
    public String getTipo() {
        return "RISTORANTE";
    }
}