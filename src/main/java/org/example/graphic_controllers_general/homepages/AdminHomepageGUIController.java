package org.example.graphic_controllers_general.homepages;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.example.PageNavigationController;
import org.example.events.OrdineEvent;
import org.example.services.AdminNotificationService;
import org.example.session_manager.SessionManager;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller grafico per la homepage dell'Amministratore.
 * 
 * <p>
 * Questo controller implementa il lato Boundary del pattern BCE per l'attore
 * Amministratore (A2). Oltre alla navigazione tra le funzionalità admin,
 * gestisce la ricezione e visualizzazione delle <b>notifiche attive</b>
 * provenienti dallo Use Case "Crea Ordine".
 * </p>
 * 
 * <h2>Ruolo nel Pattern Observer</h2>
 * <p>
 * Questo controller si integra con {@link AdminNotificationService} per
 * ricevere
 * notifiche in tempo reale quando un Cliente (A1) conferma un ordine. Questo
 * implementa il requisito della traccia d'esame che richiede una "notifica
 * attiva"
 * da UC1 verso A2.
 * </p>
 * 
 * <h2>Flusso di Notifica</h2>
 * 
 * <pre>
 * Cliente conferma ordine
 *       ↓
 * CreaOrdineController.confermaOrdine()
 *       ↓
 * OrdineEventPublisher.notifyOrdineConfermato()
 *       ↓
 * AdminNotificationService.onOrdineConfermato()
 *       ↓
 * Platform.runLater() → aggiornaNotifiche()
 *       ↓
 * [Popup + Badge UI]
 * </pre>
 * 
 * @author Daniel Di Meo
 * @version 2.0
 * @since 2026-01-14
 */
public class AdminHomepageGUIController implements Initializable {

    private static final Logger logger = Logger.getLogger(AdminHomepageGUIController.class.getName());

    /** Label per mostrare il badge con il conteggio delle notifiche */
    @FXML
    private Label labelNotifiche;

    /** Container per il badge notifiche (opzionale, per styling) */
    @FXML
    private HBox panelNotifiche;

    /**
     * Inizializza il controller e registra il servizio di notifica.
     * 
     * <p>
     * All'inizializzazione:
     * </p>
     * <ol>
     * <li>Registra {@link AdminNotificationService} come listener degli eventi
     * ordine</li>
     * <li>Imposta il callback per aggiornare l'UI quando arrivano notifiche</li>
     * <li>Controlla se ci sono notifiche pendenti da sessioni precedenti</li>
     * </ol>
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Registra il servizio di notifica come listener
        AdminNotificationService notificationService = AdminNotificationService.getInstance();
        notificationService.register();

        // Imposta il callback per aggiornare l'UI quando arrivano nuove notifiche
        notificationService.setOnNewNotification(this::onNuovaNotificaRicevuta);

        // Controlla notifiche pendenti
        aggiornaContatoreBadge();

        logger.log(Level.INFO, "AdminHomepageGUIController inizializzato con supporto notifiche");
    }

    /**
     * Callback invocato quando arriva una nuova notifica ordine.
     * 
     * <p>
     * Questo metodo viene eseguito sul JavaFX Application Thread
     * (garantito da Platform.runLater in AdminNotificationService).
     * </p>
     * 
     * <p>
     * Azioni eseguite:
     * </p>
     * <ul>
     * <li>Aggiorna il badge con il conteggio notifiche</li>
     * <li>Mostra un popup informativo all'Amministratore</li>
     * </ul>
     */
    private void onNuovaNotificaRicevuta() {
        logger.log(Level.INFO, "Nuova notifica ricevuta dall'Amministratore");

        // Aggiorna il badge
        aggiornaContatoreBadge();

        // Mostra popup per la notifica più recente
        OrdineEvent event = AdminNotificationService.getInstance().peekLatestNotification();
        if (event != null) {
            mostraNotificaPopup(event);
        }
    }

    /**
     * Aggiorna il badge delle notifiche con il conteggio corrente.
     */
    private void aggiornaContatoreBadge() {
        int count = AdminNotificationService.getInstance().getNotificationCount();

        if (labelNotifiche != null) {
            if (count > 0) {
                labelNotifiche.setText("🔔 " + count + " nuovo/i ordine/i!");
                labelNotifiche.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-font-size: 14px;");
                labelNotifiche.setVisible(true);
            } else {
                labelNotifiche.setText("");
                labelNotifiche.setVisible(false);
            }
        }

        if (panelNotifiche != null) {
            panelNotifiche.setVisible(count > 0);
            panelNotifiche.setManaged(count > 0);
        }
    }

    /**
     * Mostra un popup informativo per la notifica ordine.
     * 
     * @param event l'evento di conferma ordine
     */
    private void mostraNotificaPopup(OrdineEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("🔔 Nuovo Ordine Ricevuto!");
        alert.setHeaderText("Ordine #" + event.getNumeroOrdine());
        alert.setContentText(String.format(
                "Cliente: %s%nTotale: €%.2f%nOra: %s",
                event.getClienteId(),
                event.getTotale(),
                event.getTimestamp().toLocalTime().toString()));
        alert.show(); // Non bloccante per non interrompere il flusso

        logger.log(Level.INFO, () -> "Popup notifica mostrato per ordine #" + event.getNumeroOrdine());
    }

    /**
     * Gestisce il click sul pulsante "Visualizza Ordini".
     * Consuma tutte le notifiche pendenti e naviga alla lista ordini.
     */
    @FXML
    void handleVisualizzaOrdini(ActionEvent event) {
        logger.log(Level.INFO, "Navigazione verso Visualizza Ordini");

        // Consuma le notifiche quando l'admin va a vedere gli ordini
        AdminNotificationService.getInstance().clearNotifications();
        aggiornaContatoreBadge();

        PageNavigationController.getInstance().navigateTo("visualizza_ordini");
    }

    /**
     * Gestisce il click sul pulsante "Crea Voucher".
     */
    @FXML
    void handleCreaVoucher(ActionEvent event) {
        logger.log(Level.INFO, "Navigazione verso Crea Voucher");
        PageNavigationController.getInstance().navigateTo("CreaVoucherView");
    }

    /**
     * Gestisce il click sul pulsante "Gestione Utenti".
     */
    @FXML
    void handleGestioneUtenti(ActionEvent event) {
        logger.log(Level.INFO, "Click su Gestione Utenti");
        mostraPlaceholder("Gestione Utenti", "Funzionalità di gestione utenti in arrivo.");
    }

    /**
     * Gestisce il logout dell'Amministratore.
     * De-registra il servizio di notifica prima del logout.
     */
    @FXML
    void handleLogout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma Logout");
        alert.setHeaderText("Stai per uscire dall'applicazione");
        alert.setContentText("Sei sicuro di voler effettuare il logout?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            // De-registra il servizio di notifica
            AdminNotificationService.getInstance().unregister();

            // Invalida la sessione corrente nel SessionManager
            SessionManager.getInstance().logout();

            logger.log(Level.INFO, "Amministratore disconnesso");
            PageNavigationController.getInstance().navigateTo("login");
        }
    }

    /**
     * Mostra un placeholder per funzionalità non ancora implementate.
     */
    private void mostraPlaceholder(String titolo, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Funzionalità");
        alert.setHeaderText(titolo);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }
}
