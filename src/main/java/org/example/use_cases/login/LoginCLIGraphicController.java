package org.example.use_cases.login;

import org.example.BaseCLIGraphicController;
import org.example.beans_general.LoginBean;

/**
 * CLI Graphic Controller for the Login view.
 * Handles user authentication via console input.
 * Uses LoginControl (Application Controller) for business logic.
 */
public class LoginCLIGraphicController extends BaseCLIGraphicController {

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

            if (email.equalsIgnoreCase("exit") || email.equalsIgnoreCase("esci")) {
                exitRequested = true;
                continue;
            }

            String password = readPassword("🔐 Password");

            if (password.equalsIgnoreCase("exit") || password.equalsIgnoreCase("esci")) {
                exitRequested = true;
                continue;
            }

            // Attempt login using the Application Controller
            try {
                // Validate email format first
                loginControl.emailMatches(email);

                // Attempt authentication
                loginBean = loginControl.login(email, password);
                authenticated = true;

                // Set the current session token for session-based features
                org.example.session_manager.SessionManager.getInstance()
                        .setCurrentTokenKey(loginBean.getTokenKey());

                showSuccess("Login effettuato con successo!");
                System.out.println("  Benvenuto, " + loginBean.getUserBean().getName() + " " +
                        loginBean.getUserBean().getSurname() + "!");
                System.out.println("  Ruolo: " + loginBean.getUserBean().getRoleEnum().toString());

                waitForEnter();

            } catch (Exception e) {
                showError(getErrorMessage(e));
                System.out.println("\n  Riprova oppure digita 'esci' per uscire.");
                waitForEnter();
            }
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
