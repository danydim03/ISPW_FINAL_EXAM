package org.example;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class BaseGraphicControl implements Initializable {

    @FXML
    private StackPane content;
    @FXML
    private Button backButton;
    @FXML
    private Button accountButton;

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
    }
}
