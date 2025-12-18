package org.example.use_cases.storico_ordini.graphic_controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.use_cases.crea_ordine.beans.OrdineBean;
import org.example.use_cases.storico_ordini.StoricoOrdiniFacade;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class StoricoOrdiniGUIController implements Initializable {

    @FXML
    private TableView<OrdineBean> tableViewOrdini;

    @FXML
    private TableColumn<OrdineBean, Long> colNumeroOrdine;

    @FXML
    private TableColumn<OrdineBean, LocalDateTime> colData;

    @FXML
    private TableColumn<OrdineBean, String> colStato;

    @FXML
    private TableColumn<OrdineBean, Double> colTotale;

    private final StoricoOrdiniFacade facade = new StoricoOrdiniFacade();
    private ObservableList<OrdineBean> ordiniList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
        loadData();
    }

    private void setupTable() {
        colNumeroOrdine.setCellValueFactory(new PropertyValueFactory<>("numeroOrdine"));
        colData.setCellValueFactory(new PropertyValueFactory<>("dataCreazione"));
        colStato.setCellValueFactory(new PropertyValueFactory<>("stato"));
        colTotale.setCellValueFactory(new PropertyValueFactory<>("totale"));

        tableViewOrdini.setItems(ordiniList);
    }

    private void loadData() {
        try {
            List<OrdineBean> ordini = facade.getStoricoOrdini();
            ordiniList.setAll(ordini);
        } catch (Exception e) {
            // Gestione errore (es. log o alert se possibile)
            e.printStackTrace();
        }
    }

    @FXML
    private void handleIndietro(javafx.event.ActionEvent event) {
        org.example.PageNavigationController.getInstance().returnToMainPage();
    }
}
