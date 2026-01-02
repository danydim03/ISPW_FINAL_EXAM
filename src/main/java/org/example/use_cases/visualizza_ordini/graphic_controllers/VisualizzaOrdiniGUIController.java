package org.example.use_cases.visualizza_ordini.graphic_controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.example.use_cases.crea_ordine.beans.OrdineBean;
import org.example.use_cases.visualizza_ordini.VisualizzaOrdiniFacade;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.example.exceptions.HabibiException;

public class VisualizzaOrdiniGUIController {

    private static final Logger logger = Logger.getLogger(VisualizzaOrdiniGUIController.class.getName());

    @FXML
    private TableView<OrdineBean> tabellaOrdini;

    @FXML
    private TableColumn<OrdineBean, String> colonnaNumero;

    @FXML
    private TableColumn<OrdineBean, String> colonnaData;

    @FXML
    private TableColumn<OrdineBean, String> colonnaCliente;

    @FXML
    private TableColumn<OrdineBean, String> colonnaTotale;

    @FXML
    private TableColumn<OrdineBean, Void> colonnaAzioni;

    private final VisualizzaOrdiniFacade facade = new VisualizzaOrdiniFacade();
    private final ObservableList<OrdineBean> listaOrdini = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configuraTabella();
        caricaOrdini();
    }

    private void configuraTabella() {
        colonnaNumero.setCellValueFactory(
                cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNumeroOrdine())));

        colonnaData.setCellValueFactory(cellData -> {
            if (cellData.getValue().getDataCreazione() != null) {
                return new SimpleStringProperty(
                        cellData.getValue().getDataCreazione().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            }
            return new SimpleStringProperty("-");
        });

        colonnaCliente.setCellValueFactory(new PropertyValueFactory<>("clienteId"));

        colonnaTotale.setCellValueFactory(
                cellData -> new SimpleStringProperty(String.format("%.2f", cellData.getValue().getTotale())));

        aggiungiBottoneConsegna();
    }

    private void aggiungiBottoneConsegna() {
        Callback<TableColumn<OrdineBean, Void>, TableCell<OrdineBean, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<OrdineBean, Void> call(final TableColumn<OrdineBean, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Consegna");

                    {
                        btn.setOnAction(event -> {
                            OrdineBean ordine = getTableView().getItems().get(getIndex());
                            handleConsegna(ordine);
                        });
                        btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };

        colonnaAzioni.setCellFactory(cellFactory);
    }

    private void caricaOrdini() {
        try {
            List<OrdineBean> ordini = facade.getOrdiniInCreazione();
            listaOrdini.setAll(ordini);
            tabellaOrdini.setItems(listaOrdini);
        } catch (HabibiException e) {
            logger.log(Level.SEVERE, "Errore nel caricamento ordini", e);
            mostraErrore("Errore Caricamento", "Impossibile caricare gli ordini: " + e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Errore imprevisto nel caricamento ordini", e);
            mostraErrore("Errore Caricamento", "Impossibile caricare gli ordini: " + e.getMessage());
        }
    }

    private void handleConsegna(OrdineBean ordine) {
        try {
            facade.impostaInConsegna(ordine);
            mostraInfo("Successo", "Ordine #" + ordine.getNumeroOrdine() + " passato in consegna!");
            caricaOrdini(); // Ricarica la lista
        } catch (HabibiException e) {
            logger.log(Level.SEVERE, "Errore nell'aggiornamento ordine", e);
            mostraErrore("Errore Aggiornamento", "Impossibile aggiornare l'ordine: " + e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Errore imprevisto nell'aggiornamento ordine", e);
            mostraErrore("Errore Aggiornamento", "Impossibile aggiornare l'ordine: " + e.getMessage());
        }
    }

    private void mostraErrore(String titolo, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titolo);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

    private void mostraInfo(String titolo, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titolo);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

    @FXML
    private void handleIndietro(javafx.event.ActionEvent event) {
        org.example.PageNavigationController.getInstance().returnToMainPage();
    }
}
