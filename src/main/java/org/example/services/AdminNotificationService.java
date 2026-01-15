package org.example.services;

import javafx.application.Platform;
import org.example.events.OrdineEvent;
import org.example.events.OrdineEventListener;
import org.example.events.OrdineEventPublisher;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servizio Singleton per la gestione delle notifiche all'Amministratore.
 * 
 * Questa classe implementa il pattern Observer come <b>Concrete Observer</b>,
 * ricevendo gli eventi di conferma ordine e gestendoli per l'interfaccia
 * dell'Amministratore. Rappresenta il punto di contatto tra il sistema di
 * eventi del dominio e l'interfaccia utente dell'attore A2 (Amministratore).
 * 
 * <h2>Ruolo nell'Architettura BCE</h2>
 * <p>
 * Nel contesto dell'architettura a tre livelli:
 * </p>
 * <ul>
 * <li>Riceve eventi dal layer <b>Control</b> (via
 * {@link OrdineEventPublisher})</li>
 * <li>Comunica con il layer <b>Boundary</b> (AdminHomepageGUIController)</li>
 * <li>Non interagisce direttamente con le <b>Entity</b></li>
 * </ul>
 * 
 * <h2>Rispetto del Requisito della Traccia</h2>
 * <p>
 * Questa classe è fondamentale per rispettare il requisito:
 * </p>
 * <blockquote>
 * "Al termine di queste interazioni UC1 <b>notifica</b> A2 che qualcosa nel
 * sistema
 * si è evoluto. Per notifica, si intende una azione <b>ATTIVA</b> da parte
 * dell'istanza
 * di UC1 verso A2, <b>NON</b> la possibilità di A2 di consultare gli effetti
 * una volta
 * acceduto al sistema"
 * </blockquote>
 * 
 * <h2>Pattern Applicati</h2>
 * <ul>
 * <li><b>Singleton (GoF)</b>: unica istanza condivisa tra tutti i
 * componenti</li>
 * <li><b>Observer (GoF) - Concrete Observer</b>: implementa
 * {@link OrdineEventListener}</li>
 * <li><b>Producer-Consumer</b>: mantiene una coda di notifiche pendenti</li>
 * </ul>
 * 
 * <h2>Thread Safety</h2>
 * <p>
 * La classe è progettata per essere thread-safe:
 * </p>
 * <ul>
 * <li>Usa {@link ConcurrentLinkedQueue} per la coda delle notifiche</li>
 * <li>La callback UI viene eseguita sul JavaFX Application Thread via
 * {@code Platform.runLater()}</li>
 * <li>Il metodo {@code getInstance()} è synchronized</li>
 * </ul>
 * 
 * <h2>Principi GRASP</h2>
 * <ul>
 * <li><b>Low Coupling</b>: comunica con l'UI solo tramite callback
 * Runnable</li>
 * <li><b>High Cohesion</b>: responsabilità singola di gestione notifiche
 * admin</li>
 * <li><b>Controller</b>: coordina il flusso tra eventi e UI</li>
 * <li><b>Indirection</b>: disaccoppia il publisher dall'UI</li>
 * </ul>
 * 
 * @author Daniel Di Meo
 * @version 1.0
 * @since 2026-01-14
 * @see OrdineEvent
 * @see OrdineEventListener
 * @see OrdineEventPublisher
 */
public class AdminNotificationService implements OrdineEventListener {

    private static final Logger logger = Logger.getLogger(AdminNotificationService.class.getName());

    /** Istanza Singleton */
    private static AdminNotificationService instance;

    /** Coda thread-safe delle notifiche pendenti */
    private final Queue<OrdineEvent> pendingNotifications;

    /** Callback per aggiornare l'interfaccia utente */
    private Runnable onNewNotification;

    /** Flag per indicare se il servizio è registrato come listener */
    private boolean registered = false;

    /**
     * Costruttore privato per implementare il pattern Singleton.
     */
    private AdminNotificationService() {
        this.pendingNotifications = new ConcurrentLinkedQueue<>();
        logger.log(Level.INFO, "AdminNotificationService inizializzato");
    }

    /**
     * Restituisce l'istanza Singleton del servizio.
     * 
     * @return l'unica istanza di AdminNotificationService
     */
    public static synchronized AdminNotificationService getInstance() {
        if (instance == null) {
            instance = new AdminNotificationService();
        }
        return instance;
    }

    /**
     * Registra questo servizio come listener presso il publisher.
     * 
     * <p>
     * Questo metodo deve essere chiamato all'inizializzazione dell'interfaccia
     * dell'Amministratore per attivare la ricezione delle notifiche.
     * </p>
     * 
     * <p>
     * La registrazione è idempotente: chiamate multiple non causano
     * registrazioni duplicate.
     * </p>
     */
    public void register() {
        if (!registered) {
            OrdineEventPublisher.getInstance().addListener(this);
            registered = true;
            logger.log(Level.INFO, "AdminNotificationService registrato come listener");
        }
    }

    /**
     * De-registra questo servizio dal publisher.
     * 
     * <p>
     * Dopo la de-registrazione, il servizio non riceverà più notifiche
     * sugli ordini confermati.
     * </p>
     */
    public void unregister() {
        if (registered) {
            OrdineEventPublisher.getInstance().removeListener(this);
            registered = false;
            logger.log(Level.INFO, "AdminNotificationService de-registrato");
        }
    }

    /**
     * Callback del pattern Observer, invocato quando un ordine viene confermato.
     * 
     * <p>
     * Questo metodo:
     * </p>
     * <ol>
     * <li>Aggiunge l'evento alla coda delle notifiche pendenti</li>
     * <li>Se è stato impostato un callback UI, lo esegue sul JavaFX Application
     * Thread</li>
     * </ol>
     * 
     * <p>
     * <b>Thread Safety</b>: la coda è thread-safe e il callback viene eseguito
     * sul thread corretto tramite {@code Platform.runLater()}.
     * </p>
     * 
     * @param event l'evento di conferma ordine
     */
    @Override
    public void onOrdineConfermato(OrdineEvent event) {
        if (event == null) {
            logger.log(Level.WARNING, "Ricevuto evento null, ignorato");
            return;
        }

        // Aggiungi alla coda delle notifiche pendenti
        pendingNotifications.add(event);
        logger.log(Level.INFO, () -> "Nuova notifica ordine: " + event);

        // Notifica l'UI se è stato impostato un callback
        if (onNewNotification != null) {
            // Platform.runLater garantisce l'esecuzione sul JavaFX Application Thread
            Platform.runLater(onNewNotification);
        }
    }

    /**
     * Imposta il callback da invocare quando arriva una nuova notifica.
     * 
     * <p>
     * Il callback viene eseguito sul JavaFX Application Thread,
     * quindi è sicuro aggiornare componenti UI al suo interno.
     * </p>
     * 
     * @param callback il Runnable da eseguire alla ricezione di una notifica
     */
    public void setOnNewNotification(Runnable callback) {
        this.onNewNotification = callback;
        logger.log(Level.FINE, "Callback UI impostato");
    }

    /**
     * Restituisce la coda delle notifiche pendenti.
     * 
     * <p>
     * <b>Nota</b>: la coda restituita è thread-safe. Le notifiche
     * possono essere consumate con {@link Queue#poll()} per rimuoverle
     * dalla coda.
     * </p>
     * 
     * @return la coda delle notifiche non ancora processate
     */
    public Queue<OrdineEvent> getPendingNotifications() {
        return pendingNotifications;
    }

    /**
     * Restituisce il numero di notifiche pendenti.
     * 
     * @return il conteggio delle notifiche non ancora consumate
     */
    public int getNotificationCount() {
        return pendingNotifications.size();
    }

    /**
     * Consuma e restituisce la prossima notifica dalla coda.
     * 
     * <p>
     * La notifica viene rimossa dalla coda. Se la coda è vuota,
     * restituisce null.
     * </p>
     * 
     * @return la prossima notifica, o null se non ce ne sono
     */
    public OrdineEvent consumeNotification() {
        return pendingNotifications.poll();
    }

    /**
     * Consuma tutte le notifiche pendenti senza restituirle.
     * 
     * <p>
     * Utile per "marcare come lette" tutte le notifiche.
     * </p>
     */
    public void clearNotifications() {
        int count = pendingNotifications.size();
        pendingNotifications.clear();
        logger.log(Level.INFO, () -> "Cancellate " + count + " notifiche");
    }

    /**
     * Verifica se ci sono notifiche pendenti.
     * 
     * @return true se ci sono notifiche non lette
     */
    public boolean hasNotifications() {
        return !pendingNotifications.isEmpty();
    }

    /**
     * Restituisce la notifica più recente senza rimuoverla dalla coda.
     * 
     * @return l'ultima notifica ricevuta, o null se la coda è vuota
     */
    public OrdineEvent peekLatestNotification() {
        return pendingNotifications.peek();
    }
}
