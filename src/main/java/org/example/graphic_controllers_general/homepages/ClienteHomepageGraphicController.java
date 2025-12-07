package org.example.graphic_controllers_general.homepages;

import javafx.event.ActionEvent;
import javafx.fxml. FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene. Parent;
import javafx.scene. Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout. StackPane;
import javafx. stage.Stage;
import org.example.PageNavigationController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteHomepageGraphicController {

    private static final Logger logger = Logger.getLogger(ClienteHomepageGraphicController.class.getName());

    /**
     * Gestisce il click sulla card "Nuovo Ordine".
     * Naviga alla schermata CreaOrdineView.fxml
     */
    @FXML
    void handleNuovoOrdine(ActionEvent event) {
        logger.log(Level.INFO, "Navigazione verso Crea Ordine");

        try {
            // Metodo 1: Usando PageNavigationController (se disponibile nel progetto)
            navigateWithPageNavigationController("use_cases/crea_ordine/CreaOrdineView. fxml");

        } catch (Exception e) {
            logger.log(Level. SEVERE, "Errore durante la navigazione a Crea Ordine: " + e.getMessage(), e);
            mostraErrore("Errore di Navigazione", "Impossibile aprire la schermata Crea Ordine.\n" + e.getMessage());
        }
    }

    /**
     * Naviga usando il PageNavigationController del progetto.
     * Questo metodo carica la vista nel StackPane "content" della base_view.fxml
     */
    private void navigateWithPageNavigationController(String fxmlPath) throws IOException {
        // Carica il nuovo FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/" + fxmlPath));
        Parent creaOrdineView = loader. load();

        // Ottieni il PageNavigationController e naviga
        PageNavigationController. getInstance().setContent(creaOrdineView);
    }

    /**
     * Metodo alternativo: Naviga sostituendo l'intera scena.
     * Usa questo se PageNavigationController non è disponibile.
     */
    private void navigateWithSceneReplacement(ActionEvent event, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/" + fxmlPath));
        Parent root = loader.load();

        // Ottieni lo Stage dall'evento
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Crea e imposta la nuova scena
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Gestisce il click sulla card "Storico Ordini".
     */
    @FXML
    void handleStoricoOrdini(ActionEvent event) {
        logger.log(Level.INFO, "Click su Storico Ordini");
        mostraPlaceholder("Storico Ordini", "Qui verrà mostrata la tabella con lo storico degli ordini.");
        // TODO: Implementare navigazione a StoricoOrdini.fxml
    }

    /**
     * Gestisce il click sulla card "Mappa".
     */
    @FXML
    void handleVisualizzaMappa(ActionEvent event) {
        logger.log(Level.INFO, "Click su Mappa");
        try {
            ClienteHomepageController.getInstance(). apriMappa();
        } catch (Exception e) {
            mostraErrore("Errore", "Impossibile aprire la mappa: " + e.getMessage());
        }
    }

    /**
     * Gestisce il click sulla card "Logout".
     */
    @FXML
    void handleLogout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType. CONFIRMATION);
        alert.setTitle("Conferma Logout");
        alert.setHeaderText("Stai per uscire dall'applicazione");
        alert.setContentText("Sei sicuro di voler effettuare il logout?");

        if (alert. showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            logger.log(Level.INFO, "Utente disconnesso");
            try {
                navigateWithPageNavigationController("login. fxml");
            } catch (IOException e) {
                logger. log(Level.SEVERE, "Errore durante il logout: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Gestisce il click sulla card "Profilo".
     */
    @FXML
    void handleProfilo(ActionEvent event) {
        mostraPlaceholder("Profilo Utente", "Gestione dati anagrafici e password.");
        // TODO: Implementare navigazione a Profilo.fxml
    }

    /**
     * Gestisce il click sulla card "Assistenza".
     */
    @FXML
    void handleAssistenza(ActionEvent event) {
        mostraPlaceholder("Assistenza Clienti", "Contatta il supporto tecnico.");
        // TODO: Implementare navigazione a Assistenza.fxml
    }

    // ==================== METODI DI UTILITÀ ====================

    /**
     * Mostra un popup informativo temporaneo
     */
    private void mostraPlaceholder(String titolo, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Funzionalità");
        alert.setHeaderText(titolo);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

    /**
     * Mostra un popup di errore
     */
    private void mostraErrore(String titolo, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(titolo);
        alert. setContentText(messaggio);
        alert.showAndWait();
    }
}