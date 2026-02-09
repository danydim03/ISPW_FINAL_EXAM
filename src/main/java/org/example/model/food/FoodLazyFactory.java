package org.example.model.food;

import org.example.dao_manager.DAOFactoryAbstract;
import org.example.enums.ExceptionMessagesEnum;
import org.example.exceptions.*;

import java.util.ArrayList;
import java.util.List;

public class FoodLazyFactory {

    private static FoodLazyFactory instance;
    private final List<Food> foodCache;

    // Flag per tracciare se i dati dei food base sono già stati caricati dal DAO
    private boolean foodBaseLoaded = false;

    private FoodLazyFactory() {
        foodCache = new ArrayList<>();
    }

    public static synchronized FoodLazyFactory getInstance() {
        if (instance == null) {
            instance = new FoodLazyFactory();
        }
        return instance;
    }

    /**
     * Recupera tutti i prodotti base (tipo = BASE).
     * Implementa il pattern Lazy Loading: prima controlla la cache,
     * solo se vuota carica dal DAO.
     */
    public List<Food> getAllFoodBase() throws DAOException, ObjectNotFoundException,
            MissingAuthorizationException, WrongListQueryIdentifierValue, UserNotFoundException,
            UnrecognizedRoleException {

        // LAZY LOADING: Se già caricati, restituisci dalla cache
        if (foodBaseLoaded) {
            return getFoodFromCacheByTipo("BASE");
        }

        // Prima volta: carica dal DAO
        try {
            List<Food> foodBase = DAOFactoryAbstract.getInstance().getFoodDAO().getAllFoodBase();
            // Aggiorna cache
            for (Food f : foodBase) {
                if (!isFoodInCache(f.getId())) {
                    foodCache.add(f);
                }
            }
            foodBaseLoaded = true; // Marca come caricati
            return foodBase;
        } catch (PropertyException | ResourceNotFoundException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    /**
     * @deprecated Usa getAllAddOnDescriptors() per i metadati degli add-on.
     *             I Decorator vengono istanziati solo tramite
     *             FoodFactory.applicaDecorator().
     */
    @Deprecated
    public List<Food> getAllAddOn() throws DAOException, ObjectNotFoundException,
            MissingAuthorizationException, WrongListQueryIdentifierValue, UserNotFoundException,
            UnrecognizedRoleException {
        throw new UnsupportedOperationException(
                "GoF Compliance: usare getAllAddOnDescriptors() per i metadati degli add-on.");
    }

    /**
     * Recupera i metadati di tutti gli add-on come AddOnDescriptor.
     * Implementa Lazy Loading per migliorare le performance.
     * 
     * <p>
     * <b>GoF Compliance:</b> NON istanzia Decorator, ma restituisce Value Objects
     * con i metadati letti direttamente dal DB/CSV.
     * </p>
     */
    public List<AddOnDescriptor> getAllAddOnDescriptors() throws DAOException, ObjectNotFoundException,
            MissingAuthorizationException, WrongListQueryIdentifierValue, UserNotFoundException,
            UnrecognizedRoleException {

        // Prima volta: carica dal DAO
        try {
            return DAOFactoryAbstract.getInstance().getFoodDAO().getAllAddOnDescriptors();
        } catch (PropertyException | ResourceNotFoundException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
    }

    /**
     * Filtra i Food dalla cache per tipo.
     */
    private List<Food> getFoodFromCacheByTipo(String tipo) {
        List<Food> result = new ArrayList<>();
        for (Food f : foodCache) {
            if (tipo.equals(f.getTipo())) {
                result.add(f);
            }
        }
        return result;
    }

    /**
     * Verifica se un Food è già in cache
     */
    private boolean isFoodInCache(Long id) {
        if (id == null)
            return false;
        for (Food f : foodCache) {
            if (f.getId() != null && f.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Pulisce la cache e resetta i flag di caricamento (utile per testing)
     */
    public void clearCache() {
        foodCache.clear();
        foodBaseLoaded = false;
    }
}