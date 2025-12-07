package org.example.use_cases.crea_ordine.graphic_controllers;

import org.example.BaseCLIGraphicController;
import org.example. Facades.CreaOrdineFacade;
import org.example. Facades.CreaOrdineFacade.CreaOrdineException;
import org.example.use_cases.crea_ordine.beans.*;
import org.example.use_cases.crea_ordine. beans.RiepilogoOrdineBean. RigaOrdineBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Controller Grafico CLI per lo Use Case "Crea Ordine".
 *
 * Questo è il BOUNDARY nel pattern BCE per l'interfaccia a riga di comando.
 *
 * Responsabilità:
 * - Gestire l'interazione con l'utente tramite CLI
 * - Raccogliere i dati dall'input e incapsularli in Bean
 * - Chiamare il Facade per eseguire le operazioni
 * - Stampare i risultati a console
 *
 * NON contiene logica di business - delega tutto al Facade.
 */
public class CreaOrdineCLIController extends BaseCLIGraphicController {

    private static final String SEPARATORE = "═══════════════════════════════════════════════════════════";
    private static final String SEPARATORE_SOTTILE = "───────────────────────────────────────────────────────────";

    private final CreaOrdineFacade facade;
    private final Scanner scanner;
    private List<FoodBean> prodottiBaseDisponibili;
    private List<FoodBean> addOnDisponibili;
    private boolean eseguiLoop;

    /**
     * Costruttore
     */
    public CreaOrdineCLIController() {
        this.facade = CreaOrdineFacade.getInstance();
        this.scanner = new Scanner(System.in);
        this.eseguiLoop = true;
    }

    /**
     * Avvia il controller CLI
     */
    public void start() {
        stampaIntestazione();

        try {
            // Carica i dati iniziali
            caricaDatiIniziali();

            // Inizia un nuovo ordine
            iniziaNuovoOrdine();

            // Loop principale del menu
            while (eseguiLoop) {
                mostraMenu();
                gestisciSceltaUtente();
            }

        } catch (CreaOrdineException e) {
            stampaErrore("Errore critico: " + e.getMessage());
        }
    }

    // ==================== INIZIALIZZAZIONE ====================

    /**
     * Carica i dati iniziali dal Facade
     */
    private void caricaDatiIniziali() throws CreaOrdineException {
        prodottiBaseDisponibili = facade.getMenuProdottiBase();
        addOnDisponibili = facade.getMenuAddOn();
    }

    /**
     * Inizia un nuovo ordine
     */
    private void iniziaNuovoOrdine() throws CreaOrdineException {
        OrdineBean ordine = facade.iniziaNuovoOrdine();
        System.out.println("\n✓ Nuovo ordine #" + ordine.getNumeroOrdine() + " inizializzato.\n");
    }

    // ==================== MENU E NAVIGAZIONE ====================

    /**
     * Stampa l'intestazione
     */
    private void stampaIntestazione() {
        System.out.println("\n" + SEPARATORE);
        System.out.println("          🥙 KEBAB SHOP - CREA ORDINE 🥙");
        System.out.println(SEPARATORE + "\n");
    }

    /**
     * Mostra il menu principale
     */
    private void mostraMenu() {
        System. out.println(SEPARATORE_SOTTILE);
        System. out.println("                    MENU PRINCIPALE");
        System. out.println(SEPARATORE_SOTTILE);
        System.out.println("  1. 🍔 Aggiungi prodotto all'ordine");
        System. out.println("  2. 🗑️  Rimuovi prodotto dall'ordine");
        System.out.println("  3. 🎫 Applica voucher sconto");
        System.out. println("  4. ❌ Rimuovi voucher");
        System.out.println("  5. 📋 Visualizza riepilogo ordine");
        System.out.println("  6. ✅ Conferma ordine");
        System.out.println("  7. 🚫 Annulla ordine");
        System.out.println("  0. 🚪 Esci");
        System.out.println(SEPARATORE_SOTTILE);
        System.out.print("Scelta: ");
    }

    /**
     * Gestisce la scelta dell'utente
     */
    private void gestisciSceltaUtente() {
        String scelta = scanner.nextLine(). trim();

        try {
            switch (scelta) {
                case "1":
                    aggiungiProdotto();
                    break;
                case "2":
                    rimuoviProdotto();
                    break;
                case "3":
                    applicaVoucher();
                    break;
                case "4":
                    rimuoviVoucher();
                    break;
                case "5":
                    visualizzaRiepilogo();
                    break;
                case "6":
                    confermaOrdine();
                    break;
                case "7":
                    annullaOrdine();
                    break;
                case "0":
                    esci();
                    break;
                default:
                    System.out.println("\n⚠️  Scelta non valida.  Riprova.\n");
            }
        } catch (CreaOrdineException e) {
            stampaErrore(e.getMessage());
        }
    }

    // ==================== OPERAZIONI ====================

    /**
     * Aggiunge un prodotto all'ordine
     */
    private void aggiungiProdotto() throws CreaOrdineException {
        System.out.println("\n" + SEPARATORE_SOTTILE);
        System. out.println("         AGGIUNGI PRODOTTO ALL'ORDINE");
        System. out.println(SEPARATORE_SOTTILE);

        // Mostra prodotti base disponibili
        System.out.println("\n📋 PRODOTTI BASE DISPONIBILI:");
        for (int i = 0; i < prodottiBaseDisponibili.size(); i++) {
            FoodBean fb = prodottiBaseDisponibili.get(i);
            System.out.printf("  %d. %-25s - €%.2f (%d min)\n",
                    i + 1, fb.getDescrizione(), fb.getCosto(), fb.getDurata());
        }

        // Selezione prodotto base
        System.out.print("\nSeleziona prodotto (1-" + prodottiBaseDisponibili.size() + "): ");
        int indiceProdotto = leggiIntero() - 1;

        if (indiceProdotto < 0 || indiceProdotto >= prodottiBaseDisponibili.size()) {
            System. out.println("\n⚠️  Selezione non valida.\n");
            return;
        }

        FoodBean prodottoSelezionato = prodottiBaseDisponibili.get(indiceProdotto);

        // Crea il bean per il prodotto
        FoodBean foodBean = new FoodBean();
        foodBean.setClasse(prodottoSelezionato.getClasse());
        foodBean.setDescrizione(prodottoSelezionato. getDescrizione());
        foodBean.setCosto(prodottoSelezionato.getCosto());
        foodBean.setDurata(prodottoSelezionato.getDurata());

        // Chiedi se vuole aggiungere add-on
        System.out. println("\n🧅 VUOI AGGIUNGERE ADD-ON? (s/n): ");
        String risposta = scanner.nextLine().trim(). toLowerCase();

        if (risposta.equals("s") || risposta.equals("si")) {
            List<String> addOnSelezionati = selezionaAddOn();
            foodBean.setAddOnSelezionati(addOnSelezionati);
        }

        // Aggiungi tramite Facade
        RiepilogoOrdineBean riepilogo = facade.aggiungiProdotto(foodBean);

        System.out.println("\n✓ Prodotto aggiunto con successo!");
        stampaRiepilogoCompatto(riepilogo);
    }

    /**
     * Permette la selezione degli add-on
     */
    private List<String> selezionaAddOn() {
        List<String> selezionati = new ArrayList<>();

        System.out.println("\n📋 ADD-ON DISPONIBILI (inserisci i numeri separati da virgola, 0 per nessuno):");
        for (int i = 0; i < addOnDisponibili.size(); i++) {
            FoodBean addon = addOnDisponibili.get(i);
            System.out.printf("  %d. %-25s - €%. 2f\n",
                    i + 1, addon.getDescrizione(), addon.getCosto());
        }

        System.out.print("\nSeleziona add-on (es: 1,3,4 oppure 0): ");
        String input = scanner.nextLine().trim();

        if (input.equals("0") || input.isEmpty()) {
            return selezionati;
        }

        String[] parti = input.split(",");
        for (String parte : parti) {
            try {
                int indice = Integer.parseInt(parte. trim()) - 1;
                if (indice >= 0 && indice < addOnDisponibili.size()) {
                    selezionati.add(addOnDisponibili.get(indice).getClasse());
                }
            } catch (NumberFormatException e) {
                // Ignora input non validi
            }
        }

        return selezionati;
    }

    /**
     * Rimuove un prodotto dall'ordine
     */
    private void rimuoviProdotto() throws CreaOrdineException {
        RiepilogoOrdineBean riepilogo = facade.getRiepilogo();

        if (riepilogo. getRigheOrdine().isEmpty()) {
            System.out.println("\n⚠️  L'ordine è vuoto.  Nessun prodotto da rimuovere.\n");
            return;
        }

        System.out.println("\n" + SEPARATORE_SOTTILE);
        System.out.println("         RIMUOVI PRODOTTO DALL'ORDINE");
        System.out.println(SEPARATORE_SOTTILE);

        // Mostra prodotti nell'ordine
        List<RigaOrdineBean> righe = riepilogo.getRigheOrdine();
        System.out.println("\n📋 PRODOTTI NELL'ORDINE:");
        for (int i = 0; i < righe.size(); i++) {
            RigaOrdineBean riga = righe.get(i);
            System.out. printf("  %d. %-35s - %s\n",
                    i + 1, riga.getDescrizione(), riga.getPrezzoFormattato());
        }

        System.out. print("\nSeleziona prodotto da rimuovere (1-" + righe.size() + ", 0 per annullare): ");
        int indice = leggiIntero() - 1;

        if (indice == -1) {
            System. out.println("\nOperazione annullata.\n");
            return;
        }

        if (indice < 0 || indice >= righe.size()) {
            System. out.println("\n⚠️  Selezione non valida.\n");
            return;
        }

        riepilogo = facade.rimuoviProdotto(indice);
        System.out.println("\n✓ Prodotto rimosso con successo!");
        stampaRiepilogoCompatto(riepilogo);
    }

    /**
     * Applica un voucher all'ordine
     */
    private void applicaVoucher() throws CreaOrdineException {
        if (facade.getRiepilogo().isVoucherApplicato()) {
            System.out.println("\n⚠️  C'è già un voucher applicato.  Rimuovilo prima di applicarne un altro.\n");
            return;
        }

        if (facade.getRiepilogo(). getRigheOrdine().isEmpty()) {
            System.out.println("\n⚠️  Aggiungi almeno un prodotto prima di applicare un voucher.\n");
            return;
        }

        System.out. println("\n" + SEPARATORE_SOTTILE);
        System.out.println("              APPLICA VOUCHER SCONTO");
        System.out. println(SEPARATORE_SOTTILE);

        System.out.print("\nInserisci codice voucher: ");
        String codiceVoucher = scanner.nextLine().trim();

        if (codiceVoucher.isEmpty()) {
            System.out.println("\nOperazione annullata.\n");
            return;
        }

        RiepilogoOrdineBean riepilogo = facade.applicaVoucher(codiceVoucher);

        System.out.println("\n✓ Voucher " + codiceVoucher. toUpperCase() + " applicato con successo!");
        System.out.println("  Sconto: " + riepilogo.getScontoFormattato());
        stampaRiepilogoCompatto(riepilogo);
    }

    /**
     * Rimuove il voucher dall'ordine
     */
    private void rimuoviVoucher() throws CreaOrdineException {
        if (!facade.getRiepilogo(). isVoucherApplicato()) {
            System.out.println("\n⚠️  Nessun voucher applicato.\n");
            return;
        }

        System.out.print("\nConfermi la rimozione del voucher? (s/n): ");
        String risposta = scanner.nextLine(). trim().toLowerCase();

        if (! risposta.equals("s") && !risposta.equals("si")) {
            System.out.println("\nOperazione annullata.\n");
            return;
        }

        RiepilogoOrdineBean riepilogo = facade.rimuoviVoucher();
        System.out.println("\n✓ Voucher rimosso con successo!");
        stampaRiepilogoCompatto(riepilogo);
    }

    /**
     * Visualizza il riepilogo completo dell'ordine
     */
    private void visualizzaRiepilogo() throws CreaOrdineException {
        RiepilogoOrdineBean riepilogo = facade.getRiepilogo();
        stampaRiepilogoCompleto(riepilogo);
    }

    /**
     * Conferma l'ordine
     */
    private void confermaOrdine() throws CreaOrdineException {
        RiepilogoOrdineBean riepilogo = facade. getRiepilogo();

        if (riepilogo. getRigheOrdine().isEmpty()) {
            System.out.println("\n⚠️  L'ordine è vuoto.  Aggiungi almeno un prodotto.\n");
            return;
        }

        // Mostra riepilogo
        stampaRiepilogoCompleto(riepilogo);

        // Chiedi conferma
        System.out.print("\nConfermi l'ordine e procedi al pagamento? (s/n): ");
        String risposta = scanner.nextLine().trim().toLowerCase();

        if (!risposta.equals("s") && !risposta.equals("si")) {
            System.out.println("\nOrdine non confermato.\n");
            return;
        }

        boolean success = facade.confermaOrdine();

        if (success) {
            System.out.println("\n" + SEPARATORE);
            System.out.println("     ✅ ORDINE #" + riepilogo.getNumeroOrdine() + " CONFERMATO!  ✅");
            System.out.println(SEPARATORE);
            System.out.println("\nTotale da pagare: " + riepilogo.getTotaleFormattato());
            System.out.println("Tempo di preparazione stimato: " + riepilogo.getDurataFormattata());
            System.out.println("\nGrazie per il tuo ordine!  🥙\n");

            eseguiLoop = false;
        } else {
            stampaErrore("Si è verificato un errore durante la conferma dell'ordine.");
        }
    }

    /**
     * Annulla l'ordine
     */
    private void annullaOrdine() throws CreaOrdineException {
        if (facade.getRiepilogo(). getRigheOrdine().isEmpty()) {
            System.out.println("\n⚠️  L'ordine è già vuoto.\n");
            return;
        }

        System.out.print("\nSei sicuro di voler annullare l'ordine?  (s/n): ");
        String risposta = scanner.nextLine().trim().toLowerCase();

        if (!risposta.equals("s") && !risposta.equals("si")) {
            System. out.println("\nOperazione annullata.\n");
            return;
        }

        facade.annullaOrdine();
        iniziaNuovoOrdine();
        System.out.println("✓ Ordine annullato.  Un nuovo ordine è stato inizializzato.\n");
    }

    /**
     * Esce dall'applicazione
     */
    private void esci() {
        System.out.print("\nSei sicuro di voler uscire?  (s/n): ");
        String risposta = scanner.nextLine().trim().toLowerCase();

        if (risposta.equals("s") || risposta.equals("si")) {
            if (facade.isOrdineInCorso()) {
                facade.annullaOrdine();
            }
            System.out.println("\nArrivederci! 👋\n");
            eseguiLoop = false;
        }
    }

    // ==================== METODI DI STAMPA ====================

    /**
     * Stampa un riepilogo compatto dell'ordine
     */
    private void stampaRiepilogoCompatto(RiepilogoOrdineBean riepilogo) {
        System.out.println("\n📋 Riepilogo: " + riepilogo.getRigheOrdine().size() + " prodotti | " +
                "Totale: " + riepilogo.getTotaleFormattato() + " | " +
                "Tempo: " + riepilogo.getDurataFormattata() + "\n");
    }

    /**
     * Stampa il riepilogo completo dell'ordine
     */
    private void stampaRiepilogoCompleto(RiepilogoOrdineBean riepilogo) {
        System.out. println("\n" + SEPARATORE);
        System.out.println("           📋 RIEPILOGO ORDINE #" + riepilogo.getNumeroOrdine());
        System.out.println(SEPARATORE);

        if (riepilogo.getRigheOrdine().isEmpty()) {
            System.out.println("\n  L'ordine è vuoto.\n");
            System.out.println(SEPARATORE + "\n");
            return;
        }

        // Stampa prodotti
        System.out.println("\nPRODOTTI:");
        for (RigaOrdineBean riga : riepilogo.getRigheOrdine()) {
            System.out.printf("  • %-40s %s\n",
                    riga.getDescrizione(), riga.getPrezzoFormattato());
        }

        System.out.println("\n" + SEPARATORE_SOTTILE);

        // Stampa totali
        System.out.printf("  %-42s %s\n", "Subtotale:", riepilogo. getSubtotaleFormattato());

        if (riepilogo.isVoucherApplicato()) {
            System.out.printf("  %-42s %s\n",
                    "Sconto (" + riepilogo.getCodiceVoucher() + "):",
                    riepilogo. getScontoFormattato());
        }

        System. out.println(SEPARATORE_SOTTILE);
        System.out.printf("  %-42s %s\n", "TOTALE:", riepilogo. getTotaleFormattato());
        System.out.printf("  %-42s %s\n", "Tempo di preparazione:", riepilogo. getDurataFormattata());

        System.out.println(SEPARATORE + "\n");
    }

    /**
     * Stampa un messaggio di errore
     */
    private void stampaErrore(String messaggio) {
        System.out.println("\n❌ ERRORE: " + messaggio + "\n");
    }

    // ==================== UTILITY ====================

    /**
     * Legge un intero dall'input
     */
    private int leggiIntero() {
        try {
            return Integer.parseInt(scanner.nextLine(). trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}