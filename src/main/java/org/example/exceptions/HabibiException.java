package org.example.exceptions;

/**
 * Classe base per tutte le eccezioni personalizzate dell'applicazione Habibi
 * Shawarma.
 * 
 * Questa gerarchia permette di:
 * - Catturare tutte le eccezioni dell'applicazione con un singolo catch
 * - Garantire che tutte le eccezioni supportino il chaining (preservazione
 * causa)
 * - Mantenere coerenza semantica nel sistema di exception handling
 * 
 * NOTA: Estende Exception (checked) per forzare la gestione esplicita.
 */
public abstract class HabibiException extends Exception {

    /**
     * Costruttore con solo messaggio.
     * Usare quando l'eccezione è l'origine dell'errore.
     * 
     * @param message descrizione dell'errore
     */
    protected HabibiException(String message) {
        super(message);
    }

    /**
     * Costruttore con messaggio e causa.
     * FONDAMENTALE per il chaining: preserva lo stack trace originale.
     * 
     * @param message descrizione dell'errore
     * @param cause   l'eccezione originale che ha causato questo errore
     */
    protected HabibiException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Costruttore con solo causa.
     * Utile quando si vuole solo wrappare un'eccezione senza aggiungere contesto.
     * 
     * @param cause l'eccezione originale
     */
    protected HabibiException(Throwable cause) {
        super(cause);
    }
}
