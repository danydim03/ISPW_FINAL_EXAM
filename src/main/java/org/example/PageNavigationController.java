package org.example;

import org.example.beans_general.UserBean;
import org.example.enums.UserErrorMessagesEnum;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import org.example.model.user.UserData;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class PageNavigationController {

    private static final String FILE_EXTENSION = ".fxml";
    private static final String RESOURCE_BASE_PATH = "/org/example/";
    private static PageNavigationController instance;
    private static final Logger logger = Logger.getLogger(PageNavigationController.class.getName());

    private BaseGraphicControl baseGraphicController;
    private UserData userData;
    private StackPane contentPane; // Riferimento al StackPane "content" in base_view.fxml

    private PageNavigationController() {
    }

    public static synchronized PageNavigationController getInstance() {
        if (instance == null)
            instance = new PageNavigationController();
        return instance;
    }

    /**
     * Imposta il riferimento al StackPane principale
     */
    public void setContentPane(StackPane contentPane) {
        this.contentPane = contentPane;
    }

    /**
     * Imposta direttamente un Parent come contenuto
     */
    public void setContent(Parent content) {
        if (contentPane != null) {
            contentPane.getChildren().clear();
            contentPane.getChildren().add(content);
        } else {
            logger.log(Level.WARNING, "ContentPane non inizializzato");
        }
    }

    /**
     * Naviga a una vista FXML specifica usando il contentPane (percorso relativo a
     * /org/example/)
     */
    public void navigateToFXML(String fxmlPath) {
        String pathToLoad = fxmlPath;
        if (!pathToLoad.endsWith(FILE_EXTENSION)) {
            pathToLoad = pathToLoad.concat(FILE_EXTENSION);
        }

        final String finalPath = pathToLoad;
        String fullPath = RESOURCE_BASE_PATH + finalPath;
        logger.log(Level.INFO, () -> ">>> Tentativo caricamento: " + fullPath);
        logger.log(Level.INFO, () -> ">>> contentPane è null? " + (contentPane == null));

        try {
            URL resource = PageNavigationController.class.getResource(fullPath);
            logger.log(Level.INFO, () -> ">>> Risorsa trovata? " + (resource != null));

            if (resource == null) {
                logger.log(Level.SEVERE, () -> ">>> RISORSA NON TROVATA: " + fullPath);
                return;
            }

            FXMLLoader loader = new FXMLLoader(resource);
            Parent view = loader.load();
            logger.log(Level.INFO, ">>> View caricata con successo");

            setContent(view);
            logger.log(Level.INFO, ">>> Contenuto impostato");

        } catch (IOException e) {
            logger.log(Level.SEVERE, () -> "Errore caricamento FXML: " + finalPath);
            e.printStackTrace(); // Stampa lo stack trace completo
            showAlert(Alert.AlertType.ERROR, UserErrorMessagesEnum.RESOURCE_LOADING_TITLE.message,
                    UserErrorMessagesEnum.RESOURCE_LOADING_MSG.message, e);
        }
    }

    /**
     * Naviga a una vista FXML, imposta il contenuto e restituisce il controller
     * associato
     */
    public <T> T navigateToAndGetControllerFXML(String fxmlPath) {
        if (!fxmlPath.endsWith(FILE_EXTENSION)) {
            fxmlPath = fxmlPath.concat(FILE_EXTENSION);
        }
        try {
            FXMLLoader loader = new FXMLLoader(
                    Objects.requireNonNull(PageNavigationController.class.getResource(RESOURCE_BASE_PATH + fxmlPath)));
            Parent view = loader.load();
            setContent(view);
            return loader.getController();
        } catch (IOException e) {
            final String pathForLog = fxmlPath;
            logger.log(Level.SEVERE, () -> "Errore caricamento FXML: " + pathForLog);
            showAlert(Alert.AlertType.ERROR, UserErrorMessagesEnum.RESOURCE_LOADING_TITLE.message,
                    UserErrorMessagesEnum.RESOURCE_LOADING_MSG.message, e);
            return null;
        }
    }

    public void openMainPage(String sessionTokenKey, UserBean userBean) {
        setUserData(userBean, sessionTokenKey);
        String viewName = "";
        switch (userBean.getRole()) {
            case 1 -> viewName = "homepage_Cliente2";
            case 2 -> viewName = "homepage_Kebabbaro";
            case 3 -> viewName = "homepage_Amministratore";
            default ->
                showAlert(Alert.AlertType.ERROR, UserErrorMessagesEnum.ROLE_ERROR_TITLE.message,
                        UserErrorMessagesEnum.ROLE_ERROR_MSG.message);
        }
        viewName = viewName.concat(FILE_EXTENSION);
        try {
            baseGraphicController.openMainPage(
                    FXMLLoader.load(Objects.requireNonNull(PageNavigationController.class.getResource(viewName))),
                    String.format("%c%c", this.userData.getUserName().toUpperCase().charAt(0),
                            this.userData.getUserSurname().toUpperCase().charAt(0)));
        } catch (IOException e) {
            final String viewForLog = viewName;
            logger.log(Level.SEVERE, () -> "Errore caricamento risorsa: " + viewForLog);
            showAlert(Alert.AlertType.ERROR, UserErrorMessagesEnum.RESOURCE_LOADING_TITLE.message,
                    UserErrorMessagesEnum.RESOURCE_LOADING_MSG.message, e);
        }
    }

    /**
     * Loads a specific .fxml file into a view and navigates to it
     *
     * @param pageName the name of the view (without the '.fxml' suffix) to be
     *                 displayed
     */
    public void navigateTo(String pageName) {
        try {
            String resourcePath = RESOURCE_BASE_PATH + pageName + FILE_EXTENSION;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
            Parent view = loader.load();
            setContent(view);
        } catch (IOException e) {
            logger.severe("Errore caricando la pagina: " + pageName + " -> " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Errore di navigazione", "Impossibile aprire la pagina: " + pageName);
        }
    }

    /**
     * Navigates to under construction page
     */
    public void navigateToUnderConstructionPage() {
        navigateTo("under_construction");
    }

    public void setBaseGraphicController(BaseGraphicControl baseGraphicController) {
        this.baseGraphicController = baseGraphicController;
    }

    public void returnToMainPage() {
        if (this.userData == null) {
            navigateTo("login");
            return;
        }

        String viewName = "";
        switch (this.userData.getUserRole()) {
            case 1 -> viewName = "homepage_Cliente2";
            case 2 -> viewName = "homepage_Kebabbaro";
            case 3 -> viewName = "homepage_Amministratore";
            default -> {
                showAlert(Alert.AlertType.ERROR, UserErrorMessagesEnum.ROLE_ERROR_TITLE.message,
                        UserErrorMessagesEnum.ROLE_ERROR_MSG.message);
                return;
            }
        }

        if (!viewName.endsWith(FILE_EXTENSION)) {
            viewName = viewName.concat(FILE_EXTENSION);
        }

        try {
            String initials = String.format("%c%c",
                    this.userData.getUserName().toUpperCase().charAt(0),
                    this.userData.getUserSurname().toUpperCase().charAt(0));

            baseGraphicController.openMainPage(
                    FXMLLoader.load(Objects
                            .requireNonNull(PageNavigationController.class.getResource(RESOURCE_BASE_PATH + viewName))),
                    initials);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Errore caricamento home: " + viewName, e);
            showAlert(Alert.AlertType.ERROR, UserErrorMessagesEnum.RESOURCE_LOADING_TITLE.message,
                    UserErrorMessagesEnum.RESOURCE_LOADING_MSG.message, e);
        }
    }

    public String getSessionTokenKey() {
        if (userData == null) {
            return null;
        }

        return this.userData.getSessionTokenKey();

    }

    public void setSessionTokenKey(String sessionTokenKey) {
        this.userData.setSessionTokenKey(sessionTokenKey);
    }

    public UserData getUserData() {
        return this.userData;
    }

    /**
     * Saves User data
     *
     * @param userBean a bean containing the User data
     */
    private void setUserData(UserBean userBean, String tokenKey) {
        this.userData = new UserData(
                userBean.getName(),
                userBean.getSurname(),
                userBean.getEmail(),
                userBean.getCodiceFiscale(),
                // userBean.getMatricola(),
                userBean.getRole(),
                tokenKey);
        // IMPORTANT: Also set the current token key in SessionManager for session
        // management
        org.example.session_manager.SessionManager.getInstance().setCurrentTokenKey(tokenKey);
    }

    // TBI implement user switch

    /**
     * Display an alert with custom type, title and message.
     * This method manages the alerts globally on the current
     * instance of the application when GUI user interface is applied
     *
     * @param alertType the type of Alert to be displayed
     * @param title     the title of the Alert pane
     * @param message   the message of the Alert pane
     */
    public void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    /**
     * Creates an alert message relative to a specific thrown exception,
     * with custom type, title and message. This method manages the alerts globally
     * on the current
     * * instance of the application when GUI user interface is applied
     *
     * @param alertType the type of Alert to be displayed
     * @param title     the title of the Alert pane
     * @param message   the message of the Alert pane
     * @param e         the Exception which caused the Alert to be shown
     */
    public void showAlert(Alert.AlertType alertType, String title, String message, Exception e) {
        logger.log(Level.SEVERE, e.getMessage());
        showAlert(alertType, title, message);
    }
}
