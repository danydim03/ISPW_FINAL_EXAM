package org.example.use_cases.crea_ordine.graphic_controllers;

import org.example.BaseCLIGraphicController;
import org.example.exceptions.HabibiException;
import org.example.use_cases.crea_ordine.CreaOrdineFacade;
import org.example.use_cases.crea_ordine.beans.FoodBean;
import org.example.use_cases.crea_ordine.beans.OrdineBean;
import org.example.use_cases.crea_ordine.beans.RiepilogoOrdineBean;
import org.example.use_cases.crea_ordine.beans.VoucherBean;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CLI Controller for "Crea Ordine" use case.
 * Uses CreaOrdineFacade to maintain separation between UI and business logic.
 */
public class CreaOrdineCLIController extends BaseCLIGraphicController {

    private static final Logger logger = Logger.getLogger(CreaOrdineCLIController.class.getName());

    private CreaOrdineFacade facade;
    private List<FoodBean> prodottiBase;
    private List<FoodBean> addOns;
    private boolean orderCompleted = false;
    private boolean exitRequested = false;

    public CreaOrdineCLIController(String tokenKey) {
        super(tokenKey);
    }

    @Override
    public void start() {
        try {
            // Initialize facade
            facade = new CreaOrdineFacade(tokenKey);

            // Initialize a new order
            OrdineBean ordineBean = facade.inizializzaNuovoOrdine(tokenKey);
            showSuccess("Nuovo ordine #" + ordineBean.getNumeroOrdine() + " inizializzato!");

            // Load available products
            loadProducts();

            // Main order loop
            while (!orderCompleted && !exitRequested) {
                showOrderMenu();
            }

        } catch (HabibiException e) {
            logger.log(Level.SEVERE, "Impossibile avviare la creazione ordine", e);
            showError("Impossibile avviare la creazione ordine: " + e.getMessage());
            waitForEnter();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Errore imprevisto nell'avvio creazione ordine", e);
            showError("Errore imprevisto: " + e.getMessage());
            waitForEnter();
        }
    }

    private void loadProducts() {
        try {
            prodottiBase = facade.getProdottiBaseDisponibili();
            addOns = facade.getAddOnDisponibili();
        } catch (HabibiException e) {
            logger.log(Level.SEVERE, "Errore nel caricamento prodotti", e);
            showError("Errore nel caricamento prodotti: " + e.getMessage());
            prodottiBase = new ArrayList<>();
            addOns = new ArrayList<>();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Errore imprevisto nel caricamento prodotti", e);
            showError("Errore imprevisto: " + e.getMessage());
            prodottiBase = new ArrayList<>();
            addOns = new ArrayList<>();
        }
    }

    private void showOrderMenu() {
        printHeader("CREA ORDINE");

        // Show current order summary
        showCurrentOrderSummary();

        System.out.println("\n  Cosa vuoi fare?\n");
        System.out.println(THIN_SEPARATOR);

        printMenuOption(1, "🍔", "Aggiungi Prodotto");
        printMenuOption(2, "🎫", "Applica Voucher");
        printMenuOption(3, "📋", "Visualizza Riepilogo");
        printMenuOption(4, "✅", "Conferma Ordine");

        System.out.println(THIN_SEPARATOR);
        printMenuOption(0, "❌", "Annulla Ordine");

        int choice = readChoice();
        handleMenuChoice(choice);
    }

    private void showCurrentOrderSummary() {
        try {
            RiepilogoOrdineBean riepilogo = facade.getRiepilogoOrdine();
            if (riepilogo != null && !riepilogo.getRigheOrdine().isEmpty()) {
                System.out.println("  📦 Prodotti nel carrello: " + riepilogo.getRigheOrdine().size());
                System.out.println("  💰 Totale corrente: " + riepilogo.getTotaleFormattato());
                if (riepilogo.isVoucherApplicato()) {
                    System.out.println("  🎫 Voucher: " + riepilogo.getCodiceVoucher() +
                            " (" + riepilogo.getScontoFormattato() + ")");
                }
            } else {
                System.out.println("  📦 Il carrello è vuoto");
            }
        } catch (Exception e) {
            // getRiepilogoOrdine non lancia eccezioni checked, catch per RuntimeException
            System.out.println("  📦 Carrello in attesa...");
        }
    }

    private void handleMenuChoice(int choice) {
        switch (choice) {
            case 1 -> handleAggiungiProdotto();
            case 2 -> handleApplicaVoucher();
            case 3 -> handleVisualizzaRiepilogo();
            case 4 -> handleConfermaOrdine();
            case 0 -> handleAnnullaOrdine();
            default -> showError("Opzione non valida. Riprova.");
        }
    }

    private void handleAggiungiProdotto() {
        printHeader("AGGIUNGI PRODOTTO");

        if (prodottiBase == null || prodottiBase.isEmpty()) {
            showError("Nessun prodotto disponibile");
            waitForEnter();
            return;
        }

        // Show available base products
        System.out.println("  Seleziona un prodotto base:\n");
        System.out.println(THIN_SEPARATOR);

        for (int i = 0; i < prodottiBase.size(); i++) {
            FoodBean food = prodottiBase.get(i);
            System.out.printf("  [%d] %-25s %s  (%d min)%n",
                    i + 1,
                    food.getDescrizione(),
                    formatPrice(food.getCosto()),
                    food.getDurata());
        }

        System.out.println(THIN_SEPARATOR);
        printMenuOption(0, "Indietro");

        int productChoice = readChoice();

        if (productChoice == 0 || productChoice < 0 || productChoice > prodottiBase.size()) {
            return;
        }

        FoodBean selectedProduct = prodottiBase.get(productChoice - 1);

        // Ask for add-ons
        List<String> selectedAddOns = selectAddOns();
        selectedProduct.setAddOnSelezionati(selectedAddOns);

        // Add to order
        try {
            boolean success = facade.aggiungiProdottoAOrdine(selectedProduct);
            if (success) {
                showSuccess("Prodotto aggiunto all'ordine!");
            } else {
                showError("Impossibile aggiungere il prodotto");
            }
        } catch (HabibiException e) {
            logger.log(Level.SEVERE, "Errore nell'aggiunta prodotto", e);
            showError("Errore: " + e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Errore imprevisto nell'aggiunta prodotto", e);
            showError("Errore imprevisto: " + e.getMessage());
        }

        waitForEnter();
    }

    private List<String> selectAddOns() {
        List<String> selected = new ArrayList<>();

        if (addOns == null || addOns.isEmpty()) {
            return selected;
        }

        System.out
                .println("\n  Vuoi aggiungere degli extra? (inserisci i numeri separati da virgola, 0 per nessuno)\n");
        System.out.println(THIN_SEPARATOR);

        for (int i = 0; i < addOns.size(); i++) {
            FoodBean addon = addOns.get(i);
            System.out.printf("  [%d] %-20s +%s%n",
                    i + 1,
                    addon.getDescrizione(),
                    formatPrice(addon.getCosto()));
        }

        System.out.println(THIN_SEPARATOR);
        System.out.print("\n  👉 Inserisci selezione (es: 1,3 o 0 per nessuno): ");

        String input = scanner.nextLine().trim();

        if (input.equals("0") || input.isEmpty()) {
            return selected;
        }

        try {
            String[] choices = input.split(",");
            for (String choice : choices) {
                int idx = Integer.parseInt(choice.trim()) - 1;
                if (idx >= 0 && idx < addOns.size()) {
                    selected.add(addOns.get(idx).getClasse());
                }
            }
        } catch (NumberFormatException e) {
            showWarning("Formato non valido, nessun extra selezionato");
        }

        return selected;
    }

    private void handleApplicaVoucher() {
        printHeader("APPLICA VOUCHER");

        String codice = readInput("🎫 Inserisci codice voucher (o 'annulla' per tornare indietro)");

        if (codice.equalsIgnoreCase("annulla") || codice.isEmpty()) {
            return;
        }

        try {
            VoucherBean voucher = facade.applicaVoucher(codice);
            if (voucher != null && voucher.isValido()) {
                showSuccess("Voucher applicato! Sconto: " + voucher.getTipoVoucher() +
                        " - " + voucher.getValore() + (voucher.getTipoVoucher().equals("PERCENTUALE") ? "%" : "€"));
            } else {
                showWarning("Voucher non valido o già utilizzato");
            }
        } catch (HabibiException e) {
            logger.log(Level.SEVERE, "Errore nell'applicazione del voucher", e);
            showError("Errore nell'applicazione del voucher: " + e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Errore imprevisto nell'applicazione del voucher", e);
            showError("Errore imprevisto: " + e.getMessage());
        }

        waitForEnter();
    }

    private void handleVisualizzaRiepilogo() {
        printHeader("RIEPILOGO ORDINE");

        try {
            RiepilogoOrdineBean riepilogo = facade.getRiepilogoOrdine();

            if (riepilogo == null || riepilogo.getRigheOrdine().isEmpty()) {
                showWarning("L'ordine è vuoto. Aggiungi qualche prodotto!");
                waitForEnter();
                return;
            }

            System.out.println("  Ordine #" + riepilogo.getNumeroOrdine() + "\n");
            System.out.println("  ┌─────────────────────────────────────────────────┐");
            System.out.println("  │ PRODOTTO                          PREZZO       │");
            System.out.println("  ├─────────────────────────────────────────────────┤");

            for (RiepilogoOrdineBean.RigaOrdineBean riga : riepilogo.getRigheOrdine()) {
                System.out.printf("  │ %-35s %8s │%n",
                        truncate(riga.getDescrizione(), 35),
                        riga.getPrezzoFormattato());
            }

            System.out.println("  ├─────────────────────────────────────────────────┤");
            System.out.printf("  │ Subtotale                         %8s │%n", riepilogo.getSubtotaleFormattato());

            if (riepilogo.isVoucherApplicato()) {
                System.out.printf("  │ Sconto (%s)                  %8s │%n",
                        riepilogo.getCodiceVoucher(),
                        riepilogo.getScontoFormattato());
            }

            System.out.println("  ├─────────────────────────────────────────────────┤");
            System.out.printf("  │ TOTALE                            %8s │%n", riepilogo.getTotaleFormattato());
            System.out.println("  └─────────────────────────────────────────────────┘");
            System.out.println("\n  ⏱️  Tempo di preparazione stimato: " + riepilogo.getDurataFormattata());

        } catch (Exception e) {
            // getRiepilogoOrdine non lancia eccezioni checked
            logger.log(Level.SEVERE, "Errore imprevisto nel recupero del riepilogo", e);
            showError("Errore imprevisto: " + e.getMessage());
        }

        waitForEnter();
    }

    private void handleConfermaOrdine() {
        printHeader("CONFERMA ORDINE");

        try {
            RiepilogoOrdineBean riepilogo = facade.getRiepilogoOrdine();

            if (riepilogo == null || riepilogo.getRigheOrdine().isEmpty()) {
                showWarning("L'ordine è vuoto. Aggiungi qualche prodotto prima di confermare!");
                waitForEnter();
                return;
            }

            System.out.println("  Stai per confermare l'ordine:");
            System.out.println("  💰 Totale: " + riepilogo.getTotaleFormattato());
            System.out.println("  ⏱️  Tempo stimato: " + riepilogo.getDurataFormattata());

            System.out.print("\n  Confermi l'ordine? (s/n): ");
            String confirm = scanner.nextLine().trim().toLowerCase();

            if (confirm.equals("s") || confirm.equals("si") || confirm.equals("sì")) {
                boolean success = facade.confermaOrdine();
                if (success) {
                    showSuccess("🎉 Ordine #" + riepilogo.getNumeroOrdine() + " confermato con successo!");
                    System.out.println("\n  Il tuo ordine è stato inviato alla cucina.");
                    System.out.println("  Riceverai una notifica quando sarà pronto.");
                    orderCompleted = true;
                } else {
                    showError("Impossibile confermare l'ordine");
                }
            } else {
                showInfo("Ordine non confermato. Puoi continuare a modificarlo.");
            }

        } catch (HabibiException e) {
            logger.log(Level.SEVERE, "Errore nella conferma ordine", e);
            showError("Errore nella conferma: " + e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Errore imprevisto nella conferma ordine", e);
            showError("Errore imprevisto: " + e.getMessage());
        }

        waitForEnter();
    }

    private void handleAnnullaOrdine() {
        System.out.print("\n  Sei sicuro di voler annullare l'ordine? (s/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("s") || confirm.equals("si") || confirm.equals("sì")) {
            try {
                facade.annullaOrdine();
                showInfo("Ordine annullato.");
                exitRequested = true;
            } catch (Exception e) {
                // annullaOrdine non lancia eccezioni checked
                logger.log(Level.SEVERE, "Errore imprevisto nell'annullamento ordine", e);
                showError("Errore imprevisto: " + e.getMessage());
            }
        }
    }

    public boolean isOrderCompleted() {
        return orderCompleted;
    }
}
