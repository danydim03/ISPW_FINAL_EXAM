package org.example;

import org.example.enums.ExceptionMessagesEnum;
import org.example.enums.FrontEndTypeEnum;
import org.example.exceptions.PropertyException;
import org.example.exceptions.ResourceNotFoundException;
import org.example.loggers_general.GeneralLogger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainApplication extends Application {
    private static final Logger LOGGER = Logger.getLogger(MainApplication.class.getName());

    // Costante per evitare duplicazione della riga della cornice
    private static final String BOX_EMPTY_ROW = "  ║                                                               ║";
    @Override
    public void start(Stage stage) throws IOException {
        Pane basePane = loadLoginPane();
        if (basePane != null) {
            Scene scene = new Scene(basePane);
            stage.setTitle("ISPW PROJECT 2026");
            stage.setScene(scene);
        }
        stage.setResizable(true);
        stage.setMinWidth(950);
        stage.setMinHeight(700);
        stage.show();
    }

    public static void main(String[] args) {
        try {

            FrontEndTypeEnum frontEndType = FrontEndTypeEnum
                    .getFrontEndTypeByValue(PropertiesHandler.getInstance().getProperty("front_end_type"));
            if (frontEndType != null)
                switch (frontEndType) {
                    case JAVAFX -> launch(args);
                    case CLI -> {
                        launchCLI();
                    }
                    default -> throw new PropertyException(ExceptionMessagesEnum.UNEXPECTED_PROPERTY_NAME.message);
                }
            else
                throw new PropertyException(ExceptionMessagesEnum.UNEXPECTED_PROPERTY_NAME.message);
        } catch (ResourceNotFoundException | PropertyException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    private static void launchCLI() {
        System.out.println("\n");
        System.out.println("  ╔═══════════════════════════════════════════════════════════════╗");
        System.out.println(BOX_EMPTY_ROW);
        System.out.println("  ║     🥙  HABIBI SHAWARMA - Sistema Gestione Ordini  🥙          ║");
        System.out.println(BOX_EMPTY_ROW);
        System.out.println("  ║          Interfaccia a Linea di Comando (CLI)                 ║");
        System.out.println(BOX_EMPTY_ROW);
        System.out.println("  ╚═══════════════════════════════════════════════════════════════╝");
        System.out.println("\n");

        // Start the CLI navigation controller
        org.example.cli.CLINavigationController.getInstance().start();
    }


    private Pane loadLoginPane() {
        try {
            // Usa il percorso assoluto dalle risorse, partendo dalla root
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/org/example/base_view.fxml"));

            // Verifica che la risorsa esista prima di procedere
            if (loader.getLocation() == null) {
                GeneralLogger.logSevere("File FXML non trovato: /org/example/base_view.fxml");
                return null;
            }

            Pane baseView = loader.load();
            PageNavigationController.getInstance().setBaseGraphicController(loader.getController());
            PageNavigationController.getInstance().navigateTo("login");
            return baseView;
        } catch (IOException e) {
            GeneralLogger.logSevere("Errore durante il caricamento del file FXML: " + e.getMessage());
            return null;
        } catch (NullPointerException e) {
            GeneralLogger.logSevere("Risorsa FXML non trovata");
            return null;
        }
    }
}