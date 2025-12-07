package org.example.use_cases.crea_ordine.graphic_controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml. FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene. control.cell.PropertyValueFactory;
import org.example.BaseGraphicControl;
import org.example.Facades.CreaOrdineFacade;
import org. example.Facades.CreaOrdineFacade.CreaOrdineException;
import org. example.use_cases.crea_ordine.beans.*;
import org.example.use_cases. crea_ordine.beans. RiepilogoOrdineBean. RigaOrdineBean;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller Grafico JavaFX per lo Use Case "Crea Ordine".
 *
 * Questo è il BOUNDARY nel pattern BCE.
 *
 * Responsabilità:
 * - Gestire l'interazione con l'utente tramite la GUI
 * - Raccogliere i dati dalla vista e incapsularli in Bean
 * - Chiamare il Facade per eseguire le operazioni
 * - Aggiornare la vista con i risultati
 *
 * NON contiene logica di business - delega tutto al Facade.
 */
public class CreaOrdineGUIController extends BaseGraphicControl implements Initializable {

    // ==================== ELEMENTI FXML ====================

    // Menu Prodotti Base
    @FXML
    private ComboBox<String> comboProdottoBase;

    // Checkbox per gli Add-on
    @FXML
    private CheckBox checkCipolla;
    @FXML
    private CheckBox checkSalsaYogurt;
    @FXML
    private CheckBox checkPatatine;
    @FXML
    private CheckBox checkMixVerdure;

    // Tabella riepilogo ordine
    @FXML
    private TableView<RigaOrdineBean> tabellaOrdine;
    @FXML
    private TableColumn<RigaOrdineBean, String> colonnaDescrizione;
    @FXML
    private TableColumn<RigaOrdineBean, String> colonnaPrezzo;
    @FXML
    private TableColumn<RigaOrdineBean, String> colonnaDurata;

    // Labels per i totali
    @FXML
    private Label labelSubtotale;
    @FXML
    private Label labelSconto;
    @FXML
    private Label labelTotale;
    @FXML
    private Label labelDurata;
    @FXML
    private Label labelVoucherInfo;
    @FXML
    private Label labelNumeroOrdine;

    // Campo voucher
    @FXML
    private TextField textFieldVoucher;

    // Bottoni
    @FXML
    private Button btnAggiungiProdotto;
    @FXML
    private Button btnRimuoviProdotto;
    @FXML
    private Button btnApplicaVoucher;
    @FXML
    private Button btnRimuoviVoucher;
    @FXML
    private Button btnConfermaOrdine;
    @FXML
    private Button btnAnnullaOrdine;

    // Pannello sconto (visibile solo quando c'è un voucher)
    @FXML
    private javafx.scene.layout.HBox panelSconto;

    // ==================== VARIABILI DI ISTANZA ====================

    private CreaOrdineFacade facade;
    private List<FoodBean> prodottiBaseDisponibili;
    private List<FoodBean> addOnDisponibili;
    private ObservableList<RigaOrdineBean> righeOrdineObservable;

    // ==================== INIZIALIZZAZIONE ====================

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        facade = CreaOrdineFacade.getInstance();
        righeOrdineObservable = FXCollections.observableArrayList();

        // Configura la tabella
        setupTabella();

        // Configura i listener
        setupListeners();

        // Carica i dati iniziali
        try {
            caricaDatiIniziali();
            iniziaNuovoOrdine();
        } catch (CreaOrdineException e) {
            mostraErrore("Errore di inizializzazione", e.getMessage());
        }
    }

    /**
     * Configura le colonne della tabella
     */
    private void setupTabella() {
        colonnaDescrizione. setCellValueFactory(new PropertyValueFactory<>("descrizione"));
        colonnaPrezzo.setCellValueFactory(new PropertyValueFactory<>("prezzoFormattato"));

        // Se hai la colonna durata
        if (colonnaDurata != null) {
            colonnaDurata.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDurata() + " min"));
        }

        tabellaOrdine.setItems(righeOrdineObservable);

        // Placeholder per tabella vuota
        tabellaOrdine.setPlaceholder(new Label("Nessun prodotto nell'ordine"));
    }

    /**
     * Configura i listener per gli eventi
     */
    private void setupListeners() {
        // Abilita/disabilita bottone rimuovi in base alla selezione
        tabellaOrdine.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    btnRimuoviProdotto.setDisable(newSelection == null);
                }
        );

        // Abilita/disabilita bottone applica voucher in base al testo
        textFieldVoucher. textProperty().addListener(
                (obs, oldText, newText) -> {
                    btnApplicaVoucher.setDisable(newText == null || newText.trim().isEmpty());
                }
        );

        // Inizialmente disabilita alcuni bottoni
        btnRimuoviProdotto. setDisable(true);
        btnApplicaVoucher. setDisable(true);
        btnRimuoviVoucher.setDisable(true);

        // Nascondi il pannello sconto inizialmente
        if (panelSconto != null) {
            panelSconto. setVisible(false);
            panelSconto.setManaged(false);
        }
    }

    /**
     * Carica i prodotti base e gli add-on disponibili
     */
    private void caricaDatiIniziali() throws CreaOrdineException {
        // Carica prodotti base
        prodottiBaseDisponibili = facade.getMenuProdottiBase();

        // Popola la combo box
        List<String> nomiProdotti = new ArrayList<>();
        for (FoodBean fb : prodottiBaseDisponibili) {
            nomiProdotti.add(fb.getDescrizione() + " - €" + String.format("%.2f", fb.getCosto()));
        }
        comboProdottoBase.setItems(FXCollections.observableArrayList(nomiProdotti));

        // Seleziona il primo elemento di default
        if (! nomiProdotti.isEmpty()) {
            comboProdottoBase.getSelectionModel().selectFirst();
        }

        // Carica add-on (per riferimento futuro se necessario)
        addOnDisponibili = facade.getMenuAddOn();
    }

    /**
     * Inizia un nuovo ordine
     */
    private void iniziaNuovoOrdine() throws CreaOrdineException {
        OrdineBean ordine = facade.iniziaNuovoOrdine();

        // Mostra numero ordine
        if (labelNumeroOrdine != null && ordine. getNumeroOrdine() != null) {
            labelNumeroOrdine.setText("Ordine #" + ordine.getNumeroOrdine());
        }

        aggiornaRiepilogo();
    }

    // ==================== EVENT HANDLERS ====================

    /**
     * Handler per il bottone "Aggiungi Prodotto"
     */
    @FXML
    private void onAggiungiProdotto() {
        try {
            // Verifica selezione prodotto base
            int selectedIndex = comboProdottoBase.getSelectionModel().getSelectedIndex();
            if (selectedIndex < 0) {
                mostraWarning("Selezione richiesta", "Seleziona un prodotto base dal menu.");
                return;
            }

            // Crea il FoodBean con il prodotto selezionato
            FoodBean prodottoSelezionato = prodottiBaseDisponibili.get(selectedIndex);
            FoodBean foodBean = new FoodBean();
            foodBean.setClasse(prodottoSelezionato.getClasse());
            foodBean.setDescrizione(prodottoSelezionato.getDescrizione());
            foodBean.setCosto(prodottoSelezionato.getCosto());
            foodBean.setDurata(prodottoSelezionato.getDurata());
            foodBean.setTipo(prodottoSelezionato.getTipo());

            // Aggiungi gli add-on selezionati (Pattern Decorator applicato nel Controller Applicativo)
            if (checkCipolla != null && checkCipolla.isSelected()) {
                foodBean. aggiungiAddOn("Cipolla");
            }
            if (checkSalsaYogurt != null && checkSalsaYogurt.isSelected()) {
                foodBean.aggiungiAddOn("SalsaYogurt");
            }
            if (checkPatatine != null && checkPatatine.isSelected()) {
                foodBean.aggiungiAddOn("Patatine");
            }
            if (checkMixVerdure != null && checkMixVerdure.isSelected()) {
                foodBean.aggiungiAddOn("MixVerdureGrigliate");
            }

            // Aggiungi tramite il Facade
            RiepilogoOrdineBean riepilogo = facade.aggiungiProdotto(foodBean);

            // Aggiorna la vista
            aggiornaVistaConRiepilogo(riepilogo);

            // Reset selezioni add-on
            resetSelezioniAddOn();

            // Mostra feedback
            mostraInfo("Prodotto aggiunto", "Prodotto aggiunto all'ordine con successo!");

        } catch (CreaOrdineException e) {
            mostraErrore("Errore", e.getMessage());
        }
    }

    /**
     * Handler per il bottone "Rimuovi Prodotto"
     */
    @FXML
    private void onRimuoviProdotto() {
        try {
            int selectedIndex = tabellaOrdine.getSelectionModel().getSelectedIndex();
            if (selectedIndex < 0) {
                mostraWarning("Selezione richiesta", "Seleziona un prodotto dalla lista da rimuovere.");
                return;
            }

            // Chiedi conferma
            RigaOrdineBean rigaSelezionata = tabellaOrdine.getSelectionModel(). getSelectedItem();
            if (! mostraConferma("Conferma rimozione",
                    "Vuoi rimuovere \"" + rigaSelezionata.getDescrizione() + "\" dall'ordine?")) {
                return;
            }

            RiepilogoOrdineBean riepilogo = facade.rimuoviProdotto(selectedIndex);
            aggiornaVistaConRiepilogo(riepilogo);

        } catch (CreaOrdineException e) {
            mostraErrore("Errore", e.getMessage());
        }
    }

    /**
     * Handler per il bottone "Applica Voucher"
     */
    @FXML
    private void onApplicaVoucher() {
        try {
            String codiceVoucher = textFieldVoucher.getText();

            if (codiceVoucher == null || codiceVoucher.trim().isEmpty()) {
                mostraWarning("Voucher richiesto", "Inserisci un codice voucher.");
                return;
            }

            // Verifica che ci siano prodotti nell'ordine
            if (righeOrdineObservable. isEmpty()) {
                mostraWarning("Ordine vuoto", "Aggiungi almeno un prodotto prima di applicare un voucher.");
                return;
            }

            RiepilogoOrdineBean riepilogo = facade.applicaVoucher(codiceVoucher);
            aggiornaVistaConRiepilogo(riepilogo);

            // Disabilita il campo voucher e il bottone applica
            textFieldVoucher.setDisable(true);
            btnApplicaVoucher.setDisable(true);
            btnRimuoviVoucher.setDisable(false);

            mostraInfo("Voucher applicato",
                    "Voucher " + codiceVoucher. toUpperCase() + " applicato con successo!\n" +
                            "Sconto: " + riepilogo.getScontoFormattato());

        } catch (CreaOrdineException e) {
            mostraErrore("Voucher non valido", e.getMessage());
            textFieldVoucher.selectAll();
            textFieldVoucher.requestFocus();
        }
    }

    /**
     * Handler per il bottone "Rimuovi Voucher"
     */
    @FXML
    private void onRimuoviVoucher() {
        try {
            if (! mostraConferma("Rimuovi Voucher", "Vuoi rimuovere il voucher applicato?")) {
                return;
            }

            RiepilogoOrdineBean riepilogo = facade.rimuoviVoucher();
            aggiornaVistaConRiepilogo(riepilogo);

            // Riabilita il campo voucher
            textFieldVoucher.setDisable(false);
            textFieldVoucher.clear();
            btnApplicaVoucher.setDisable(true);
            btnRimuoviVoucher.setDisable(true);

        } catch (CreaOrdineException e) {
            mostraErrore("Errore", e.getMessage());
        }
    }

    /**
     * Handler per il bottone "Conferma Ordine"
     */
    @FXML
    private void onConfermaOrdine() {
        try {
            // Verifica che ci siano prodotti
            if (righeOrdineObservable.isEmpty()) {
                mostraWarning("Ordine vuoto", "Aggiungi almeno un prodotto all'ordine prima di confermare.");
                return;
            }

            // Mostra riepilogo e chiedi conferma
            RiepilogoOrdineBean riepilogo = facade.getRiepilogo();
            String messaggioConferma = costruisciMessaggioConferma(riepilogo);

            if (!mostraConferma("Conferma Ordine", messaggioConferma)) {
                return;
            }

            boolean success = facade.confermaOrdine();

            if (success) {
                mostraInfo("Ordine confermato",
                        "Il tuo ordine #" + riepilogo.getNumeroOrdine() + " è stato confermato!\n\n" +
                                "Totale: " + riepilogo.getTotaleFormattato() + "\n" +
                                "Tempo di preparazione stimato: " + riepilogo.getDurataFormattata());

                // Naviga alla pagina successiva (pagamento o conferma)
                navigaToPagamento();
            } else {
                mostraErrore("Errore", "Si è verificato un errore durante la conferma dell'ordine.");
            }

        } catch (CreaOrdineException e) {
            mostraErrore("Errore", e.getMessage());
        }
    }

    /**
     * Handler per il bottone "Annulla Ordine"
     */
    @FXML
    private void onAnnullaOrdine() {
        // Se l'ordine è vuoto, esci direttamente
        if (righeOrdineObservable.isEmpty()) {
            navigaToHome();
            return;
        }

        if (mostraConferma("Annulla Ordine",
                "Sei sicuro di voler annullare l'ordine?\nTutti i prodotti selezionati verranno rimossi. ")) {

            facade.annullaOrdine();

            try {
                // Reset completo della vista
                resetVistaCompleta();
                iniziaNuovoOrdine();

            } catch (CreaOrdineException e) {
                mostraErrore("Errore", e.getMessage());
            }
        }
    }

    // ==================== METODI DI AGGIORNAMENTO VISTA ====================

    /**
     * Aggiorna il riepilogo chiamando il Facade
     */
    private void aggiornaRiepilogo() throws CreaOrdineException {
        RiepilogoOrdineBean riepilogo = facade.getRiepilogo();
        aggiornaVistaConRiepilogo(riepilogo);
    }

    /**
     * Aggiorna la vista con i dati del riepilogo
     */
    private void aggiornaVistaConRiepilogo(RiepilogoOrdineBean riepilogo) {
        if (riepilogo == null) {
            return;
        }

        // Aggiorna la tabella
        righeOrdineObservable. clear();
        righeOrdineObservable.addAll(riepilogo.getRigheOrdine());

        // Aggiorna i totali
        labelSubtotale.setText(riepilogo.getSubtotaleFormattato());
        labelTotale.setText(riepilogo.getTotaleFormattato());
        labelDurata.setText(riepilogo.getDurataFormattata());

        // Aggiorna info voucher e sconto
        if (riepilogo.isVoucherApplicato()) {
            // Mostra lo sconto
            if (labelSconto != null) {
                labelSconto.setText(riepilogo.getScontoFormattato());
            }
            if (labelVoucherInfo != null) {
                labelVoucherInfo.setText(riepilogo.getCodiceVoucher() + " - " + riepilogo. getDescrizioneVoucher());
            }
            // Mostra il pannello sconto
            if (panelSconto != null) {
                panelSconto.setVisible(true);
                panelSconto.setManaged(true);
            }
        } else {
            // Nascondi lo sconto
            if (labelSconto != null) {
                labelSconto.setText("€0.00");
            }
            if (labelVoucherInfo != null) {
                labelVoucherInfo.setText("");
            }
            // Nascondi il pannello sconto
            if (panelSconto != null) {
                panelSconto.setVisible(false);
                panelSconto. setManaged(false);
            }
        }

        // Aggiorna stato bottone conferma
        btnConfermaOrdine.setDisable(righeOrdineObservable. isEmpty());
    }

    // ==================== METODI DI SUPPORTO ====================

    /**
     * Reset delle selezioni degli add-on
     */
    private void resetSelezioniAddOn() {
        if (checkCipolla != null) checkCipolla.setSelected(false);
        if (checkSalsaYogurt != null) checkSalsaYogurt.setSelected(false);
        if (checkPatatine != null) checkPatatine.setSelected(false);
        if (checkMixVerdure != null) checkMixVerdure.setSelected(false);
    }

    /**
     * Reset completo della vista
     */
    private void resetVistaCompleta() {
        // Reset selezioni
        resetSelezioniAddOn();
        comboProdottoBase.getSelectionModel().selectFirst();

        // Reset voucher
        textFieldVoucher.clear();
        textFieldVoucher.setDisable(false);
        btnApplicaVoucher.setDisable(true);
        btnRimuoviVoucher.setDisable(true);

        // Reset tabella
        righeOrdineObservable. clear();

        // Reset labels
        labelSubtotale. setText("€0.00");
        if (labelSconto != null) labelSconto. setText("€0.00");
        labelTotale.setText("€0.00");
        labelDurata.setText("0 min");
        if (labelVoucherInfo != null) labelVoucherInfo.setText("");

        // Nascondi pannello sconto
        if (panelSconto != null) {
            panelSconto.setVisible(false);
            panelSconto.setManaged(false);
        }
    }

    /**
     * Costruisce il messaggio di conferma ordine
     */
    private String costruisciMessaggioConferma(RiepilogoOrdineBean riepilogo) {
        StringBuilder sb = new StringBuilder();
        sb.append("Riepilogo Ordine #").append(riepilogo.getNumeroOrdine()).append("\n\n");

        sb. append("Prodotti:\n");
        for (RigaOrdineBean riga : riepilogo.getRigheOrdine()) {
            sb.append("• ").append(riga.getDescrizione())
                    .append(" - "). append(riga.getPrezzoFormattato()).append("\n");
        }

        sb.append("\n");
        sb.append("Subtotale: ").append(riepilogo. getSubtotaleFormattato()).append("\n");

        if (riepilogo.isVoucherApplicato()) {
            sb.append("Sconto ("). append(riepilogo.getCodiceVoucher()).append("): ")
                    .append(riepilogo.getScontoFormattato()).append("\n");
        }

        sb.append("TOTALE: ").append(riepilogo.getTotaleFormattato()).append("\n\n");
        sb.append("Tempo di preparazione: ").append(riepilogo.getDurataFormattata()).append("\n\n");
        sb.append("Vuoi confermare l'ordine? ");

        return sb. toString();
    }

    // ==================== METODI DI NAVIGAZIONE ====================

    /**
     * Naviga alla pagina di pagamento
     */
    private void navigaToPagamento() {
        // Implementare la navigazione usando PageNavigationController
        // PageNavigationController.getInstance().navigateTo("PagamentoView. fxml");
    }

    /**
     * Naviga alla home
     */
    private void navigaToHome() {
        // Implementare la navigazione usando PageNavigationController
        // PageNavigationController.getInstance().navigateTo("HomeView.fxml");
    }

    // ==================== METODI DI DIALOGO ====================

    /**
     * Mostra un dialogo di errore
     */
    private void mostraErrore(String titolo, String messaggio) {
        Alert alert = new Alert(Alert. AlertType.ERROR);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert. showAndWait();
    }

    /**
     * Mostra un dialogo di warning
     */
    private void mostraWarning(String titolo, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert. setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

    /**
     * Mostra un dialogo informativo
     */
    private void mostraInfo(String titolo, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert. showAndWait();
    }

    /**
     * Mostra un dialogo di conferma
     * @return true se l'utente conferma, false altrimenti
     */
    private boolean mostraConferma(String titolo, String messaggio) {
        Alert alert = new Alert(Alert. AlertType.CONFIRMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}