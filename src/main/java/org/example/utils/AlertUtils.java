package org.example.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

/**
 * Utility class per la gestione centralizzata degli Alert JavaFX.
 * 
 * Segue GRASP Pure Fabrication: questa classe non rappresenta un concetto
 * di dominio, ma fornisce funzionalità comuni a tutti i GUI Controller.
 * 
 * Benefici:
 * - High Cohesion: tutti i metodi riguardano gli Alert
 * - Low Coupling: i controller usano questa utility invece di duplicare codice
 * - DRY: elimina duplicazione di codice Alert in 5+ controller
 */
public final class AlertUtils {

    private AlertUtils() {
        // Utility class - previene istanziazione
    }

    /**
     * Mostra un messaggio di errore all'utente.
     * 
     * @param titolo    il titolo della finestra di dialogo
     * @param messaggio il messaggio da visualizzare
     */
    public static void mostraErrore(String titolo, String messaggio) {
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
    public static void mostraWarning(String titolo, String messaggio) {
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
    public static void mostraInfo(String titolo, String messaggio) {
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
    public static boolean mostraConferma(String titolo, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Mostra un messaggio placeholder per funzionalità non ancora implementate.
     * 
     * @param titolo    il titolo della funzionalità
     * @param messaggio la descrizione
     */
    public static void mostraPlaceholder(String titolo, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Funzionalità");
        alert.setHeaderText(titolo);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }
}
