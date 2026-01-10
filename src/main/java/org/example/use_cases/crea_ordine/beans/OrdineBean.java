package org.example.use_cases.crea_ordine.beans;

import java.io.Serializable;
import org.example.exceptions.ValidationException;

/**
 * Bean per il trasporto dei dati essenziali di un Ordine.
 * Contiene i dati identificativi, stato e totale.
 * 
 * Include validazione sintattica nei setter (Fail Fast principle).
 */
public class OrdineBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long numeroOrdine;
    private String clienteId;
    private java.time.LocalDateTime dataCreazione;
    private double totale;
    private String stato;

    public OrdineBean() {
    }

    public OrdineBean(Long numeroOrdine, String clienteId) {
        this();
        this.numeroOrdine = numeroOrdine;
        setClienteId(clienteId);
    }

    // Getters e Setters con validazione sintattica

    public Long getNumeroOrdine() {
        return numeroOrdine;
    }

    public void setNumeroOrdine(Long numeroOrdine) {
        this.numeroOrdine = numeroOrdine;
    }

    public String getClienteId() {
        return clienteId;
    }

    /**
     * Imposta l'ID del cliente.
     * 
     * @param clienteId l'ID del cliente (non può essere null o vuoto)
     * @throws ValidationException se l'ID è null o vuoto
     */
    public void setClienteId(String clienteId) {
        if (clienteId == null || clienteId.trim().isEmpty()) {
            throw new ValidationException("L'ID cliente non può essere vuoto");
        }
        this.clienteId = clienteId;
    }

    public java.time.LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(java.time.LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public double getTotale() {
        return totale;
    }

    /**
     * Imposta il totale dell'ordine.
     * 
     * @param totale il totale (non può essere negativo)
     * @throws ValidationException se il totale è negativo
     */
    public void setTotale(double totale) {
        if (totale < 0) {
            throw new ValidationException("Il totale dell'ordine non può essere negativo: " + totale);
        }
        this.totale = totale;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    @Override
    public String toString() {
        return String.format("OrdineBean{numero=%d, cliente=%s, totale=%.2f, stato=%s}",
                numeroOrdine, clienteId, totale, stato);
    }
}