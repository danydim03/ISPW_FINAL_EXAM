package org.example.graphic_controllers_general.homepages;

import org.example.BaseCLIGraphicController;
import org.example.use_cases.crea_ordine.graphic_controllers.CreaOrdineCLIController;
import org.example.use_cases.storico_ordini.graphic_controllers.StoricoOrdiniCLIController;

/**
 * CLI Graphic Controller for Cliente Homepage.
 * Displays menu options specific to cliente role.
 */
public class ClienteHomepageCLIController extends BaseCLIGraphicController {

    private static final String CLI_NOT_IMPLEMENTED_MSG = "View CLI implementata in seguito";
    private static final String CLI_FUTURE_VERSION_MSG = "  in una futura versione dell'interfaccia CLI.";
    private boolean loggedOut = false;

    public ClienteHomepageCLIController(String tokenKey) {
        super(tokenKey);
    }

    @Override
    public void start() {
        while (!loggedOut) {
            printHeader("HOMEPAGE CLIENTE");

            System.out.println("  Cosa vuoi fare oggi?\n");
            System.out.println(THIN_SEPARATOR);

            printMenuOption(1, "🍔", "Nuovo Ordine");
            printMenuOption(2, "📜", "Storico Ordini");
            printMenuOption(3, "🗺️", "Visualizza Mappa");
            printMenuOption(4, "👤", "Profilo");
            printMenuOption(5, "🌐", "Sito Web");

            System.out.println(THIN_SEPARATOR);
            printMenuOption(0, "🚪", "Logout");

            int choice = readChoice();
            handleChoice(choice);
        }
    }

    private void handleChoice(int choice) {
        switch (choice) {
            case 1 -> handleNuovoOrdine();
            case 2 -> handleStoricoOrdini();
            case 3 -> handleVisualizzaMappa();
            case 4 -> handleProfilo();
            case 5 -> handleWebsite();
            case 0 -> handleLogout();
            default -> showError("Opzione non valida. Riprova.");
        }
    }

    private void handleNuovoOrdine() {
        CreaOrdineCLIController creaOrdineController = new CreaOrdineCLIController(tokenKey);
        creaOrdineController.start();
    }

    private void handleStoricoOrdini() {
        StoricoOrdiniCLIController storicoController = new StoricoOrdiniCLIController(tokenKey);
        storicoController.start();
    }

    private void handleVisualizzaMappa() {
        printHeader("VISUALIZZA MAPPA");
        showInfo("Apertura della mappa nel browser...");

        try {
            // Use the same controller as JavaFX GUI
            ClienteHomepageController.getInstance().apriMappa();

            showSuccess("Mappa aperta nel browser!");
            System.out.println("\n  🗺️  La mappa delle nostre sedi è stata aperta nel tuo browser.");
        } catch (Exception e) {
            showError("Impossibile aprire la mappa: " + e.getMessage());
            System.out.println("\n  Puoi visitare manualmente:");
            System.out.println(
                    "  https://www.google.com/maps/d/u/0/embed?mid=1rOu3jHBshR_ZiVIPd46f6ncQaHpjRvQ&ehbc=2E312F");
        }

        waitForEnter();
    }

    private void handleProfilo() {
        printHeader("PROFILO UTENTE");
        showInfo(CLI_NOT_IMPLEMENTED_MSG);
        System.out.println("\n  La gestione del profilo utente sarà disponibile");
        System.out.println(CLI_FUTURE_VERSION_MSG);
        waitForEnter();
    }

    private void handleWebsite() {
        printHeader("SITO WEB");
        System.out.println("  🌐 Visita il nostro sito web:");
        System.out.println("     https://www.yahabibi.it");
        System.out.println("\n  (Copia e incolla l'URL nel tuo browser)");
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
