package org.example.use_cases.crea_ordine;

import org.example.exceptions.*;
import org.example.model.food.*;
import org.example.model.ordine.Ordine;
import org.example.model.ordine.OrdineLazyFactory;
import org.example.use_cases.crea_ordine.beans.*;
import org.example.use_cases.usa_voucher.UsaVoucherController;
import org.example.events.*;
import java.util.List;
import org.example.mappers.*;

/**
 * Controller Applicativo per lo Use Case "Crea Ordine".
 * 
 * <p>
 * <b>STATELESS</b>: questo controller non mantiene stato interno.
 * Recupera l'Entity Ordine tramite ID dal Singleton Factory.
 * </p>
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

    /**
     * Metodo helper per ottenere un'istanza di UsaVoucherController.
     * Creato on-demand per mantenere il controller stateless.
     * 
     * @return nuova istanza di UsaVoucherController
     */
    private UsaVoucherController getVoucherController() {
        return new UsaVoucherController();
    }

    /**
     * Metodo helper per ottenere l'istanza di OrdineLazyFactory.
     * Riduce il coupling diretto con il Singleton e migliora la testabilità.
     * Può essere sovrascritto in sottoclassi di test per iniettare mock.
     * 
     * @return istanza di OrdineLazyFactory
     */
    protected OrdineLazyFactory getOrdineFactory() {
        return OrdineLazyFactory.getInstance();
    }

    /**
     * Metodo helper per ottenere l'istanza di FoodLazyFactory.
     * Riduce il coupling diretto con il Singleton e migliora la testabilità.
     * 
     * @return istanza di FoodLazyFactory
     */
    protected FoodLazyFactory getFoodFactory() {
        return FoodLazyFactory.getInstance();
    }

    /**
     * Crea un nuovo ordine per il cliente.
     * L'ordine viene registrato nel Factory e il suo ID viene restituito nel Bean.
     * 
     * @param clienteId ID del cliente
     * @return OrdineBean con i dati dell'ordine creato (incluso numeroOrdine)
     */
    public OrdineBean creaNuovoOrdine(String clienteId) throws DAOException {
        Ordine ordine = getOrdineFactory().newOrdine(clienteId);
        return OrdineMapper.toBean(ordine);
    }

    /**
     * Recupera tutti i prodotti base disponibili
     * 
     * @return Lista di FoodBean dei prodotti base
     */
    public List<FoodBean> getProdottiBaseDisponibili() throws DAOException, ObjectNotFoundException,
            MissingAuthorizationException, WrongListQueryIdentifierValue, UserNotFoundException,
            UnrecognizedRoleException {

        List<Food> foodBase = getFoodFactory().getAllFoodBase();
        return FoodMapper.toBeanList(foodBase);
    }

    /**
     * Recupera tutti gli add-on disponibili
     * 
     * @return Lista di FoodBean degli add-on
     */
    public List<FoodBean> getAddOnDisponibili() throws DAOException, ObjectNotFoundException,
            MissingAuthorizationException, WrongListQueryIdentifierValue, UserNotFoundException,
            UnrecognizedRoleException {

        List<Food> addOns = getFoodFactory().getAllAddOn();
        return FoodMapper.toBeanList(addOns);
    }

    /**
     * Aggiunge un prodotto all'ordine con gli add-on selezionati.
     * Applica il pattern Decorator per gli add-on.
     * 
     * @param ordineId ID dell'ordine (recuperato dal Factory)
     * @param foodBean il prodotto da aggiungere con gli add-on selezionati
     * @return true se l'aggiunta è andata a buon fine
     */
    public boolean aggiungiProdottoAOrdine(String ordineId, FoodBean foodBean) {
        Ordine ordine = getOrdineFactory().getOrdineById(ordineId);

        if (ordine == null || foodBean == null) {
            return false;
        }

        // Creo il prodotto base usando FoodFactory (GRASP: Low Coupling)
        Food prodotto = FoodFactory.creaProdottoBase(foodBean.getClasse());
        if (prodotto == null) {
            return false;
        }

        // Applica gli add-on usando il pattern Decorator
        for (String addOnClasse : foodBean.getAddOnSelezionati()) {
            prodotto = FoodFactory.applicaDecorator(prodotto, addOnClasse);
        }

        // Aggiungi all'ordine
        ordine.aggiungiProdotto(prodotto);
        return true;
    }

    /**
     * Rimuove un prodotto dall'ordine
     * 
     * @param ordineId ID dell'ordine
     * @param index    indice del prodotto da rimuovere
     * @return true se la rimozione è andata a buon fine
     */
    public boolean rimuoviProdottoDaOrdine(String ordineId, int index) {
        Ordine ordine = getOrdineFactory().getOrdineById(ordineId);

        if (ordine == null) {
            return false;
        }

        List<Food> prodotti = ordine.getProdotti();
        if (index >= 0 && index < prodotti.size()) {
            ordine.rimuoviProdotto(prodotti.get(index));
            return true;
        }
        return false;
    }

    /**
     * Applica un voucher all'ordine.
     * DELEGA a UsaVoucherController (GRASP: Single Responsibility).
     * 
     * @param ordineId      ID dell'ordine
     * @param codiceVoucher il codice del voucher da applicare
     * @return VoucherBean con i dati del voucher applicato, null se non valido
     */
    public VoucherBean applicaVoucher(String ordineId, String codiceVoucher)
            throws DAOException, ObjectNotFoundException, MissingAuthorizationException,
            WrongListQueryIdentifierValue, UserNotFoundException, UnrecognizedRoleException {

        Ordine ordine = getOrdineFactory().getOrdineById(ordineId);

        if (ordine == null) {
            return null;
        }
        return getVoucherController().applicaVoucherAOrdine(ordine, codiceVoucher);
    }

    /**
     * Rimuove il voucher dall'ordine.
     * DELEGA a UsaVoucherController.
     * 
     * @param ordineId ID dell'ordine
     */
    public void rimuoviVoucher(String ordineId) {
        Ordine ordine = getOrdineFactory().getOrdineById(ordineId);
        if (ordine != null) {
            getVoucherController().rimuoviVoucherDaOrdine(ordine);
        }
    }

    /**
     * Calcola e restituisce il riepilogo dell'ordine.
     * Delega la conversione a RiepilogoMapper (GRASP: High Cohesion).
     * 
     * @param ordineId ID dell'ordine
     * @return RiepilogoOrdineBean con tutti i dati calcolati
     */
    public RiepilogoOrdineBean getRiepilogoOrdine(String ordineId) {
        Ordine ordine = getOrdineFactory().getOrdineById(ordineId);
        return RiepilogoMapper.toBean(ordine);
    }

    /**
     * Conferma e salva l'ordine nel sistema.
     * 
     * <p>
     * Dopo il salvataggio, pubblica un evento {@link OrdineEvent} per notificare
     * attivamente l'Amministratore (A2) che un nuovo ordine è stato confermato.
     * </p>
     * 
     * @param ordineId ID dell'ordine
     * @return true se l'ordine è stato confermato con successo
     * @throws DAOException                  se si verifica un errore durante il
     *                                       salvataggio
     * @throws MissingAuthorizationException se mancano le autorizzazioni
     */
    public boolean confermaOrdine(String ordineId) throws DAOException, MissingAuthorizationException {
        Ordine ordine = getOrdineFactory().getOrdineById(ordineId);

        if (ordine == null || ordine.getProdotti().isEmpty()) {
            return false;
        }

        // Salva l'ordine tramite il DAO
        getOrdineFactory().salvaOrdine(ordine);

        // ==================== NOTIFICA ATTIVA (Pattern Observer) ====================
        OrdineEvent event = new OrdineEvent(
                ordine.getNumeroOrdine(),
                ordine.getClienteId(),
                ordine.getTotale());
        OrdineEventPublisher.getInstance().notifyOrdineConfermato(event);
        // =============================================================================

        // Rimuovi dalla cache dopo conferma
        getOrdineFactory().rimuoviOrdineInCorso(ordineId);

        return true;
    }

    /**
     * Annulla l'ordine corrente.
     * 
     * @param ordineId ID dell'ordine da annullare
     */
    public void annullaOrdine(String ordineId) {
        getOrdineFactory().rimuoviOrdineInCorso(ordineId);
    }
}