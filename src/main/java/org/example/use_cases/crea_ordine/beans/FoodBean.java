package org.example.use_cases.crea_ordine.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Bean per il trasporto dei dati di un Food tra Boundary e Control. 
 * Segue il pattern BCE - i dati dalla grafica vengono incapsulati in questo bean. 
 */
public class FoodBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String descrizione;
    private double costo;
    private int durata;
    private String tipo; // "BASE" o "ADDON"
    private String classe; // Nome della classe concreta (es. "PaninoDonerKebab")
    private List<String> addOnSelezionati; // Lista dei nomi degli addon da applicare
    
    public FoodBean() {
        this.addOnSelezionati = new ArrayList<>();
    }
    
    public FoodBean(Long id, String descrizione, double costo, int durata, String tipo, String classe) {
        this();
        this.id = id;
        this.descrizione = descrizione;
        this.costo = costo;
        this. durata = durata;
        this.tipo = tipo;
        this.classe = classe;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getDescrizione() {
        return descrizione;
    }
    
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    
    public double getCosto() {
        return costo;
    }
    
    public void setCosto(double costo) {
        this.costo = costo;
    }
    
    public int getDurata() {
        return durata;
    }
    
    public void setDurata(int durata) {
        this. durata = durata;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String getClasse() {
        return classe;
    }
    
    public void setClasse(String classe) {
        this.classe = classe;
    }
    
    public List<String> getAddOnSelezionati() {
        return new ArrayList<>(addOnSelezionati);
    }
    
    public void setAddOnSelezionati(List<String> addOnSelezionati) {
        this.addOnSelezionati = addOnSelezionati != null ? new ArrayList<>(addOnSelezionati) : new ArrayList<>();
    }
    
    public void aggiungiAddOn(String addOnClasse) {
        if (addOnClasse != null && !addOnClasse.isEmpty()) {
            this.addOnSelezionati. add(addOnClasse);
        }
    }
    
    public void rimuoviAddOn(String addOnClasse) {
        this.addOnSelezionati. remove(addOnClasse);
    }
    
    @Override
    public String toString() {
        return String.format("FoodBean{id=%d, descrizione='%s', costo=%.2f, tipo='%s', addOns=%s}",
                id, descrizione, costo, tipo, addOnSelezionati);
    }
}