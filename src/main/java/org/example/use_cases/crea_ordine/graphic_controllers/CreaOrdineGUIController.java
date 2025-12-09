package org.example.use_cases.crea_ordine.graphic_controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.BaseGraphicControl;
import org.example.Facades.CreaOrdineFacade;
import org.example.use_cases.crea_ordine.beans.FoodBean;
import org.example.use_cases.crea_ordine.beans.OrdineBean;
import org.example.use_cases.crea_ordine.beans.RiepilogoOrdineBean;
import org.example.use_cases.crea_ordine.beans.RiepilogoOrdineBean.RigaOrdineBean;
import java.util.List;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;

import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class CreaOrdineGUIController  implements Initializable {

    private static final Logger logger = Logger.getLogger(CreaOrdineGUIController.class.getName());

    @FXML
    private ComboBox<String> comboProdottoBase;
    @FXML
    private CheckBox checkCipolla;
    @FXML
    private CheckBox checkSalsaYogurt;
    @FXML
    private CheckBox checkPatatine;
    @FXML
    private CheckBox checkMixVerdure;

    @FXML
    private TableView<RigaOrdineBean> tabellaOrdine;
    @FXML
    private TableColumn<RigaOrdineBean, String> colonnaDescrizione;
    @FXML
    private TableColumn<RigaOrdineBean, String> colonnaPrezzo;
    @FXML
    private TableColumn<RigaOrdineBean, String> colonnaDurata;

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

    @FXML
    private TextField textFieldVoucher;

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

    @FXML
    private javafx.scene.layout.HBox panelSconto;

    private CreaOrdineFacade facade;
    private List<FoodBean> prodottiBaseDisponibili;
    private List<FoodBean> addOnDisponibili;
    private ObservableList<RigaOrdineBean> righeOrdineObservable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        facade = CreaOrdineFacade.getInstance();
        righeOrdineObservable = FXCollections.observableArrayList();

        setupTabella();
        setupListeners();

        try {
            caricaDatiIniziali();
            iniziaNuovoOrdine();
        } catch (CreaOrdineFacade.CreaOrdineException e) {
            logger.log(Level.WARNING, "DB non disponibile, uso dati mock: " + e.getMessage());
            caricaDatiMock();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Errore generico nell'inizializzazione", e);
            mostraErrore("Errore di inizializzazione", e.getMessage());
        }
    }

    private void caricaDatiMock() {
        // Dati di test per quando il DB non è disponibile
        List<String> prodottiMock = List.of(
                "Kebab Classico - €5.00",
                "Kebab XL - €7.50",
                "Piadina Kebab - €6.00"
        );
        comboProdottoBase.setItems(FXCollections.observableArrayList(prodottiMock));
        if (!prodottiMock.isEmpty()) {
            comboProdottoBase.getSelectionModel().selectFirst();
        }

        // Inizializza una lista vuota di prodotti base per evitare NullPointerException
        prodottiBaseDisponibili = new ArrayList<>();
        addOnDisponibili = new ArrayList<>();
    }


    private void setupTabella() {
        colonnaDescrizione.setCellValueFactory(new PropertyValueFactory<>("descrizione"));
        colonnaPrezzo.setCellValueFactory(new PropertyValueFactory<>("prezzoFormattato"));
        if (colonnaDurata != null) {
            colonnaDurata.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getDurata() + " min"));
        }
        tabellaOrdine.setItems(righeOrdineObservable);
        tabellaOrdine.setPlaceholder(new Label("Nessun prodotto nell'ordine"));
    }

    private void setupListeners() {
        tabellaOrdine.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> btnRimuoviProdotto.setDisable(newSelection == null)
        );

        textFieldVoucher.textProperty().addListener(
                (obs, oldText, newText) -> btnApplicaVoucher.setDisable(newText == null || newText.trim().isEmpty())
        );

        btnRimuoviProdotto.setDisable(true);
        btnApplicaVoucher.setDisable(true);
        btnRimuoviVoucher.setDisable(true);

        if (panelSconto != null) {
            panelSconto.setVisible(false);
            panelSconto.setManaged(false);
        }
    }

    private void caricaDatiIniziali() throws CreaOrdineFacade.CreaOrdineException {
        // Solo facade: nessun mock qui, nessun accesso diretto a DAO
        prodottiBaseDisponibili = facade.getMenuProdottiBase();
        addOnDisponibili = facade.getMenuAddOn();

        List<String> nomiProdotti = new ArrayList<>();
        for (FoodBean fb : prodottiBaseDisponibili) {
            nomiProdotti.add(fb.getDescrizione() + " - €" + String.format("%.2f", fb.getCosto()));
        }
        comboProdottoBase.setItems(FXCollections.observableArrayList(nomiProdotti));
        if (!nomiProdotti.isEmpty()) {
            comboProdottoBase.getSelectionModel().selectFirst();
        }
    }

    private void iniziaNuovoOrdine() throws CreaOrdineFacade.CreaOrdineException {
        OrdineBean ordine = facade.iniziaNuovoOrdine();
        if (labelNumeroOrdine != null && ordine.getNumeroOrdine() != null) {
            labelNumeroOrdine.setText("Ordine #" + ordine.getNumeroOrdine());
        }
        aggiornaRiepilogo();
    }

    @FXML
    private void onAggiungiProdotto() {
        try {
            int selectedIndex = comboProdottoBase.getSelectionModel().getSelectedIndex();
            if (selectedIndex < 0) {
                mostraWarning("Selezione richiesta", "Seleziona un prodotto base dal menu.");
                return;
            }

            FoodBean prodottoSelezionato = prodottiBaseDisponibili.get(selectedIndex);
            FoodBean foodBean = new FoodBean();
            foodBean.setClasse(prodottoSelezionato.getClasse());
            foodBean.setDescrizione(prodottoSelezionato.getDescrizione());
            foodBean.setCosto(prodottoSelezionato.getCosto());
            foodBean.setDurata(prodottoSelezionato.getDurata());
            foodBean.setTipo(prodottoSelezionato.getTipo());

            if (checkCipolla != null && checkCipolla.isSelected()) foodBean.aggiungiAddOn("Cipolla");
            if (checkSalsaYogurt != null && checkSalsaYogurt.isSelected()) foodBean.aggiungiAddOn("SalsaYogurt");
            if (checkPatatine != null && checkPatatine.isSelected()) foodBean.aggiungiAddOn("Patatine");
            if (checkMixVerdure != null && checkMixVerdure.isSelected()) foodBean.aggiungiAddOn("MixVerdureGrigliate");

            RiepilogoOrdineBean riepilogo = facade.aggiungiProdotto(foodBean);
            aggiornaVistaConRiepilogo(riepilogo);
            resetSelezioniAddOn();
            mostraInfo("Prodotto aggiunto", "Prodotto aggiunto all'ordine con successo!");

        } catch (CreaOrdineFacade.CreaOrdineException e) {
            mostraErrore("Errore", e.getMessage());
        }
    }

    @FXML
    private void onRimuoviProdotto() {
        try {
            int selectedIndex = tabellaOrdine.getSelectionModel().getSelectedIndex();
            if (selectedIndex < 0) {
                mostraWarning("Selezione richiesta", "Seleziona un prodotto dalla lista da rimuovere.");
                return;
            }

            RigaOrdineBean rigaSelezionata = tabellaOrdine.getSelectionModel().getSelectedItem();
            if (!mostraConferma("Conferma rimozione",
                    "Vuoi rimuovere \"" + rigaSelezionata.getDescrizione() + "\" dall'ordine?")) {
                return;
            }

            RiepilogoOrdineBean riepilogo = facade.rimuoviProdotto(selectedIndex);
            aggiornaVistaConRiepilogo(riepilogo);

        } catch (CreaOrdineFacade.CreaOrdineException e) {
            mostraErrore("Errore", e.getMessage());
        }
    }

    @FXML
    private void onApplicaVoucher() {
        try {
            String codiceVoucher = textFieldVoucher.getText();
            if (codiceVoucher == null || codiceVoucher.trim().isEmpty()) {
                mostraWarning("Voucher richiesto", "Inserisci un codice voucher.");
                return;
            }
            if (righeOrdineObservable.isEmpty()) {
                mostraWarning("Ordine vuoto", "Aggiungi almeno un prodotto prima di applicare un voucher.");
                return;
            }

            RiepilogoOrdineBean riepilogo = facade.applicaVoucher(codiceVoucher);
            aggiornaVistaConRiepilogo(riepilogo);

            textFieldVoucher.setDisable(true);
            btnApplicaVoucher.setDisable(true);
            btnRimuoviVoucher.setDisable(false);

            mostraInfo("Voucher applicato",
                    "Voucher " + codiceVoucher.toUpperCase() + " applicato con successo!\n" +
                            "Sconto: " + riepilogo.getScontoFormattato());

        } catch (CreaOrdineFacade.CreaOrdineException e) {
            mostraErrore("Voucher non valido", e.getMessage());
            textFieldVoucher.selectAll();
            textFieldVoucher.requestFocus();
        }
    }

    @FXML
    private void onRimuoviVoucher() {
        try {
            if (!mostraConferma("Rimuovi Voucher", "Vuoi rimuovere il voucher applicato?")) {
                return;
            }

            RiepilogoOrdineBean riepilogo = facade.rimuoviVoucher();
            aggiornaVistaConRiepilogo(riepilogo);

            textFieldVoucher.setDisable(false);
            textFieldVoucher.clear();
            btnApplicaVoucher.setDisable(true);
            btnRimuoviVoucher.setDisable(true);

        } catch (CreaOrdineFacade.CreaOrdineException e) {
            mostraErrore("Errore", e.getMessage());
        }
    }

    @FXML
    private void onConfermaOrdine() {
        try {
            if (righeOrdineObservable.isEmpty()) {
                mostraWarning("Ordine vuoto", "Aggiungi almeno un prodotto all'ordine prima di confermare.");
                return;
            }

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
                navigaToPagamento();
            } else {
                mostraErrore("Errore", "Si è verificato un errore durante la conferma dell'ordine.");
            }

        } catch (CreaOrdineFacade.CreaOrdineException e) {
            mostraErrore("Errore", e.getMessage());
        }
    }

    @FXML
    private void onAnnullaOrdine() {
        if (righeOrdineObservable.isEmpty()) {
            navigaToHome();
            return;
        }

        if (mostraConferma("Annulla Ordine",
                "Sei sicuro di voler annullare l'ordine?\nTutti i prodotti selezionati verranno rimossi.")) {

            facade.annullaOrdine();

            try {
                resetVistaCompleta();
                iniziaNuovoOrdine();
            } catch (CreaOrdineFacade.CreaOrdineException e) {
                mostraErrore("Errore", e.getMessage());
            }
        }
    }

    private void aggiornaRiepilogo() throws CreaOrdineFacade.CreaOrdineException {
        RiepilogoOrdineBean riepilogo = facade.getRiepilogo();
        aggiornaVistaConRiepilogo(riepilogo);
    }

    private void aggiornaVistaConRiepilogo(RiepilogoOrdineBean riepilogo) {
        if (riepilogo == null) return;

        righeOrdineObservable.clear();
        righeOrdineObservable.addAll(riepilogo.getRigheOrdine());

        labelSubtotale.setText(riepilogo.getSubtotaleFormattato());
        labelTotale.setText(riepilogo.getTotaleFormattato());
        labelDurata.setText(riepilogo.getDurataFormattata());

        if (riepilogo.isVoucherApplicato()) {
            if (labelSconto != null) labelSconto.setText(riepilogo.getScontoFormattato());
            if (labelVoucherInfo != null) labelVoucherInfo.setText(riepilogo.getCodiceVoucher() + " - " + riepilogo.getDescrizioneVoucher());
            if (panelSconto != null) {
                panelSconto.setVisible(true);
                panelSconto.setManaged(true);
            }
        } else {
            if (labelSconto != null) labelSconto.setText("€0.00");
            if (labelVoucherInfo != null) labelVoucherInfo.setText("");
            if (panelSconto != null) {
                panelSconto.setVisible(false);
                panelSconto.setManaged(false);
            }
        }

        btnConfermaOrdine.setDisable(righeOrdineObservable.isEmpty());
    }

    private void resetSelezioniAddOn() {
        if (checkCipolla != null) checkCipolla.setSelected(false);
        if (checkSalsaYogurt != null) checkSalsaYogurt.setSelected(false);
        if (checkPatatine != null) checkPatatine.setSelected(false);
        if (checkMixVerdure != null) checkMixVerdure.setSelected(false);
    }

    private void resetVistaCompleta() {
        resetSelezioniAddOn();
        if (comboProdottoBase.getItems() != null && !comboProdottoBase.getItems().isEmpty()) {
            comboProdottoBase.getSelectionModel().selectFirst();
        }

        textFieldVoucher.clear();
        textFieldVoucher.setDisable(false);
        btnApplicaVoucher.setDisable(true);
        btnRimuoviVoucher.setDisable(true);

        righeOrdineObservable.clear();

        labelSubtotale.setText("€0.00");
        if (labelSconto != null) labelSconto.setText("€0.00");
        labelTotale.setText("€0.00");
        labelDurata.setText("0 min");
        if (labelVoucherInfo != null) labelVoucherInfo.setText("");

        if (panelSconto != null) {
            panelSconto.setVisible(false);
            panelSconto.setManaged(false);
        }
    }

    private String costruisciMessaggioConferma(RiepilogoOrdineBean riepilogo) {
        StringBuilder sb = new StringBuilder();
        sb.append("Riepilogo Ordine #").append(riepilogo.getNumeroOrdine()).append("\n\n");
        sb.append("Prodotti:\n");
        for (RigaOrdineBean riga : riepilogo.getRigheOrdine()) {
            sb.append("• ").append(riga.getDescrizione())
                    .append(" - ").append(riga.getPrezzoFormattato()).append("\n");
        }
        sb.append("\nSubtotale: ").append(riepilogo.getSubtotaleFormattato()).append("\n");
        if (riepilogo.isVoucherApplicato()) {
            sb.append("Sconto (").append(riepilogo.getCodiceVoucher()).append("): ")
                    .append(riepilogo.getScontoFormattato()).append("\n");
        }
        sb.append("TOTALE: ").append(riepilogo.getTotaleFormattato()).append("\n\n");
        sb.append("Tempo di preparazione: ").append(riepilogo.getDurataFormattata()).append("\n\n");
        sb.append("Vuoi confermare l'ordine?");
        return sb.toString();
    }

    private void navigaToPagamento() {
        // PageNavigationController.getInstance().navigateTo("PagamentoView.fxml");
    }

    private void navigaToHome() {
        // PageNavigationController.getInstance().navigateTo("HomeView.fxml");
    }

    private void mostraErrore(String titolo, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

    private void mostraWarning(String titolo, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

    private void mostraInfo(String titolo, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

    private boolean mostraConferma(String titolo, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}