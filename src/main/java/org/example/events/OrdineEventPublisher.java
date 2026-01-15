package org.example.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Subject Singleton del pattern Observer per la gestione degli eventi ordine.
 * 
 * Questa classe rappresenta il cuore del sistema di notifiche del progetto
 * Habibi Shawarma.
 * Implementa il ruolo di <b>Subject</b> nel pattern Observer (GoF), mantenendo
 * una lista
 * di observer ({@link OrdineEventListener}) e notificandoli quando si
 * verificano eventi
 * rilevanti nel dominio degli ordini.
 * 
 * <h2>Ruolo nell'Architettura</h2>
 * <p>
 * Nel contesto dell'architettura BCE, questa classe funge da punto di
 * coordinamento
 * tra i layer Control (dove avviene la conferma dell'ordine) e i componenti
 * Boundary
 * (dove l'Amministratore riceve la notifica). Questo design permette di
 * rispettare
 * il requisito della traccia che richiede una <b>notifica attiva</b> verso
 * l'attore A2.
 * </p>
 * 
 * <h2>Pattern Applicati</h2>
 * <ul>
 * <li><b>Singleton (GoF)</b>: garantisce un'unica istanza globale per
 * centralizzare
 * la gestione degli eventi. L'implementazione è thread-safe tramite
 * synchronized.</li>
 * <li><b>Observer/Subject (GoF)</b>: mantiene la lista di observer e implementa
 * i metodi per la registrazione, de-registrazione e notifica.</li>
 * </ul>
 * 
 * <h2>Principi GRASP</h2>
 * <ul>
 * <li><b>Low Coupling</b>: gli observer sono referenziati tramite l'interfaccia
 * {@link OrdineEventListener}, non tramite classi concrete</li>
 * <li><b>High Cohesion</b>: la classe ha una singola responsabilità ben
 * definita:
 * gestire la pubblicazione degli eventi ordine</li>
 * <li><b>Indirection</b>: agisce come intermediario tra il controller che
 * genera
 * l'evento e i componenti che lo consumano</li>
 * <li><b>Pure Fabrication</b>: non rappresenta un concetto del dominio reale,
 * ma è una classe di servizio creata per supportare l'architettura</li>
 * </ul>
 * 
 * <h2>Thread Safety</h2>
 * <p>
 * L'implementazione è thread-safe: il metodo {@code getInstance()} è
 * synchronized
 * e le operazioni sulla lista di listener sono protette da un lock implicito.
 * </p>
 * 
 * @author Daniel Di Meo
 * @version 1.0
 * @since 2026-01-14
 * @see OrdineEvent
 * @see OrdineEventListener
 */
public class OrdineEventPublisher {

    private static final Logger logger = Logger.getLogger(OrdineEventPublisher.class.getName());

    /** Istanza Singleton */
    private static OrdineEventPublisher instance;

    /** Lista degli observer registrati */
    private final List<OrdineEventListener> listeners;

    /** Coda di eventi pendenti per listener che si registrano in ritardo */
    private final Queue<OrdineEvent> pendingEvents;

    /**
     * Costruttore privato per implementare il pattern Singleton.
     * Inizializza la lista di listener vuota e la coda di eventi pendenti.
     */
    private OrdineEventPublisher() {
        this.listeners = new ArrayList<>();
        this.pendingEvents = new java.util.concurrent.ConcurrentLinkedQueue<>();
        logger.log(Level.INFO, "OrdineEventPublisher inizializzato");
    }

    /**
     * Restituisce l'istanza Singleton del publisher.
     * 
     * Questo metodo è thread-safe grazie alla keyword synchronized,
     * garantendo che solo un'istanza venga creata anche in contesti
     * multi-threaded.
     * 
     * @return l'unica istanza di OrdineEventPublisher
     */
    public static synchronized OrdineEventPublisher getInstance() {
        if (instance == null) {
            instance = new OrdineEventPublisher();
        }
        return instance;
    }

    /**
     * Registra un nuovo observer per ricevere notifiche sugli eventi ordine.
     * 
     * <p>
     * Gli observer registrati verranno notificati ogni volta che viene
     * chiamato il metodo {@link #notifyOrdineConfermato(OrdineEvent)}.
     * </p>
     * 
     * <p>
     * <b>Nota</b>: la registrazione è idempotente - registrare lo stesso
     * listener più volte non causa notifiche duplicate.
     * </p>
     * 
     * @param listener l'observer da registrare
     * @throws IllegalArgumentException se listener è null
     */
    public void addListener(OrdineEventListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Il listener non può essere null");
        }

        synchronized (listeners) {
            if (!listeners.contains(listener)) {
                listeners.add(listener);
                logger.log(Level.FINE, () -> "Listener aggiunto: " + listener.getClass().getSimpleName());

                // Notifica immediatamente gli eventi pendenti al nuovo listener
                if (!pendingEvents.isEmpty()) {
                    int count = pendingEvents.size();
                    logger.log(Level.INFO,
                            () -> "Consegna " + count + " eventi pendenti a " + listener.getClass().getSimpleName());
                    for (OrdineEvent event : pendingEvents) {
                        try {
                            listener.onOrdineConfermato(event);
                        } catch (Exception e) {
                            logger.log(Level.WARNING, "Errore durante la consegna evento pendente", e);
                        }
                    }
                }
            }
        }
    }

    /**
     * Rimuove un observer dalla lista di notifica.
     * 
     * <p>
     * Dopo la rimozione, l'observer non riceverà più notifiche
     * sugli eventi ordine.
     * </p>
     * 
     * @param listener l'observer da rimuovere
     */
    public void removeListener(OrdineEventListener listener) {
        synchronized (listeners) {
            boolean removed = listeners.remove(listener);
            if (removed) {
                logger.log(Level.FINE, () -> "Listener rimosso: " + listener.getClass().getSimpleName());
            }
        }
    }

    /**
     * Notifica tutti gli observer registrati che un ordine è stato confermato.
     * 
     * <p>
     * Questo metodo viene chiamato dal {@code CreaOrdineController} dopo
     * che un ordine è stato salvato con successo. Itera su tutti i listener
     * registrati e invoca il loro metodo {@code onOrdineConfermato}.
     * </p>
     * 
     * <p>
     * <b>Gestione errori</b>: se un listener solleva un'eccezione durante
     * la notifica, l'errore viene loggato ma la notifica continua verso
     * gli altri listener (fail-safe).
     * </p>
     * 
     * @param event l'evento contenente i dettagli dell'ordine confermato
     * @throws IllegalArgumentException se event è null
     */
    public void notifyOrdineConfermato(OrdineEvent event) {
        // Validazione dell'input
        // Controlla che l'evento non sia null
        if (event == null) {
            throw new IllegalArgumentException("L'evento non può essere null");
        }

        logger.log(Level.INFO, () -> "Pubblicazione evento: " + event);

        // Copia la lista dei listener per evitare ConcurrentModificationException
        List<OrdineEventListener> listenersCopy;
        synchronized (listeners) {
            // Crea una copia della lista per iterare in sicurezza
            listenersCopy = new ArrayList<>(listeners);
        }


        // Se non ci sono listener, accoda l'evento per consegna successiva
        // Questo supporta scenari in cui i listener si registrano in ritardo
        // rispetto alla generazione degli eventi
        if (listenersCopy.isEmpty()) {
            pendingEvents.add(event);
            int count = pendingEvents.size();
            logger.log(Level.INFO,
                    () -> "Nessun listener registrato. Evento accodato. Pendenti: " + count);
            return;
        }

        // Notifica tutti i listener registrati
        // Itera sulla copia per evitare problemi di concorrenza
        // Ogni listener viene notificato in un blocco try-catch
        // per garantire che un errore in uno non blocchi gli altri
        for (OrdineEventListener listener : listenersCopy) {
            try {
                // Invoca il callback del listener
                listener.onOrdineConfermato(event);
            } catch (Exception e) {
                // Fail-safe: un errore in un listener non deve bloccare gli altri
                logger.log(Level.WARNING,
                        "Errore durante la notifica al listener " + listener.getClass().getSimpleName(), e);
            }
        }

        logger.log(Level.INFO, () -> "Evento notificato a " + listenersCopy.size() + " listener");
    }

    /**
     * Restituisce il numero di listener attualmente registrati.
     * Utile per testing e debugging.
     * 
     * @return il numero di observer registrati
     */
    public int getListenerCount() {
        synchronized (listeners) {
            return listeners.size();
        }
    }

    /**
     * Rimuove tutti i listener registrati.
     * Utile per il reset del sistema durante i test.
     */
    public void clearListeners() {
        synchronized (listeners) {
            listeners.clear();
            logger.log(Level.INFO, "Tutti i listener sono stati rimossi");
        }
    }
}
