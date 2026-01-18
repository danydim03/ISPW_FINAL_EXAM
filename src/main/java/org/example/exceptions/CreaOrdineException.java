package org.example.exceptions;

/**
 * Eccezione specifica per errori durante la creazione dell'ordine.
 */
public class CreaOrdineException extends HabibiException {

    /**
     * Costruttore per eccezioni con messaggio.
     *
     * @param message il messaggio di errore
     */
    public CreaOrdineException(String message) {
        super(message);
    }

    /**
     * Costruttore per eccezioni con messaggio e causa.
     *
     * @param message il messaggio di errore
     * @param cause   l'eccezione sottostante
     */
    public CreaOrdineException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Costruttore per eccezioni con causa.
     *
     * @param cause l'eccezione sottostante
     */
    public CreaOrdineException(Throwable cause) {
        super(cause);
    }
}