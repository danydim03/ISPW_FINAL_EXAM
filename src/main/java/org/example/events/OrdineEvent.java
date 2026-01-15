package org.example.events;

import java.time.LocalDateTime;

/**
 * Data Transfer Object immutabile che rappresenta l'evento di conferma di un
 * ordine.
 * 
 * Questo oggetto viene creato quando un Cliente conferma un ordine e viene
 * propagato attraverso il sistema di eventi per notificare gli observer
 * interessati
 * (in particolare l'Amministratore).
 * 
 * <p>
 * Caratteristiche del design:
 * </p>
 * <ul>
 * <li><b>Immutabilità</b>: tutti i campi sono final e non esistono setter,
 * garantendo thread-safety e prevenendo modifiche accidentali</li>
 * <li><b>Self-documenting</b>: il timestamp viene generato automaticamente
 * al momento della creazione</li>
 * </ul>
 * 
 * <p>
 * Pattern e Principi applicati:
 * </p>
 * <ul>
 * <li><b>Value Object</b>: oggetto immutabile senza identità propria</li>
 * <li><b>GRASP Information Expert</b>: contiene tutte le informazioni
 * necessarie per descrivere l'evento</li>
 * </ul>
 * 
 * @author Daniele Di Meo
 * @version 1.0
 * @since 2026-01-14
 */
public class OrdineEvent {

    /** Identificativo univoco dell'ordine confermato */
    private final Long numeroOrdine;

    /** Identificativo del cliente che ha effettuato l'ordine */
    private final String clienteId;

    /** Importo totale dell'ordine (inclusi eventuali sconti) */
    private final double totale;

    /** Timestamp della conferma dell'ordine */
    private final LocalDateTime timestamp;

    /**
     * Costruisce un nuovo evento di ordine confermato.
     * 
     * Il timestamp viene generato automaticamente al momento della creazione,
     * garantendo che rappresenti esattamente il momento in cui l'evento è stato
     * generato nel sistema.
     * 
     * @param numeroOrdine l'identificativo univoco dell'ordine
     * @param clienteId    l'identificativo del cliente che ha effettuato l'ordine
     * @param totale       l'importo totale dell'ordine
     * @throws IllegalArgumentException se numeroOrdine è null o clienteId è
     *                                  null/vuoto
     */
    public OrdineEvent(Long numeroOrdine, String clienteId, double totale) {
        if (numeroOrdine == null) {
            throw new IllegalArgumentException("Il numero ordine non può essere null");
        }
        if (clienteId == null || clienteId.trim().isEmpty()) {
            throw new IllegalArgumentException("Il clienteId non può essere null o vuoto");
        }

        this.numeroOrdine = numeroOrdine;
        this.clienteId = clienteId;
        this.totale = totale;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Restituisce l'identificativo univoco dell'ordine.
     * 
     * @return il numero dell'ordine
     */
    public Long getNumeroOrdine() {
        return numeroOrdine;
    }

    /**
     * Restituisce l'identificativo del cliente che ha effettuato l'ordine.
     * 
     * @return l'ID del cliente
     */
    public String getClienteId() {
        return clienteId;
    }

    /**
     * Restituisce l'importo totale dell'ordine.
     * 
     * @return il totale in euro
     */
    public double getTotale() {
        return totale;
    }

    /**
     * Restituisce il timestamp della conferma dell'ordine.
     * 
     * @return il momento esatto della conferma
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Restituisce una rappresentazione stringa dell'evento per logging/debug.
     * 
     * @return stringa descrittiva dell'evento
     */
    @Override
    public String toString() {
        return String.format("OrdineEvent{ordine=#%d, cliente='%s', totale=€%.2f, timestamp=%s}",
                numeroOrdine, clienteId, totale, timestamp);
    }
}
