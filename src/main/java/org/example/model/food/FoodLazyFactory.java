package org.example.model.food;

import org.example.dao_manager.DAOFactoryAbstract;
import org.example.enums.ExceptionMessagesEnum;
import org.example.exceptions.*;

import java.util.ArrayList;
import java.util.List;

public class FoodLazyFactory {

    private static FoodLazyFactory instance;
    private final List<Food> foodCache;

    // Flag per tracciare se i dati sono già stati caricati dal DAO
    private boolean foodBaseLoaded = false;
    private boolean addOnLoaded = false;

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
     * Recupera tutti gli addon (tipo = ADDON).
     * Implementa il pattern Lazy Loading: prima controlla la cache,
     * solo se vuota carica dal DAO.
     */
    public List<Food> getAllAddOn() throws DAOException, ObjectNotFoundException,
            MissingAuthorizationException, WrongListQueryIdentifierValue, UserNotFoundException,
            UnrecognizedRoleException {

        // LAZY LOADING: Se già caricati, restituisci dalla cache
        if (addOnLoaded) {
            return getFoodFromCacheByTipo("ADDON");
        }

        // Prima volta: carica dal DAO
        try {
            List<Food> addons = DAOFactoryAbstract.getInstance().getFoodDAO().getAllAddOn();
            // Aggiorna cache
            for (Food f : addons) {
                if (!isFoodInCache(f.getId())) {
                    foodCache.add(f);
                }
            }
            addOnLoaded = true; // Marca come caricati
            return addons;
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
        addOnLoaded = false;
    }
}