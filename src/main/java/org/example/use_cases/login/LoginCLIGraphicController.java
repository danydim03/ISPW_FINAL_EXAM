package org.example.use_cases.login;

import org.example.BaseCLIGraphicController;
import org.example.beans_general.LoginBean;
import org.example.exceptions.HabibiException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CLI Graphic Controller for the Login view.
 * Handles user authentication via console input.
 * Uses LoginControl (Application Controller) for business logic.
 */
public class LoginCLIGraphicController extends BaseCLIGraphicController {

    private static final Logger logger = Logger.getLogger(LoginCLIGraphicController.class.getName());
    private final LoginControl loginControl;
    private LoginBean loginBean;
    private boolean exitRequested = false;

    public LoginCLIGraphicController() {
        super();
        this.loginControl = new LoginControl();
    }

    @Override
    public void start() {
        boolean authenticated = false;

        while (!authenticated && !exitRequested) {
            printHeader("LOGIN");

            System.out.println("  Benvenuto nel sistema di gestione ordini!");
            System.out.println("  Inserisci le tue credenziali per accedere.\n");
            System.out.println(THIN_SEPARATOR);

            // Prompt for credentials
            String email = readInput("📧 Email");

            if (isExitCommand(email)) {
                exitRequested = true;
                return;
            }

            String password = readPassword("🔐 Password");

            if (isExitCommand(password)) {
                exitRequested = true;
                return;
            }

            // Attempt login using the Application Controller
            authenticated = attemptLogin(email, password);
        }
    }

    /**
     * Checks if the input is an exit command
     */
    private boolean isExitCommand(String input) {
        return input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("esci");
    }

    /**
     * Attempts login and handles all exceptions
     * 
     * @return true if login was successful
     */
    private boolean attemptLogin(String email, String password) {
        try {
            // Validate email format first
            loginControl.emailMatches(email);

            // Attempt authentication
            loginBean = loginControl.login(email, password);

            // Set the current session token for session-based features
            org.example.session_manager.SessionManager.getInstance()
                    .setCurrentTokenKey(loginBean.getTokenKey());

            showSuccess("Login effettuato con successo!");
            System.out.println("  Benvenuto, " + loginBean.getUserBean().getName() + " " +
                    loginBean.getUserBean().getSurname() + "!");
            System.out.println("  Ruolo: " + loginBean.getUserBean().getRoleEnum().toString());

            waitForEnter();
            return true;

        } catch (HabibiException e) {
            logger.log(Level.WARNING, "Errore di login", e);
            showError(getErrorMessage(e));
            System.out.println("\n  Riprova oppure digita 'esci' per uscire.");
            waitForEnter();
            return false;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Errore imprevisto durante il login", e);
            showError("Errore imprevisto durante il login");
            System.out.println("\n  Riprova oppure digita 'esci' per uscire.");
            waitForEnter();
            return false;
        }
    }

    /**
     * Converts exception to user-friendly message
     */
    private String getErrorMessage(Exception e) {
        String message = e.getMessage();

        if (message == null) {
            return "Errore durante il login";
        }

        // Translate common error messages
        if (message.contains("Email") || message.contains("email")) {
            return "Formato email non valido";
        }
        if (message.contains("password") || message.contains("Password")) {
            return "Password non corretta";
        }
        if (message.contains("not found") || message.contains("non trovato")) {
            return "Utente non trovato";
        }

        return message;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public boolean isExitRequested() {
        return exitRequested;
    }
}
