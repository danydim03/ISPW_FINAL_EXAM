package org.example.graphic_controllers_general.homepages;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ClienteHomepageGraphicController {

    /**
     * Gestisce il click sulla card "Nuovo Ordine".
     * Definito nell'FXML con onAction="#handleNuovoOrdine"
     */
    @FXML
    void handleNuovoOrdine(ActionEvent event) {
        System.out.println("Click su Nuovo Ordine");
        mostraPlaceholder("Nuovo Ordine", "Qui andrà la schermata per creare una nuova spedizione.");
        // TODO: Caricare la scena 'NuovoOrdine.fxml'
    }

    /**
     * Gestisce il click sulla card "Storico Ordini".
     * Definito nell'FXML con onAction="#handleStoricoOrdini"
     */
    @FXML
    void handleStoricoOrdini(ActionEvent event) {
        System.out.println("Click su Storico Ordini");
        mostraPlaceholder("Storico Ordini", "Qui verrà mostrata la tabella con lo storico degli ordini.");
        // TODO: Caricare la scena 'StoricoOrdini.fxml'
    }

    /**
     * Gestisce il click sulla card "Mappa".
     * Definito nell'FXML con onAction="#handleVisualizzaMappa"
     */
    @FXML
    void handleVisualizzaMappa(ActionEvent event) {
        System.out.println("Click su Mappa");
        try {
            ClienteHomepageController.getInstance().apriMappa();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Impossibile aprire la mappa");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Gestisce il click sulla card "Logout".
     * Definito nell'FXML con onAction="#handleLogout"
     */
    @FXML
    void handleLogout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma Logout");
        alert.setHeaderText("Stai per uscire dall'applicazione");
        alert.setContentText("Sei sicuro di voler effettuare il logout?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            System.out.println("Utente disconnesso");
            // TODO: Tornare alla scena di Login
        }
    }

    // --- Metodi aggiuntivi per le card Profilo e Assistenza ---
    // Nota: Nel tuo FXML attuale, i bottoni "Profilo" e "Assistenza" non hanno ancora un 'onAction'.
    // Se vuoi attivarli, aggiungi onAction="#handleProfilo" e onAction="#handleAssistenza" nel file FXML.

    @FXML
    void handleProfilo(ActionEvent event) {
        mostraPlaceholder("Profilo Utente", "Gestione dati anagrafici e password.");
    }

    @FXML
    void handleAssistenza(ActionEvent event) {
        mostraPlaceholder("Assistenza Clienti", "Contatta il supporto tecnico.");
    }

    // Metodo di utilità per mostrare popup temporanei
    private void mostraPlaceholder(String titolo, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Funzionalità");
        alert.setHeaderText(titolo);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }
}