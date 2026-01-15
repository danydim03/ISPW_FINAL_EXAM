package org.example.events;

/**
 * Interfaccia Observer per il pattern Observer applicato agli eventi degli
 * ordini.
 * 
 * Questa interfaccia definisce il contratto per tutti i componenti del sistema
 * che desiderano essere notificati quando si verificano eventi relativi agli
 * ordini,
 * in particolare quando un ordine viene confermato da un Cliente.
 * 
 * <p>
 * Nel contesto dell'architettura BCE del sistema Habibi Shawarma, questa
 * interfaccia
 * permette di implementare il requisito della traccia d'esame che richiede una
 * <b>notifica attiva</b> dall'istanza dello Use Case "Crea Ordine" (avviato da
 * A1/Cliente)
 * verso l'attore A2 (Amministratore).
 * </p>
 * 
 * <h2>Pattern Observer (GoF)</h2>
 * <p>
 * Questa interfaccia rappresenta il ruolo di <b>Observer</b> nel pattern:
 * </p>
 * <ul>
 * <li><b>Subject</b>: {@link OrdineEventPublisher} - mantiene la lista degli
 * observer</li>
 * <li><b>Observer</b>: questa interfaccia - definisce il contratto di
 * notifica</li>
 * <li><b>ConcreteObserver</b>: {@code AdminNotificationService} - implementa la
 * logica</li>
 * </ul>
 * 
 * <h2>Principi GRASP applicati</h2>
 * <ul>
 * <li><b>Low Coupling</b>: il publisher non conosce le implementazioni
 * concrete,
 * dipende solo da questa interfaccia</li>
 * <li><b>Polymorphism</b>: permette diverse implementazioni di notifica
 * (GUI, CLI, log, email, etc.)</li>
 * <li><b>Protected Variations</b>: isola il sistema da cambiamenti nelle
 * strategie di notifica</li>
 * </ul>
 * 
 * @author Daniel Di Meo
 * @version 1.0
 * @since 2026-01-14
 * @see OrdineEvent
 * @see OrdineEventPublisher
 */
@FunctionalInterface
public interface OrdineEventListener {

    /**
     * Metodo callback invocato quando un ordine viene confermato nel sistema.
     * 
     * <p>
     * Questo metodo viene chiamato dal {@link OrdineEventPublisher} per ogni
     * observer registrato, immediatamente dopo che un Cliente ha confermato
     * il proprio ordine.
     * </p>
     * 
     * <p>
     * <b>Nota implementativa</b>: le implementazioni di questo metodo devono
     * essere thread-safe se il sistema supporta operazioni concorrenti. In
     * particolare,
     * per le implementazioni che aggiornano componenti UI JavaFX, è necessario
     * utilizzare {@code Platform.runLater()} per garantire l'esecuzione sul
     * thread dell'applicazione JavaFX.
     * </p>
     * 
     * @param event l'evento contenente i dettagli dell'ordine confermato
     *              (numero ordine, cliente, totale, timestamp)
     * @throws NullPointerException se event è null
     */
    void onOrdineConfermato(OrdineEvent event);
}
