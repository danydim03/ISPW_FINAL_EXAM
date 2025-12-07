package org.example.model.ristoranti;

import java.util.Objects;

public abstract class Abstract_Habibi_Point {
    private String id;
    private String nome;
    private String indirizzo;
    private String telefono;
    private String orarioApertura;
    private String orarioChiusura;
    private boolean aperto;

    protected Abstract_Habibi_Point(String id, String nome, String indirizzo, String telefono,
                                    String orarioApertura, String orarioChiusura, boolean aperto) {
        this.id = id;
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.telefono = telefono;
        this.orarioApertura = orarioApertura;
        this.orarioChiusura = orarioChiusura;
        this.aperto = aperto;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getOrarioApertura() {
        return orarioApertura;
    }

    public String getOrarioChiusura() {
        return orarioChiusura;
    }

    public boolean isAperto() {
        return aperto;
    }

    // Setters
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setOrarioApertura(String orarioApertura) {
        this.orarioApertura = orarioApertura;
    }

    public void setOrarioChiusura(String orarioChiusura) {
        this.orarioChiusura = orarioChiusura;
    }

    public void setAperto(boolean aperto) {
        this.aperto = aperto;
    }

    // Metodi astratti da implementare nelle sottoclassi
    public abstract String getTipo();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Abstract_Habibi_Point that = (Abstract_Habibi_Point) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}