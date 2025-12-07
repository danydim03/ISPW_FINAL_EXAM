package org.example.model.food;

public abstract class Food {

    protected Long id;
    protected String descrizione = "Sconosciuto";
    protected String tipo; // "BASE" o "ADDON"

    protected Food() {
    }

    protected Food(Long id, String descrizione, String tipo) {
        this.id = id;
        this.descrizione = descrizione;
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public abstract double getCosto();

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public abstract int getDurata(); // tempo di preparazione in minuti

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}