package org.example.exceptions;

/**
 * Eccezione specifica per errori durante la creazione dell'ordine.
 */
public class CreaOrdineException extends HabibiException {

    public CreaOrdineException(String message) {
        super(message);
    }

    public CreaOrdineException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreaOrdineException(Throwable cause) {
        super(cause);
    }
}