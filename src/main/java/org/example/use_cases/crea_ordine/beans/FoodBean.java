package org.example.use_cases.crea_ordine.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.example.exceptions.ValidationException;

/**
 * Bean per il trasporto dei dati di un Food tra Boundary e Control.
 * Segue il pattern BCE - i dati dalla grafica vengono incapsulati in questo
 * bean.
 * 
 * Include validazione sintattica nei setter (Fail Fast principle).
 */
public class FoodBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Set<String> TIPI_VALIDI = Set.of("BASE", "ADDON");

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
        setDescrizione(descrizione);
        setCosto(costo);
        setDurata(durata);
        setTipo(tipo);
        setClasse(classe);
    }

    // Getters e Setters con validazione sintattica

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Imposta la descrizione del prodotto.
     * 
     * @param descrizione la descrizione (non può essere null o vuota)
     * @throws ValidationException se la descrizione è null o vuota
     */
    public void setDescrizione(String descrizione) {
        if (descrizione == null || descrizione.trim().isEmpty()) {
            throw new ValidationException("La descrizione del prodotto non può essere vuota");
        }
        this.descrizione = descrizione;
    }

    public double getCosto() {
        return costo;
    }

    /**
     * Imposta il costo del prodotto.
     * 
     * @param costo il costo (non può essere negativo)
     * @throws ValidationException se il costo è negativo
     */
    public void setCosto(double costo) {
        if (costo < 0) {
            throw new ValidationException("Il costo non può essere negativo: " + costo);
        }
        this.costo = costo;
    }

    public int getDurata() {
        return durata;
    }

    /**
     * Imposta la durata di preparazione.
     * 
     * @param durata la durata in minuti (non può essere negativa)
     * @throws ValidationException se la durata è negativa
     */
    public void setDurata(int durata) {
        if (durata < 0) {
            throw new ValidationException("La durata non può essere negativa: " + durata);
        }
        this.durata = durata;
    }

    public String getTipo() {
        return tipo;
    }

    /**
     * Imposta il tipo di prodotto.
     * 
     * @param tipo il tipo (deve essere "BASE" o "ADDON")
     * @throws ValidationException se il tipo non è valido
     */
    public void setTipo(String tipo) {
        if (tipo == null || !TIPI_VALIDI.contains(tipo.toUpperCase())) {
            throw new ValidationException("Tipo prodotto non valido: " + tipo + ". Valori ammessi: BASE, ADDON");
        }
        this.tipo = tipo.toUpperCase();
    }

    public String getClasse() {
        return classe;
    }

    /**
     * Imposta la classe concreta del prodotto.
     * 
     * @param classe il nome della classe (non può essere null o vuoto)
     * @throws ValidationException se la classe è null o vuota
     */
    public void setClasse(String classe) {
        if (classe == null || classe.trim().isEmpty()) {
            throw new ValidationException("La classe del prodotto non può essere vuota");
        }
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
            this.addOnSelezionati.add(addOnClasse);
        }
    }

    public void rimuoviAddOn(String addOnClasse) {
        this.addOnSelezionati.remove(addOnClasse);
    }

    @Override
    public String toString() {
        return String.format("FoodBean{id=%d, descrizione='%s', costo=%.2f, tipo='%s', addOns=%s}",
                id, descrizione, costo, tipo, addOnSelezionati);
    }
}