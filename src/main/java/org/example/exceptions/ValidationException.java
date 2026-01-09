package org.example.exceptions;

/**
 * Eccezione unchecked per errori di validazione sintattica nei Bean.
 * 
 * Secondo il pattern BCE, i Bean devono validare il formato sintattico dei dati
 * e lanciare eccezioni unchecked (fail fast) se i dati sono malformati.
 * 
 * Estende IllegalArgumentException perché rappresenta un errore di input
 * che il chiamante avrebbe dovuto prevenire.
 */
public class ValidationException extends IllegalArgumentException {

    /**
     * Costruttore con messaggio di errore.
     * 
     * @param message descrizione dell'errore di validazione
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Costruttore con messaggio e causa.
     * 
     * @param message descrizione dell'errore
     * @param cause   l'eccezione originale
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
