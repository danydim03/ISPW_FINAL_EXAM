package org.example.use_cases.crea_voucher.graphic_controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.example.PageNavigationController;
import org.example.exceptions.*;
import org.example.model.voucher.Voucher;
import org.example.use_cases.crea_voucher.CreaVoucherFacade;
import org.example.use_cases.crea_voucher.beans.CreaVoucherBean;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * GUI Controller per la vista JavaFX "Crea Voucher".
 * 
 * Responsabilità:
 * - Gestire l'interazione con l'utente tramite componenti JavaFX
 * - Validare gli input utente
 * - Delegare la logica di business al Facade
 * - Fornire feedback visivo all'utente
 * 
 * Segue il pattern BCE: questo è il BOUNDARY (interfaccia grafica).
 * Applica GRASP: Low Coupling (dipende solo da Facade e Bean).
 */
public class CreaVoucherGUIController implements Initializable {

    private static final Logger logger = Logger.getLogger(CreaVoucherGUIController.class.getName());

    // FXML Components - Tipo Voucher
    @FXML
    private RadioButton radioPercentuale;

    @FXML
    private RadioButton radioFisso;

    private ToggleGroup groupTipoVoucher;

    // FXML Components - Form Fields
    @FXML
    private TextField textFieldCodice;

    @FXML
    private TextField textFieldValore;

    @FXML
    private TextField textFieldMinimoOrdine;

    @FXML
    private DatePicker datePickerScadenza;

    // FXML Components - Labels and Messages
    @FXML
    private Label labelValore;

    @FXML
    private Label labelErrore;

    @FXML
    private Label labelSuccesso;

    @FXML
    private javafx.scene.text.Text textHintValore;

    // FXML Components - Containers
    @FXML
    private VBox vboxMinimoOrdine;

    // FXML Components - Buttons
    @FXML
    private Button btnCreaVoucher;

    @FXML
    private Button btnAnnulla;

    // Business logic components
    private CreaVoucherFacade facade;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupToggleGroup();
        setupListeners();
        inizializzaFacade();
    }

    /**
     * Configura il ToggleGroup per i radio buttons.
     */
    private void setupToggleGroup() {
        groupTipoVoucher = new ToggleGroup();
        radioPercentuale.setToggleGroup(groupTipoVoucher);
        radioFisso.setToggleGroup(groupTipoVoucher);

        // Seleziona Percentuale di default
        radioPercentuale.setSelected(true);
    }

    /**
     * Configura i listener per i componenti UI.
     */
    private void setupListeners() {
        // Listener per cambio tipo voucher
        groupTipoVoucher.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                onTipoVoucherChanged();
            }
        });

        // Listener per rimuovere messaggi di errore quando l'utente modifica i campi
        textFieldCodice.textProperty().addListener((observable, oldValue, newValue) -> nascondiMessaggi());
        textFieldValore.textProperty().addListener((observable, oldValue, newValue) -> nascondiMessaggi());
        textFieldMinimoOrdine.textProperty().addListener((observable, oldValue, newValue) -> nascondiMessaggi());
    }

    /**
     * Inizializza il Facade con il token di sessione.
     */
    private void inizializzaFacade() {
        try {
            String tokenKey = PageNavigationController.getInstance().getSessionTokenKey();
            this.facade = new CreaVoucherFacade(tokenKey);
        } catch (MissingAuthorizationException e) {
            logger.log(Level.SEVERE, "Errore di autorizzazione durante inizializzazione facade", e);
            mostraErrore("Accesso negato. Solo gli amministratori possono creare voucher.");
            disabilitaForm();
        }
    }

    /**
     * Gestisce il cambio di tipo voucher (Percentuale/Fisso).
     * Aggiorna i label e la visibilità del campo "Minimo Ordine".
     */
    private void onTipoVoucherChanged() {
        boolean isFisso = radioFisso.isSelected();

        // Aggiorna label e hint del valore
        if (isFisso) {
            labelValore.setText("Valore Sconto (€) *");
            textHintValore.setText("Importo fisso di sconto");
            textFieldValore.setPromptText("Es. 5.00");

            // Mostra campo minimo ordine
            vboxMinimoOrdine.setVisible(true);
            vboxMinimoOrdine.setManaged(true);
        } else {
            labelValore.setText("Valore Sconto (%) *");
            textHintValore.setText("Percentuale tra 0 e 100");
            textFieldValore.setPromptText("Es. 10");

            // Nascondi campo minimo ordine
            vboxMinimoOrdine.setVisible(false);
            vboxMinimoOrdine.setManaged(false);
        }

        nascondiMessaggi();
    }

    /**
     * Gestisce l'evento di creazione del voucher.
     */
    @FXML
    public void onCreaVoucher() {
        nascondiMessaggi();

        try {
            // 1. Raccogli i dati dal form
            CreaVoucherBean bean = costruisciBeanDaForm();

            // 2. Delega la creazione al Facade
            Voucher voucherCreato = facade.creaVoucher(bean);

            // 3. Mostra messaggio di successo
            mostraSuccesso(
                    "Voucher creato con successo!\nCodice: " + voucherCreato.getCodice());

            // 4. Reset del form dopo 2 secondi
            resetFormDopoSuccesso();

        } catch (ValidationException e) {
            // Errore di validazione (business rules o sintassi)
            logger.log(Level.WARNING, "Validazione fallita", e);
            mostraErrore(e.getMessage());

        } catch (DAOException | PropertyException | ResourceNotFoundException | MissingAuthorizationException e) {
            // Errore di sistema
            logger.log(Level.SEVERE, "Errore durante la creazione del voucher", e);
            mostraErrore("Errore durante la creazione del voucher. Riprova.");
        }
    }

    /**
     * Costruisce il bean dai dati inseriti nel form.
     * 
     * @return CreaVoucherBean con i dati del form
     * @throws ValidationException se i dati non sono validi sintatticamente
     */
    private CreaVoucherBean costruisciBeanDaForm() throws ValidationException {
        CreaVoucherBean bean = new CreaVoucherBean();

        // Codice
        String codice = textFieldCodice.getText();
        if (codice == null || codice.trim().isEmpty()) {
            throw new ValidationException("Il codice del voucher è obbligatorio");
        }
        bean.setCodice(codice.trim());

        // Tipo voucher
        String tipo = radioPercentuale.isSelected() ? "PERCENTUALE" : "FISSO";
        bean.setTipoVoucher(tipo);

        // Valore
        String valoreText = textFieldValore.getText();
        if (valoreText == null || valoreText.trim().isEmpty()) {
            throw new ValidationException("Il valore dello sconto è obbligatorio");
        }
        try {
            double valore = Double.parseDouble(valoreText.trim().replace(',', '.'));
            bean.setValore(valore);
        } catch (NumberFormatException e) {
            throw new ValidationException("Il valore deve essere un numero valido", e);
        }

        // Minimo ordine (solo per voucher fissi)
        if (radioFisso.isSelected()) {
            String minimoText = textFieldMinimoOrdine.getText();
            if (minimoText != null && !minimoText.trim().isEmpty()) {
                try {
                    double minimo = Double.parseDouble(minimoText.trim().replace(',', '.'));
                    bean.setMinimoOrdine(minimo);
                } catch (NumberFormatException e) {
                    throw new ValidationException("Il minimo ordine deve essere un numero valido", e);
                }
            }
        }

        // Data scadenza (opzionale)
        LocalDate dataScadenza = datePickerScadenza.getValue();
        if (dataScadenza != null) {
            bean.setDataScadenza(dataScadenza);
        }

        return bean;
    }

    /**
     * Gestisce l'evento di annullamento.
     * Torna alla homepage dell'amministratore.
     */
    @FXML
    public void onAnnulla() {
        PageNavigationController.getInstance().returnToMainPage();
    }

    /**
     * Reset del form dopo creazione con successo.
     */
    private void resetFormDopoSuccesso() {
        // Usa Platform.runLater per evitare problemi di threading
        javafx.application.Platform.runLater(() -> {
            try {
                Thread.sleep(2000); // Aspetta 2 secondi
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.log(Level.WARNING, "Thread interrotto durante il reset", e);
            }
            resetForm();
        });
    }

    /**
     * Reset completo del form ai valori di default.
     */
    private void resetForm() {
        textFieldCodice.clear();
        textFieldValore.clear();
        textFieldMinimoOrdine.setText("0");
        datePickerScadenza.setValue(null);
        radioPercentuale.setSelected(true);
        nascondiMessaggi();
    }

    /**
     * Disabilita il form (in caso di errore di autorizzazione).
     */
    private void disabilitaForm() {
        btnCreaVoucher.setDisable(true);
        textFieldCodice.setDisable(true);
        textFieldValore.setDisable(true);
        textFieldMinimoOrdine.setDisable(true);
        datePickerScadenza.setDisable(true);
        radioPercentuale.setDisable(true);
        radioFisso.setDisable(true);
    }

    /**
     * Mostra un messaggio di errore nell'UI.
     */
    private void mostraErrore(String messaggio) {
        labelErrore.setText("❌ " + messaggio);
        labelErrore.setVisible(true);
        labelErrore.setManaged(true);
        labelErrore.setStyle("-fx-text-fill: #d32f2f; -fx-font-weight: bold;");
    }

    /**
     * Mostra un messaggio di successo nell'UI.
     */
    private void mostraSuccesso(String messaggio) {
        labelSuccesso.setText("✅ " + messaggio);
        labelSuccesso.setVisible(true);
        labelSuccesso.setManaged(true);
        labelSuccesso.setStyle("-fx-text-fill: #388e3c; -fx-font-weight: bold;");
    }

    /**
     * Nasconde tutti i messaggi di errore e successo.
     */
    private void nascondiMessaggi() {
        labelErrore.setVisible(false);
        labelErrore.setManaged(false);
        labelSuccesso.setVisible(false);
        labelSuccesso.setManaged(false);
    }
}
