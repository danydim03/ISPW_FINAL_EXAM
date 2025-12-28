package org.example.use_cases.storico_ordini.graphic_controllers;

import org.example.BaseCLIGraphicController;
import org.example.use_cases.crea_ordine.beans.OrdineBean;
import org.example.use_cases.storico_ordini.StoricoOrdiniFacade;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * CLI Controller for "Storico Ordini" use case (Cliente).
 * Displays the order history for the logged-in cliente.
 */
public class StoricoOrdiniCLIController extends BaseCLIGraphicController {

    private final StoricoOrdiniFacade facade;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public StoricoOrdiniCLIController(String tokenKey) {
        super(tokenKey);
        this.facade = new StoricoOrdiniFacade();
    }

    @Override
    public void start() {
        printHeader("STORICO ORDINI");

        try {
            List<OrdineBean> ordini = facade.getStoricoOrdini();

            if (ordini == null || ordini.isEmpty()) {
                showInfo("Non hai ancora effettuato nessun ordine.");
                System.out.println("\n  Vai alla sezione 'Nuovo Ordine' per creare il tuo primo ordine!");
            } else {
                displayOrderTable(ordini);
            }

        } catch (Exception e) {
            showError("Impossibile recuperare lo storico ordini: " + e.getMessage());
        }

        waitForEnter();
    }

    private void displayOrderTable(List<OrdineBean> ordini) {
        System.out.println("  Trovati " + ordini.size() + " ordini:\n");

        // Table header
        System.out.println("  ┌──────────┬─────────────────────┬────────────────┬──────────┐");
        System.out.println("  │  ORDINE  │        DATA         │     STATO      │  TOTALE  │");
        System.out.println("  ├──────────┼─────────────────────┼────────────────┼──────────┤");

        // Table rows
        for (OrdineBean ordine : ordini) {
            String numero = String.format("#%d", ordine.getNumeroOrdine());
            String data = ordine.getDataCreazione() != null ? ordine.getDataCreazione().format(DATE_FORMATTER) : "-";
            String stato = formatStato(ordine.getStato());
            String totale = formatPrice(ordine.getTotale());

            System.out.printf("  │ %-8s │ %-19s │ %-14s │ %8s │%n",
                    numero, data, stato, totale);
        }

        // Table footer
        System.out.println("  └──────────┴─────────────────────┴────────────────┴──────────┘");

        // Summary
        double totaleComplessivo = ordini.stream()
                .mapToDouble(OrdineBean::getTotale)
                .sum();

        System.out.println("\n  📊 Statistiche:");
        System.out.println("     Ordini totali: " + ordini.size());
        System.out.println("     Spesa complessiva: " + formatPrice(totaleComplessivo));
    }

    /**
     * Formats the order status with emoji
     */
    private String formatStato(String stato) {
        if (stato == null)
            return "❓ Sconosciuto";

        return switch (stato.toUpperCase()) {
            case "IN_CREAZIONE" -> "📝 In creazione";
            case "CONFERMATO" -> "✅ Confermato";
            case "IN_PREPARAZIONE" -> "👨‍🍳 Preparazione";
            case "PRONTO" -> "🔔 Pronto";
            case "IN_CONSEGNA" -> "🚚 In consegna";
            case "CONSEGNATO" -> "📦 Consegnato";
            case "ANNULLATO" -> "❌ Annullato";
            default -> stato;
        };
    }
}
