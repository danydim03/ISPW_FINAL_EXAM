package org.example.graphic_controllers_general.homepages;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.example.PageNavigationController;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteHomepageGraphicController {

    private static final Logger logger = Logger.getLogger(ClienteHomepageGraphicController.class.getName());

    @FXML
    void handleNuovoOrdine(ActionEvent event) {
        logger.log(Level.INFO, "Navigazione verso Crea Ordine");
        PageNavigationController.getInstance().navigateTo("CreaOrdineView");
    }

    @FXML
    void handleStoricoOrdini(ActionEvent event) {
        logger.log(Level.INFO, "Navigazione verso Storico Ordini");
        PageNavigationController.getInstance().navigateTo("StoricoOrdiniView");
    }

    @FXML
    void handleVisualizzaMappa(ActionEvent event) {
        logger.log(Level.INFO, "Click su Mappa");
        try {
            ClienteHomepageController.getInstance().apriMappa();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Impossibile aprire la mappa", e);
            mostraErrore("Errore", "Impossibile aprire la mappa: " + e.getMessage());
        }
    }

    @FXML
    void handleLogout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma Logout");
        alert.setHeaderText("Stai per uscire dall'applicazione");
        alert.setContentText("Sei sicuro di voler effettuare il logout?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            logger.log(Level.INFO, "Utente disconnesso");
            PageNavigationController.getInstance().navigateTo("login");
        }
    }

    @FXML
    void handleProfilo(ActionEvent event) {
        mostraPlaceholder("Profilo Utente", "Gestione dati anagrafici e password.");
        // TODO: PageNavigationController.getInstance().navigateTo("Profilo");
    }

    @FXML
    void handleWebsite(ActionEvent event) {
        logger.log(Level.INFO, "Apertura sito web Habibi");
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI("https://www.yahabibi.it"));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Impossibile aprire il sito web", e);
            mostraErrore("Errore", "Impossibile aprire il sito web: " + e.getMessage());
        }
    }

    private void mostraPlaceholder(String titolo, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Funzionalità");
        alert.setHeaderText(titolo);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

    private void mostraErrore(String titolo, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(titolo);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }
}