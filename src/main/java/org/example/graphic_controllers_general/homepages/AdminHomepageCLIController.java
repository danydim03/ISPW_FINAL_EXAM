package org.example.graphic_controllers_general.homepages;

import org.example.BaseCLIGraphicController;
import org.example.use_cases.visualizza_ordini.graphic_controllers.VisualizzaOrdiniCLIController;

/**
 * CLI Graphic Controller for Admin/Kebabbaro Homepage.
 * Displays menu options specific to admin/kebabbaro role.
 */
public class AdminHomepageCLIController extends BaseCLIGraphicController {

    private static final String CLI_NOT_IMPLEMENTED_MSG = "View CLI implementata in seguito";
    private static final String CLI_FUTURE_VERSION_MSG = "  in una futura versione dell'interfaccia CLI.";
    private boolean loggedOut = false;

    public AdminHomepageCLIController(String tokenKey) {
        super(tokenKey);
    }

    @Override
    public void start() {
        while (!loggedOut) {
            printHeader("HOMEPAGE AMMINISTRATORE");

            System.out.println("  Pannello di gestione\n");
            System.out.println(THIN_SEPARATOR);

            printMenuOption(1, "📋", "Visualizza Ordini");
            printMenuOption(2, "🎫", "Gestione Voucher");
            printMenuOption(3, "📊", "Report e Statistiche");
            printMenuOption(4, "👥", "Gestione Utenti");
            printMenuOption(5, "⚙️", "Impostazioni");

            System.out.println(THIN_SEPARATOR);
            printMenuOption(0, "🚪", "Logout");

            int choice = readChoice();
            handleChoice(choice);
        }
    }

    private void handleChoice(int choice) {
        switch (choice) {
            case 1 -> handleVisualizzaOrdini();
            case 2 -> handleGestioneVoucher();
            case 3 -> handleReport();
            case 4 -> handleGestioneUtenti();
            case 5 -> handleImpostazioni();
            case 0 -> handleLogout();
            default -> showError("Opzione non valida. Riprova.");
        }
    }

    private void handleVisualizzaOrdini() {
        VisualizzaOrdiniCLIController visualizzaController = new VisualizzaOrdiniCLIController(tokenKey);
        visualizzaController.start();
    }

    private void handleGestioneVoucher() {
        printHeader("GESTIONE VOUCHER");
        showInfo(CLI_NOT_IMPLEMENTED_MSG);
        System.out.println("\n  La gestione dei voucher sarà disponibile");
        System.out.println(CLI_FUTURE_VERSION_MSG);
        waitForEnter();
    }

    private void handleReport() {
        printHeader("REPORT E STATISTICHE");
        showInfo(CLI_NOT_IMPLEMENTED_MSG);
        System.out.println("\n  I report e le statistiche saranno disponibili");
        System.out.println(CLI_FUTURE_VERSION_MSG);
        waitForEnter();
    }

    private void handleGestioneUtenti() {
        printHeader("GESTIONE UTENTI");
        showInfo(CLI_NOT_IMPLEMENTED_MSG);
        System.out.println("\n  La gestione degli utenti sarà disponibile");
        System.out.println(CLI_FUTURE_VERSION_MSG);
        waitForEnter();
    }

    private void handleImpostazioni() {
        printHeader("IMPOSTAZIONI");
        showInfo(CLI_NOT_IMPLEMENTED_MSG);
        System.out.println("\n  Le impostazioni di sistema saranno disponibili");
        System.out.println(CLI_FUTURE_VERSION_MSG);
        waitForEnter();
    }

    private void handleLogout() {
        System.out.print("\n  Sei sicuro di voler uscire? (s/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("s") || confirm.equals("si") || confirm.equals("sì")) {
            showSuccess("Logout effettuato. A presto!");
            loggedOut = true;
        }
    }

    public boolean isLoggedOut() {
        return loggedOut;
    }
}
