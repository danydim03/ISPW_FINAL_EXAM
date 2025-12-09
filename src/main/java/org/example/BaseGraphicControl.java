package org.example;

import org.example.enums.ExceptionMessagesEnum;
import org.example.enums.UserErrorMessagesEnum;
import org.example.exceptions.*;
import org.example.Facades.UserFacade;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class BaseGraphicControl implements Initializable {

    @FXML
    private StackPane content;
    @FXML
    private Button backButton;
    private final UserFacade facade;
    @FXML
    private Button accountButton;
    @FXML
    private Button notificationButton;
    @FXML
    private VBox pendingEventList;

    public BaseGraphicControl() {
        this.facade = new UserFacade();
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

    void returnToMainPage() {
        while (content.getChildren().size() > 1)
            goBack();
    }

    void openMainPage(Node node, String name) {
        content.getChildren().clear();
        content.getChildren().add(node);
        accountButton.setText(name);
        accountButton.setVisible(true);
        notificationButton.setVisible(true);
    }
}
