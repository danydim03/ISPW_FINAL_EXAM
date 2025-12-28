package org.example.use_cases.visualizza_ordini.graphic_controllers;

import org.example.BaseCLIGraphicController;
import org.example.use_cases.crea_ordine.beans.OrdineBean;
import org.example.use_cases.visualizza_ordini.VisualizzaOrdiniFacade;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * CLI Controller for "Visualizza Ordini" use case (Amministratore/Kebabbaro).
 * Displays pending orders and allows status updates.
 */
public class VisualizzaOrdiniCLIController extends BaseCLIGraphicController {

    private final VisualizzaOrdiniFacade facade;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public VisualizzaOrdiniCLIController(String tokenKey) {
        super(tokenKey);
        this.facade = new VisualizzaOrdiniFacade();
    }

    @Override
    public void start() {
        boolean continueViewing = true;

        while (continueViewing) {
            printHeader("GESTIONE ORDINI");

            try {
                List<OrdineBean> ordini = facade.getOrdiniInCreazione();

                if (ordini == null || ordini.isEmpty()) {
                    showInfo("Non ci sono ordini in attesa al momento.");
                    System.out.println("\n  Tutti gli ordini sono stati processati! 🎉");
                    waitForEnter();
                    continueViewing = false;
                } else {
                    displayOrdersAndMenu(ordini);
                    continueViewing = handleUserAction(ordini);
                }

            } catch (Exception e) {
                showError("Impossibile recuperare gli ordini: " + e.getMessage());
                waitForEnter();
                continueViewing = false;
            }
        }
    }

    private void displayOrdersAndMenu(List<OrdineBean> ordini) {
        System.out.println("  Ordini in attesa di elaborazione: " + ordini.size() + "\n");

        // Table header
        System.out.println("  ┌────┬──────────┬─────────────────────┬──────────────┬──────────┐");
        System.out.println("  │ #  │  ORDINE  │        DATA         │   CLIENTE    │  TOTALE  │");
        System.out.println("  ├────┼──────────┼─────────────────────┼──────────────┼──────────┤");

        // Table rows
        int index = 1;
        for (OrdineBean ordine : ordini) {
            String numero = String.format("#%d", ordine.getNumeroOrdine());
            String data = ordine.getDataCreazione() != null ? ordine.getDataCreazione().format(DATE_FORMATTER) : "-";
            String cliente = truncate(ordine.getClienteId(), 12);
            String totale = formatPrice(ordine.getTotale());

            System.out.printf("  │ %-2d │ %-8s │ %-19s │ %-12s │ %8s │%n",
                    index++, numero, data, cliente, totale);
        }

        // Table footer
        System.out.println("  └────┴──────────┴─────────────────────┴──────────────┴──────────┘");

        // Menu
        System.out.println("\n  Azioni disponibili:");
        System.out.println(THIN_SEPARATOR);
        printMenuOption(1, "🚚", "Imposta ordine 'In Consegna'");
        printMenuOption(2, "🔄", "Aggiorna lista ordini");
        System.out.println(THIN_SEPARATOR);
        printMenuOption(0, "⬅️", "Torna alla homepage");
    }

    private boolean handleUserAction(List<OrdineBean> ordini) {
        int choice = readChoice();

        switch (choice) {
            case 1 -> {
                handleSetInConsegna(ordini);
                return true;
            }
            case 2 -> {
                showInfo("Lista aggiornata.");
                return true;
            }
            case 0 -> {
                return false;
            }
            default -> {
                showError("Opzione non valida.");
                return true;
            }
        }
    }

    private void handleSetInConsegna(List<OrdineBean> ordini) {
        System.out.print("\n  Inserisci il numero dell'ordine da impostare 'In Consegna' (1-" + ordini.size() + "): ");

        try {
            int index = Integer.parseInt(scanner.nextLine().trim());

            if (index < 1 || index > ordini.size()) {
                showError("Numero ordine non valido.");
                waitForEnter();
                return;
            }

            OrdineBean selectedOrder = ordini.get(index - 1);

            System.out.println("\n  Ordine selezionato: #" + selectedOrder.getNumeroOrdine());
            System.out.print("  Confermi di impostare come 'In Consegna'? (s/n): ");

            String confirm = scanner.nextLine().trim().toLowerCase();

            if (confirm.equals("s") || confirm.equals("si") || confirm.equals("sì")) {
                try {
                    facade.impostaInConsegna(selectedOrder);
                    showSuccess("Ordine #" + selectedOrder.getNumeroOrdine() + " impostato 'In Consegna'!");
                } catch (Exception e) {
                    showError("Impossibile aggiornare lo stato: " + e.getMessage());
                }
            } else {
                showInfo("Operazione annullata.");
            }

        } catch (NumberFormatException e) {
            showError("Input non valido.");
        }

        waitForEnter();
    }
}
