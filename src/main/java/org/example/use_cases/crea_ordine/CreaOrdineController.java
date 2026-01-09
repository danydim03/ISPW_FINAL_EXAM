package org.example.use_cases.crea_ordine;

import org.example.exceptions.*;
import org.example.model.food.*;
import org.example.model.food.decorator.*;
import org.example.model.ordine.Ordine;
import org.example.model.ordine.OrdineLazyFactory;
import org.example.model.voucher.Voucher;
import org.example.use_cases.crea_ordine.beans.*;
import org.example.use_cases.crea_ordine.beans.RiepilogoOrdineBean.RigaOrdineBean;
import org.example.use_cases.usa_voucher.UsaVoucherController;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller Applicativo per lo Use Case "Crea Ordine".
 * 
 * Responsabilità:
 * - Orchestrare la logica di business per la creazione dell'ordine
 * - Convertire i Bean in Entity e viceversa
 * - Applicare il pattern Decorator per gli add-on
 * - DELEGARE la gestione voucher a UsaVoucherController (GRASP: Low Coupling)
 *
 * Segue il pattern BCE: questo è il CONTROL.
 */
public class CreaOrdineController {

    private Ordine ordineCorrente;
    private final UsaVoucherController voucherController;

    public CreaOrdineController() {
        // Il controller viene istanziato dal Facade o dal Controller Grafico
        this.voucherController = new UsaVoucherController();
    }

    /**
     * Inizializza un nuovo ordine per il cliente
     * 
     * @param clienteId ID del cliente
     * @return OrdineBean con i dati dell'ordine creato
     */
    public OrdineBean inizializzaNuovoOrdine(String clienteId) throws DAOException {
        // Crea un nuovo ordine tramite la LazyFactory
        ordineCorrente = OrdineLazyFactory.getInstance().newOrdine(clienteId);

        // Converti in Bean e restituisci
        OrdineBean bean = new OrdineBean();
        bean.setNumeroOrdine(ordineCorrente.getNumeroOrdine());
        bean.setClienteId(clienteId);
        return bean;
    }

    /**
     * Recupera tutti i prodotti base disponibili
     * 
     * @return Lista di FoodBean dei prodotti base
     */
    public List<FoodBean> getProdottiBaseDisponibili() throws DAOException, ObjectNotFoundException,
            MissingAuthorizationException, WrongListQueryIdentifierValue, UserNotFoundException,
            UnrecognizedRoleException {

        List<Food> foodBase = FoodLazyFactory.getInstance().getAllFoodBase();
        return convertFoodListToBeanList(foodBase);
    }

    /**
     * Recupera tutti gli add-on disponibili
     * 
     * @return Lista di FoodBean degli add-on
     */
    public List<FoodBean> getAddOnDisponibili() throws DAOException, ObjectNotFoundException,
            MissingAuthorizationException, WrongListQueryIdentifierValue, UserNotFoundException,
            UnrecognizedRoleException {

        List<Food> addOns = FoodLazyFactory.getInstance().getAllAddOn();
        return convertFoodListToBeanList(addOns);
    }

    /**
     * Aggiunge un prodotto all'ordine corrente con gli add-on selezionati.
     * Applica il pattern Decorator per gli add-on.
     * 
     * @param foodBean il prodotto da aggiungere con gli add-on selezionati
     * @return true se l'aggiunta è andata a buon fine
     */
    public boolean aggiungiProdottoAOrdine(FoodBean foodBean) {
        if (ordineCorrente == null || foodBean == null) {
            return false;
        }

        // Crea il prodotto base usando Factory Method
        Food prodotto = creaProdottoBase(foodBean.getClasse());
        if (prodotto == null) {
            return false;
        }

        // Applica gli add-on usando il pattern Decorator
        for (String addOnClasse : foodBean.getAddOnSelezionati()) {
            prodotto = applicaDecorator(prodotto, addOnClasse);
        }

        // Aggiungi all'ordine
        ordineCorrente.aggiungiProdotto(prodotto);
        return true;
    }

    /**
     * Rimuove un prodotto dall'ordine
     * 
     * @param index indice del prodotto da rimuovere
     * @return true se la rimozione è andata a buon fine
     */
    public boolean rimuoviProdottoDaOrdine(int index) {
        if (ordineCorrente == null) {
            return false;
        }

        List<Food> prodotti = ordineCorrente.getProdotti();
        if (index >= 0 && index < prodotti.size()) {
            ordineCorrente.rimuoviProdotto(prodotti.get(index));
            return true;
        }
        return false;
    }

    /**
     * Applica un voucher all'ordine corrente.
     * DELEGA a UsaVoucherController (GRASP: Single Responsibility).
     * 
     * @param codiceVoucher il codice del voucher da applicare
     * @return VoucherBean con i dati del voucher applicato, null se non valido
     */
    public VoucherBean applicaVoucher(String codiceVoucher) throws DAOException, ObjectNotFoundException,
            MissingAuthorizationException, WrongListQueryIdentifierValue, UserNotFoundException,
            UnrecognizedRoleException {

        if (ordineCorrente == null) {
            return null;
        }
        return voucherController.applicaVoucherAOrdine(ordineCorrente, codiceVoucher);
    }

    /**
     * Rimuove il voucher dall'ordine corrente.
     * DELEGA a UsaVoucherController.
     */
    public void rimuoviVoucher() {
        voucherController.rimuoviVoucherDaOrdine(ordineCorrente);
    }

    /**
     * Calcola e restituisce il riepilogo dell'ordine corrente.
     * 
     * @return RiepilogoOrdineBean con tutti i dati calcolati
     */
    public RiepilogoOrdineBean getRiepilogoOrdine() {
        if (ordineCorrente == null) {
            return null;
        }

        RiepilogoOrdineBean riepilogo = new RiepilogoOrdineBean();
        riepilogo.setNumeroOrdine(ordineCorrente.getNumeroOrdine());

        // Aggiungi le righe dei prodotti
        for (Food food : ordineCorrente.getProdotti()) {
            RigaOrdineBean riga = new RigaOrdineBean(
                    food.getDescrizione(),
                    food.getCosto(),
                    food.getDurata());
            riepilogo.aggiungiRiga(riga);
        }

        // Calcola i totali
        riepilogo.setSubtotale(ordineCorrente.getSubtotale());
        riepilogo.setSconto(ordineCorrente.getSconto());
        riepilogo.setTotale(ordineCorrente.getTotale());
        riepilogo.setDurataTotale(ordineCorrente.getDurataTotale());

        // Info voucher - delega a UsaVoucherController per la conversione
        if (voucherController.hasVoucherApplicato(ordineCorrente)) {
            Voucher v = ordineCorrente.getVoucher();
            riepilogo.setVoucherApplicato(true);
            riepilogo.setCodiceVoucher(v.getCodice());
            riepilogo.setDescrizioneVoucher(v.getDescrizione());
        } else {
            riepilogo.setVoucherApplicato(false);
        }

        return riepilogo;
    }

    /**
     * Conferma e salva l'ordine nel sistema.
     * 
     * @return true se l'ordine è stato confermato con successo
     */
    public boolean confermaOrdine() throws DAOException, MissingAuthorizationException {
        if (ordineCorrente == null || ordineCorrente.getProdotti().isEmpty()) {
            return false;
        }

        // Salva l'ordine tramite il DAO
        OrdineLazyFactory.getInstance().salvaOrdine(ordineCorrente);
        return true;
    }

    /**
     * Annulla l'ordine corrente
     */
    public void annullaOrdine() {
        ordineCorrente = null;
    }

    // ==================== METODI PRIVATI DI SUPPORTO ====================

    /**
     * Factory method per creare un prodotto base dalla classe
     */
    private Food creaProdottoBase(String classe) {
        if (classe == null)
            return null;

        switch (classe) {
            case "PaninoDonerKebab":
                return new PaninoDonerKebab();
            case "PiadinaDonerKebab":
                return new PiadinaDonerKebab();
            case "KebabAlPiatto":
                return new KebabAlPiatto();
            default:
                return null;
        }
    }

    /**
     * Applica un decorator (add-on) al prodotto usando il pattern Decorator
     */
    private Food applicaDecorator(Food food, String addOnClasse) {
        if (addOnClasse == null || food == null)
            return food;

        switch (addOnClasse) {
            case "Cipolla":
                return new Cipolla(food);
            case "SalsaYogurt":
                return new SalsaYogurt(food);
            case "Patatine":
                return new Patatine(food);
            case "MixVerdureGrigliate":
                return new MixVerdureGrigliate(food);
            default:
                return food;
        }
    }

    /**
     * Converte una lista di Food in lista di FoodBean
     */
    private List<FoodBean> convertFoodListToBeanList(List<Food> foodList) {
        List<FoodBean> beans = new ArrayList<>();
        for (Food food : foodList) {
            beans.add(convertFoodToBean(food));
        }
        return beans;
    }

    /**
     * Converte un Food in FoodBean
     */
    private FoodBean convertFoodToBean(Food food) {
        FoodBean bean = new FoodBean();
        bean.setId(food.getId());
        bean.setDescrizione(food.getDescrizione());
        bean.setCosto(food.getCosto());
        bean.setDurata(food.getDurata());
        bean.setTipo(food.getTipo());
        bean.setClasse(food.getClass().getSimpleName());
        return bean;
    }
}