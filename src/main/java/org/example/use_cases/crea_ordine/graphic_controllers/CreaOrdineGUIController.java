package org.example.use_cases.crea_ordine.graphic_controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.BaseGraphicControl;
import org.example.PageNavigationController;
import org.example.exceptions.*;
import org.example.use_cases.crea_ordine.CreaOrdineFacade;
import org.example.use_cases.crea_ordine.beans.FoodBean;
import org.example.use_cases.crea_ordine.beans.OrdineBean;
import org.example.use_cases.crea_ordine.beans.RiepilogoOrdineBean;
import org.example.use_cases.crea_ordine.beans.RiepilogoOrdineBean.RigaOrdineBean;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller Grafico (Boundary) per lo Use Case "Crea Ordine".
 * 
 * <p>
 * Implementa il <b>Pull Model</b>: la View richiede esplicitamente i dati
 * al Controller (tramite Facade) solo quando necessario, tipicamente dopo
 * un'azione dell'utente.
 * </p>
 * 
 * <p>
 * Flusso Pull Model:
 * </p>
 * <ol>
 * <li>Utente interagisce con la UI (click, input)</li>
 * <li>View richiede azione al Facade (es. aggiungiProdotto)</li>
 * <li>View richiede stato aggiornato (es. getRiepilogoOrdine)</li>
 * <li>View aggiorna la UI con i dati ricevuti</li>
 * </ol>
 * 
 * <p>
 * Pattern architetturali:
 * </p>
 * <ul>
 * <li><b>BCE/MVC</b>: questa classe è il Boundary (View)</li>
 * <li><b>Facade</b>: comunica solo con CreaOrdineFacade</li>
 * <li><b>Bean</b>: scambia dati tramite DTO (FoodBean,
 * RiepilogoOrdineBean)</li>
 * </ul>
 */
public class CreaOrdineGUIController extends BaseGraphicControl implements Initializable {

    private static final Logger logger = Logger.getLogger(CreaOrdineGUIController.class.getName());
    private static final String ERROR_TITLE = "Errore";
    private static final String ADDON_CIPOLLA = "Cipolla";
    private static final String ADDON_PATATINE = "Patatine";
    private static final String ZERO_CURRENCY = "€0.00";

    @FXML
    private RadioButton radioPanino;
    @FXML
    private RadioButton radioPiadina;
    @FXML
    private RadioButton radioPiatto;
    private ToggleGroup baseGroup;

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
    private String ordineId; // Stato UI: ID dell'ordine corrente
    private List<FoodBean> prodottiBaseDisponibili;
    private List<FoodBean> addOnDisponibili;
    private ObservableList<RigaOrdineBean> righeOrdineObservable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        righeOrdineObservable = FXCollections.observableArrayList();

        try {
            setupTabella();
            setupListeners();

            // Verifica che l'utente sia loggato
            String tokenKey = PageNavigationController.getInstance().getSessionTokenKey();
            if (tokenKey == null) {
                logger.severe("Utente non loggato - token null");
                mostraErrore("Errore di sessione", "Devi effettuare il login per creare un ordine.");
                return;
            }

            // 1. Prima crea Facade (richiesto per caricamento dati)
            facade = new CreaOrdineFacade(tokenKey);

            // 2. Poi carica dati via Facade (richiede Facade inizializzato)
            caricaDatiIniziali();

            // 3. Inizializza nuovo ordine
            iniziaNuovoOrdine();

        } catch (MissingAuthorizationException e) {
            logger.log(Level.SEVERE, "Errore di autorizzazione", e);
            mostraErrore("Errore di autorizzazione", e.getMessage());
        } catch (CreaOrdineException e) {
            logger.log(Level.SEVERE, "Errore di inizializzazione ordine", e);
            mostraErrore("Errore di inizializzazione", e.getMessage());
        } catch (HabibiException e) {
            logger.log(Level.SEVERE, "Errore imprevisto", e);
            mostraErrore(ERROR_TITLE, "Si è verificato un errore: " + e.getMessage());
        }
    }

    // Imposta le colonne della tabella e il placeholder
    private void setupTabella() {
        colonnaDescrizione.setCellValueFactory(new PropertyValueFactory<>("descrizione"));
        colonnaPrezzo.setCellValueFactory(new PropertyValueFactory<>("prezzoFormattato"));
        if (colonnaDurata != null) {
            colonnaDurata.setCellValueFactory(
                    cellData -> new SimpleStringProperty(cellData.getValue().getDurata() + " min"));
        }
        tabellaOrdine.setItems(righeOrdineObservable);
        tabellaOrdine.setPlaceholder(new Label("Nessun prodotto nell'ordine"));
    }

    // Imposta i listener per i componenti UI
    // Configura i gruppi di toggle, i listener per la selezione della tabella e i
    // campi di testo
    // Inizializza lo stato dei pulsanti e dei pannelli
    private void setupListeners() {
        baseGroup = new ToggleGroup();
        radioPanino.setToggleGroup(baseGroup);
        radioPiadina.setToggleGroup(baseGroup);
        radioPiatto.setToggleGroup(baseGroup);
        radioPanino.setSelected(true);

        tabellaOrdine.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> btnRimuoviProdotto.setDisable(newSelection == null));

        textFieldVoucher.textProperty().addListener(
                (obs, oldText, newText) -> btnApplicaVoucher.setDisable(newText == null || newText.trim().isEmpty()));

        btnRimuoviProdotto.setDisable(true);
        btnApplicaVoucher.setDisable(true);
        btnRimuoviVoucher.setDisable(true);

        if (panelSconto != null) {
            panelSconto.setVisible(false);
            panelSconto.setManaged(false);
        }
    }

    /**
     * Carica prodotti base e add-on dal database via Facade.
     * 
     * <p>
     * <b>PULL MODEL</b>: la View richiede esplicitamente i dati iniziali
     * al Facade. I dati vengono "tirati" (pulled) dalla View quando serve,
     * non "spinti" (pushed) automaticamente dal Model.
     * </p>
     * 
     * Sostituisce i dati hardcoded con dati dinamici dal layer di persistenza.
     */
    private void caricaDatiIniziali() {
        try {
            prodottiBaseDisponibili = facade.getProdottiBaseDisponibili();
            addOnDisponibili = facade.getAddOnDisponibili();

            logger.log(Level.INFO, () -> "Caricati " + prodottiBaseDisponibili.size()
                    + " prodotti base e " + addOnDisponibili.size() + " add-on");

        } catch (DAOException | ObjectNotFoundException | MissingAuthorizationException
                | WrongListQueryIdentifierValue | UserNotFoundException
                | UnrecognizedRoleException e) {
            logger.log(Level.SEVERE, "Errore caricamento dati iniziali", e);
            mostraErrore(ERROR_TITLE, "Impossibile caricare i prodotti: " + e.getMessage());

            // Fallback: liste vuote per evitare NullPointerException
            prodottiBaseDisponibili = List.of();
            addOnDisponibili = List.of();
        }
    }

    /**
     * Restituisce il FoodBean corrispondente al prodotto base selezionato.
     * Usa ricerca dinamica per classe invece di indice hardcoded.
     */
    private FoodBean getProdottoBaseSelezionato() {
        Toggle selected = baseGroup.getSelectedToggle();
        if (selected == null || prodottiBaseDisponibili.isEmpty()) {
            return null;
        }

        String classeRichiesta;
        if (selected == radioPanino) {
            classeRichiesta = "PaninoDonerKebab";
        } else if (selected == radioPiadina) {
            classeRichiesta = "PiadinaDonerKebab";
        } else if (selected == radioPiatto) {
            classeRichiesta = "KebabAlPiatto";
        } else {
            return null;
        }

        return prodottiBaseDisponibili.stream()
                .filter(f -> classeRichiesta.equals(f.getClasse()))
                .findFirst()
                .orElse(null);
    }

    private void iniziaNuovoOrdine() throws CreaOrdineException {
        try {
            // Inizializza ordine e salva l'ID (stato UI)
            OrdineBean ordine = facade.inizializzaNuovoOrdine();
            this.ordineId = String.valueOf(ordine.getNumeroOrdine());

            if (labelNumeroOrdine != null && ordine.getNumeroOrdine() != null) {
                labelNumeroOrdine.setText(" Numero Ordine: " + ordine.getNumeroOrdine());
            }
            aggiornaRiepilogo();
        } catch (DAOException e) {
            throw new CreaOrdineException("Impossibile inizializzare l'ordine: " + e.getMessage(), e);
        }
    }

    @FXML
    private void onRimuoviProdotto() {
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

        boolean success = facade.rimuoviProdottoDaOrdine(ordineId, selectedIndex);
        if (success) {
            aggiornaRiepilogo();
        } else {
            mostraErrore(ERROR_TITLE, "Impossibile rimuovere il prodotto.");
        }
    }

    /**
     * Aggiunge il prodotto selezionato con gli add-on scelti all'ordine.
     * 
     * <p>
     * <b>PULL MODEL</b>: dopo l'azione di aggiunta, la View richiede
     * esplicitamente il riepilogo aggiornato chiamando {@code aggiornaRiepilogo()}.
     * Non riceve notifiche push dal Model.
     * </p>
     * 
     * Flusso:
     * <ol>
     * <li>Recupera il prodotto base selezionato</li>
     * <li>Crea FoodBean con add-on selezionati</li>
     * <li>PULL: richiede aggiunta al Facade</li>
     * <li>PULL: richiede riepilogo aggiornato</li>
     * <li>Aggiorna la UI</li>
     * </ol>
     */
    @FXML
    private void onAggiungiProdotto() {
        FoodBean prodottoSelezionato = getProdottoBaseSelezionato();
        if (prodottoSelezionato == null) {
            mostraWarning("Selezione richiesta", "Seleziona un prodotto base.");
            return;
        }

        FoodBean richiesta = new FoodBean();
        richiesta.setClasse(prodottoSelezionato.getClasse());
        richiesta.setDescrizione(prodottoSelezionato.getDescrizione());
        richiesta.setCosto(prodottoSelezionato.getCosto());
        richiesta.setDurata(prodottoSelezionato.getDurata());
        richiesta.setTipo(prodottoSelezionato.getTipo());

        if (checkCipolla != null && checkCipolla.isSelected())
            richiesta.aggiungiAddOn(ADDON_CIPOLLA);
        if (checkSalsaYogurt != null && checkSalsaYogurt.isSelected())
            richiesta.aggiungiAddOn("SalsaYogurt");
        if (checkPatatine != null && checkPatatine.isSelected())
            richiesta.aggiungiAddOn(ADDON_PATATINE);
        if (checkMixVerdure != null && checkMixVerdure.isSelected())
            richiesta.aggiungiAddOn("MixVerdureGrigliate");

        boolean success = facade.aggiungiProdottoAOrdine(ordineId, richiesta);
        if (success) {
            aggiornaRiepilogo();
            resetSelezioniAddOn();
            mostraInfo("Prodotto aggiunto", "Prodotto aggiunto all'ordine con successo!");
        } else {
            mostraErrore(ERROR_TITLE, "Impossibile aggiungere il prodotto.");
        }
    }

    /**
     * Applica un voucher all'ordine.
     * 
     * <p>
     * <b>PULL MODEL</b>: la View richiede l'applicazione del voucher
     * e successivamente richiede il riepilogo aggiornato.
     * </p>
     * 
     * Flusso:
     * <ol>
     * <li>Recupera codice voucher dal campo di testo</li>
     * <li>Valida input</li>
     * <li>PULL: richiede applicazione al Facade</li>
     * <li>PULL: richiede riepilogo aggiornato</li>
     * </ol>
     */
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

            var voucherBean = facade.applicaVoucher(ordineId, codiceVoucher);
            if (voucherBean != null) {
                aggiornaRiepilogo();
                textFieldVoucher.setDisable(true);
                btnApplicaVoucher.setDisable(true);
                btnRimuoviVoucher.setDisable(false);
                mostraInfo("Voucher applicato", "Voucher " + codiceVoucher.toUpperCase() + " applicato con successo!");
            } else {
                mostraErrore("Voucher non valido", "Il codice voucher inserito non è valido.");
                textFieldVoucher.selectAll();
                textFieldVoucher.requestFocus();
            }

        } catch (DAOException | ObjectNotFoundException | MissingAuthorizationException | WrongListQueryIdentifierValue
                | UserNotFoundException | UnrecognizedRoleException e) {
            mostraErrore("Voucher non valido", e.getMessage());
            textFieldVoucher.selectAll();
            textFieldVoucher.requestFocus();
        }
    }

    @FXML
    private void onRimuoviVoucher() {

        if (!mostraConferma("Rimuovi Voucher", "Vuoi rimuovere il voucher applicato?")) {
            return;
        }

        facade.rimuoviVoucher(ordineId);
        aggiornaRiepilogo();

        textFieldVoucher.setDisable(false);
        textFieldVoucher.clear();
        btnApplicaVoucher.setDisable(true);
        btnRimuoviVoucher.setDisable(true);

    }

    @FXML
    private void onConfermaOrdine() {
        try {
            // Verifica che l'ordine non sia vuoto
            if (righeOrdineObservable.isEmpty()) {
                mostraWarning("Ordine vuoto", "Aggiungi almeno un prodotto all'ordine prima di confermare.");
                return;
            }
            // Costruisce il riepilogo dell'ordine e mostra la conferma
            // Se l'utente conferma, chiama il facade per confermare l'ordine
            // Mostra messaggi di conferma o errore in base al risultato
            RiepilogoOrdineBean riepilogo = facade.getRiepilogoOrdine(ordineId);

            boolean success = facade.confermaOrdine(ordineId);
            if (success) {
                mostraInfo("Ordine confermato",
                        "Il tuo ordine #" + riepilogo.getNumeroOrdine() + " è stato confermato!\n\n" +
                                "Totale: " + riepilogo.getTotaleFormattato() + "\n" +
                                "Tempo di preparazione stimato: " + riepilogo.getDurataFormattata());
                org.example.PageNavigationController.getInstance().returnToMainPage();
            } else {
                mostraErrore(ERROR_TITLE, "Si è verificato un errore durante la conferma dell'ordine.");
            }

        } catch (DAOException | MissingAuthorizationException e) {
            mostraErrore(ERROR_TITLE, e.getMessage());
        }
    }

    @FXML
    private void onAnnullaOrdine() {
        if (righeOrdineObservable.isEmpty()) {
            org.example.PageNavigationController.getInstance().returnToMainPage();
            return;
        }

        if (mostraConferma("Annulla Ordine",
                "Sei sicuro di voler annullare l'ordine?\nTutti i prodotti selezionati verranno rimossi.")) {

            facade.annullaOrdine(ordineId);
            ordineId = null; // Reset dopo annullamento
            resetVistaCompleta();
            org.example.PageNavigationController.getInstance().returnToMainPage();
        }
    }

    /**
     * Richiede il riepilogo aggiornato dell'ordine e aggiorna la UI.
     * 
     * <p>
     * <b>PULL MODEL</b>: metodo centrale che implementa il "pull" dei dati.
     * La View richiede esplicitamente lo stato corrente al Facade.
     * </p>
     */
    private void aggiornaRiepilogo() {
        RiepilogoOrdineBean riepilogo = facade.getRiepilogoOrdine(ordineId);
        aggiornaVistaConRiepilogo(riepilogo);
    }

    private void aggiornaVistaConRiepilogo(RiepilogoOrdineBean riepilogo) {
        if (riepilogo == null)
            return;

        righeOrdineObservable.clear();
        righeOrdineObservable.addAll(riepilogo.getRigheOrdine());

        labelSubtotale.setText(riepilogo.getSubtotaleFormattato());
        labelTotale.setText(riepilogo.getTotaleFormattato());
        labelDurata.setText(riepilogo.getDurataFormattata());

        if (riepilogo.isVoucherApplicato()) {
            if (labelSconto != null)
                labelSconto.setText(riepilogo.getScontoFormattato());
            if (labelVoucherInfo != null)
                labelVoucherInfo.setText(riepilogo.getCodiceVoucher() + " - " + riepilogo.getDescrizioneVoucher());
            if (panelSconto != null) {
                panelSconto.setVisible(true);
                panelSconto.setManaged(true);
            }
        } else {
            if (labelSconto != null)
                labelSconto.setText(ZERO_CURRENCY);
            if (labelVoucherInfo != null)
                labelVoucherInfo.setText("");
            if (panelSconto != null) {
                panelSconto.setVisible(false);
                panelSconto.setManaged(false);
            }
        }

        btnConfermaOrdine.setDisable(righeOrdineObservable.isEmpty());
    }

    private void resetSelezioniAddOn() {
        if (checkCipolla != null)
            checkCipolla.setSelected(false);
        if (checkSalsaYogurt != null)
            checkSalsaYogurt.setSelected(false);
        if (checkPatatine != null)
            checkPatatine.setSelected(false);
        if (checkMixVerdure != null)
            checkMixVerdure.setSelected(false);
    }

    private void resetVistaCompleta() {
        resetSelezioniAddOn();
        radioPanino.setSelected(true);

        textFieldVoucher.clear();
        textFieldVoucher.setDisable(false);
        btnApplicaVoucher.setDisable(true);
        btnRimuoviVoucher.setDisable(true);

        righeOrdineObservable.clear();

        labelSubtotale.setText(ZERO_CURRENCY);
        if (labelSconto != null)
            labelSconto.setText(ZERO_CURRENCY);
        labelTotale.setText(ZERO_CURRENCY);
        labelDurata.setText("0 min");
        if (labelVoucherInfo != null)
            labelVoucherInfo.setText("");

        if (panelSconto != null) {
            panelSconto.setVisible(false);
            panelSconto.setManaged(false);
        }
    }

    // I metodi mostraErrore, mostraWarning, mostraInfo, mostraConferma
    // sono ora ereditati da BaseGraphicControl (GRASP: High Cohesion)
}
