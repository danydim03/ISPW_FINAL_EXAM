package org.example.Facades;

import org.example.exceptions.DAOException;
import org.example.use_cases.crea_ordine.CreaOrdineController;
import org.example.use_cases.crea_ordine.beans.FoodBean;
import org.example.use_cases.crea_ordine.beans.OrdineBean;
import org.example.use_cases.crea_ordine.beans.RiepilogoOrdineBean;
import org.example.use_cases.crea_ordine.mock.CreaOrdineMockDataProvider;

import java.util.List;

/**
 * Facade per lo use-case "Crea Ordine".
 * - Espone un'interfaccia semplice alla GUI (Boundary).
 * - Gestisce fallback/mock senza coinvolgere i controller grafici.
 */
public class CreaOrdineFacade {

    public static class CreaOrdineException extends Exception {
        public CreaOrdineException(String message) { super(message); }
        public CreaOrdineException(String message, Throwable cause) { super(message, cause); }
    }

    private static final boolean USE_FAKE_DATA_WHEN_DB_UNAVAILABLE = true;

    private static CreaOrdineFacade instance;
    private final CreaOrdineController controller;
    private final CreaOrdineMockDataProvider mockProvider;

    private CreaOrdineFacade() {
        this.controller = new CreaOrdineController();
        this.mockProvider = new CreaOrdineMockDataProvider();
    }

    public static synchronized CreaOrdineFacade getInstance() {
        if (instance == null) {
            instance = new CreaOrdineFacade();
        }
        return instance;
    }

    public List<FoodBean> getMenuProdottiBase() throws CreaOrdineException {
        if (USE_FAKE_DATA_WHEN_DB_UNAVAILABLE) {
            return mockProvider.getProdottiBase();
        }
        try {
            return controller.getProdottiBaseDisponibili();
        } catch (Exception e) {
            throw new CreaOrdineException("Errore nel recupero dei prodotti base", e);
        }
    }

    public List<FoodBean> getMenuAddOn() throws CreaOrdineException {
        if (USE_FAKE_DATA_WHEN_DB_UNAVAILABLE) {
            return mockProvider.getAddOn();
        }
        try {
            return controller.getAddOnDisponibili();
        } catch (Exception e) {
            throw new CreaOrdineException("Errore nel recupero degli add-on", e);
        }
    }

    public OrdineBean iniziaNuovoOrdine() throws CreaOrdineException {
        try {
            return controller.inizializzaNuovoOrdine("CLIENTE_DEMO");
        } catch (Exception e) {
            throw new CreaOrdineException("Impossibile inizializzare l'ordine", e);
        }
    }

    public RiepilogoOrdineBean aggiungiProdotto(FoodBean foodBean) throws CreaOrdineException {
        try {
            controller.aggiungiProdottoAOrdine(foodBean);
            return controller.getRiepilogoOrdine();
        } catch (Exception e) {
            throw new CreaOrdineException("Errore durante l'aggiunta del prodotto", e);
        }
    }

    public RiepilogoOrdineBean rimuoviProdotto(int index) throws CreaOrdineException {
        try {
            controller.rimuoviProdottoDaOrdine(index);
            return controller.getRiepilogoOrdine();
        } catch (Exception e) {
            throw new CreaOrdineException("Errore durante la rimozione del prodotto", e);
        }
    }

    public RiepilogoOrdineBean applicaVoucher(String codice) throws CreaOrdineException {
        try {
            controller.applicaVoucher(codice);
            return controller.getRiepilogoOrdine();
        } catch (Exception e) {
            throw new CreaOrdineException("Errore durante l'applicazione del voucher", e);
        }
    }

    public RiepilogoOrdineBean rimuoviVoucher() throws CreaOrdineException {
        try {
            controller.rimuoviVoucher();
            return controller.getRiepilogoOrdine();
        } catch (Exception e) {
            throw new CreaOrdineException("Errore durante la rimozione del voucher", e);
        }
    }

    public RiepilogoOrdineBean getRiepilogo() throws CreaOrdineException {
        try {
            return controller.getRiepilogoOrdine();
        } catch (Exception e) {
            throw new CreaOrdineException("Errore nel calcolo del riepilogo", e);
        }
    }

    public boolean confermaOrdine() throws CreaOrdineException {
        try {
            return controller.confermaOrdine();
        } catch (DAOException e) {
            throw new CreaOrdineException("Errore nel salvataggio dell'ordine", e);
        } catch (Exception e) {
            throw new CreaOrdineException("Errore durante la conferma dell'ordine", e);
        }
    }

    public void annullaOrdine() {
        controller.annullaOrdine();
    }
}