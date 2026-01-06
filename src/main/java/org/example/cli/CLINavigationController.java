package org.example.cli;

import org.example.beans_general.LoginBean;
import org.example.graphic_controllers_general.homepages.AdminHomepageCLIController;
import org.example.graphic_controllers_general.homepages.ClienteHomepageCLIController;
import org.example.enums.UserRoleEnum;
import org.example.session_manager.SessionManager;
import org.example.use_cases.login.LoginCLIGraphicController;

/**
 * Singleton controller for CLI navigation.
 * Manages routing between different CLI views based on user actions.
 */
public class CLINavigationController {

    private static CLINavigationController instance;
    private LoginBean currentLoginBean;
    private boolean running = true;

    private CLINavigationController() {
    }

    public static synchronized CLINavigationController getInstance() {
        if (instance == null) {
            instance = new CLINavigationController();
        }
        return instance;
    }

    /**
     * Starts the CLI application with the login screen
     */
    public void start() {
        running = true;

        while (running) {
            LoginCLIGraphicController loginController = new LoginCLIGraphicController();
            loginController.start();

            // After login, check if successful
            if (loginController.getLoginBean() != null) {
                currentLoginBean = loginController.getLoginBean();
                navigateToHomepage();
            } else {
                // User chose to exit
                running = false;
            }
        }

        System.out.println("\n  👋 Arrivederci! Grazie per aver usato Habibi Shawarma.\n");
    }

    /**
     * Navigates to the appropriate homepage based on user role
     */
    private void navigateToHomepage() {
        if (currentLoginBean == null || currentLoginBean.getUserBean() == null) {
            return;
        }

        UserRoleEnum role = currentLoginBean.getUserBean().getRoleEnum();
        String tokenKey = currentLoginBean.getTokenKey();

        switch (role) {
            case CLIENTE -> {
                ClienteHomepageCLIController clienteHomepage = new ClienteHomepageCLIController(tokenKey);
                clienteHomepage.start();
            }
            case AMMINISTRATORE -> {
                AdminHomepageCLIController adminHomepage = new AdminHomepageCLIController(tokenKey);
                adminHomepage.start();
            }
            case KEBABBARO -> {
                // Kebabbaro uses same view as Admin for now
                AdminHomepageCLIController kebabbaroHomepage = new AdminHomepageCLIController(tokenKey);
                kebabbaroHomepage.start();
            }
        }

        // After returning from homepage (logout), clear session
        try {
            SessionManager.getInstance().logout();
        } catch (Exception e) {
            // Ignore session cleanup errors
        }
        currentLoginBean = null;
    }
}
