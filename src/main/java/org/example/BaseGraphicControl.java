package org.example;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class BaseGraphicControl implements Initializable {

    @FXML
    private StackPane content;
    @FXML
    private Button backButton;
    @FXML
    private Button accountButton;
    @FXML
    private Button notificationButton;
    @FXML
    private VBox pendingEventList;

    /**
     * Default constructor - initialization is handled by JavaFX's initialize()
     * method.
     * All field injection and setup occurs there, not in the constructor.
     */
    public BaseGraphicControl() {
        // Empty: JavaFX controller initialization happens in initialize()
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backButton.setVisible(false);

        // Registra questo controller nel PageNavigationController
        PageNavigationController.getInstance().setBaseGraphicController(this);

        // AGGIUNGI: Imposta anche il contentPane
        PageNavigationController.getInstance().setContentPane(content);

        System.out.println(">>> BaseGraphicControl inizializzato e registrato!");
    }

    @FXML
    private void goBack() {
        content.getChildren().remove(content.getChildren().size() - 1);
        if (content.getChildren().size() == 1)
            backButton.setVisible(false);
    }

    public void switchTo(Node node) {
        content.getChildren().add(node);
        if (content.getChildren().size() >= 2)
            backButton.setVisible(true);
    }

    void openMainPage(Node node, String name) {
        content.getChildren().clear();
        content.getChildren().add(node);
        if (accountButton != null) {
            accountButton.setText(name);
            accountButton.setVisible(true);
        }
        if (notificationButton != null) {
            notificationButton.setVisible(true);
        }
    }

    // ==================== UTILITY METHODS FOR ALERTS ====================
    // Centralizzati qui per High Cohesion e riuso in tutti i GUI Controller

    /**
     * Mostra un messaggio di errore all'utente.
     * 
     * @param titolo    il titolo della finestra di dialogo
     * @param messaggio il messaggio da visualizzare
     */
    protected void mostraErrore(String titolo, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

    /**
     * Mostra un messaggio di warning all'utente.
     * 
     * @param titolo    il titolo della finestra di dialogo
     * @param messaggio il messaggio da visualizzare
     */
    protected void mostraWarning(String titolo, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

    /**
     * Mostra un messaggio informativo all'utente.
     * 
     * @param titolo    il titolo della finestra di dialogo
     * @param messaggio il messaggio da visualizzare
     */
    protected void mostraInfo(String titolo, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

    /**
     * Mostra una finestra di conferma all'utente.
     * 
     * @param titolo    il titolo della finestra di dialogo
     * @param messaggio il messaggio da visualizzare
     * @return true se l'utente conferma, false altrimenti
     */
    protected boolean mostraConferma(String titolo, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        java.util.Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK;
    }
}
