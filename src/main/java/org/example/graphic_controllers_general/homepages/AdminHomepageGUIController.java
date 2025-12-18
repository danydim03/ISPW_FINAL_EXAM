package org.example.graphic_controllers_general.homepages;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.example.PageNavigationController;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminHomepageGUIController {

    private static final Logger logger = Logger.getLogger(AdminHomepageGUIController.class.getName());

    @FXML
    void handleVisualizzaOrdini(ActionEvent event) {
        logger.log(Level.INFO, "Navigazione verso Visualizza Ordini");
        PageNavigationController.getInstance().navigateTo("visualizza_ordini");
    }

    @FXML
    void handleCreaVoucher(ActionEvent event) {
        logger.log(Level.INFO, "Click su Crea Voucher");
        mostraPlaceholder("Crea Voucher", "Funzionalità per creare nuovi voucher in arrivo.");
        // TODO: PageNavigationController.getInstance().navigateTo("CreaVoucher");
    }

    @FXML
    void handleGestioneUtenti(ActionEvent event) {
        logger.log(Level.INFO, "Click su Gestione Utenti");
        mostraPlaceholder("Gestione Utenti", "Funzionalità di gestione utenti in arrivo.");
    }

    @FXML
    void handleLogout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma Logout");
        alert.setHeaderText("Stai per uscire dall'applicazione");
        alert.setContentText("Sei sicuro di voler effettuare il logout?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            logger.log(Level.INFO, "Amministratore disconnesso");
            PageNavigationController.getInstance().navigateTo("login");
        }
    }

    private void mostraPlaceholder(String titolo, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Funzionalità");
        alert.setHeaderText(titolo);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }
}
