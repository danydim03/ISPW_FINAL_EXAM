package org.example.model.Food;


public abstract class Food {

    protected String descrizione = "Sconosciuto";

    public abstract double getCosto();

    public String getDescrizione() {
        return descrizione;
    }

    public abstract int getDurata(); // tempo di preparazione in minuti
}