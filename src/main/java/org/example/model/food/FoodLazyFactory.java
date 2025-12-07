package org.example.model. food;

import org.example. dao_manager.DAOFactoryAbstract;
import org.example.enums.ExceptionMessagesEnum;
import org.example.exceptions.*;

import java.util.ArrayList;
import java.util.List;

public class FoodLazyFactory {
    
    private static FoodLazyFactory instance;
    private final List<Food> foodCache;
    
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
     * Recupera un Food per ID, usando la cache se disponibile
     */
    public Food getFoodById(Long id) throws DAOException, ObjectNotFoundException, 
            MissingAuthorizationException, WrongListQueryIdentifierValue, UserNotFoundException, UnrecognizedRoleException {
        
        // Cerca prima nella cache
        for (Food f : foodCache) {
            if (f.getId() != null && f.getId().equals(id)) {
                return f;
            }
        }
        
        // Se non trovato, recupera dal DAO
        try {
            Food daoFood = DAOFactoryAbstract.getInstance().getFoodDAO().getFoodById(id);
            foodCache.add(daoFood);
            return daoFood;
        } catch (PropertyException | ResourceNotFoundException e) {
            throw new DAOException(ExceptionMessagesEnum. DAO. message, e);
        }
    }
    
    /**
     * Recupera tutti i prodotti base (tipo = BASE)
     */
    public List<Food> getAllFoodBase() throws DAOException, ObjectNotFoundException, 
            MissingAuthorizationException, WrongListQueryIdentifierValue, UserNotFoundException, UnrecognizedRoleException {
        try {
            List<Food> foodBase = DAOFactoryAbstract.getInstance().getFoodDAO(). getAllFoodBase();
            // Aggiorna cache
            for (Food f : foodBase) {
                if (! isFoodInCache(f. getId())) {
                    foodCache.add(f);
                }
            }
            return foodBase;
        } catch (PropertyException | ResourceNotFoundException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO. message, e);
        }
    }
    
    /**
     * Recupera tutti gli addon (tipo = ADDON)
     */
    public List<Food> getAllAddOn() throws DAOException, ObjectNotFoundException, 
            MissingAuthorizationException, WrongListQueryIdentifierValue, UserNotFoundException, UnrecognizedRoleException {
        try {
            List<Food> addons = DAOFactoryAbstract.getInstance().getFoodDAO().getAllAddOn();
            // Aggiorna cache
            for (Food f : addons) {
                if (!isFoodInCache(f.getId())) {
                    foodCache.add(f);
                }
            }
            return addons;
        } catch (PropertyException | ResourceNotFoundException e) {
            throw new DAOException(ExceptionMessagesEnum. DAO.message, e);
        }
    }
    
    /**
     * Crea e salva un nuovo Food
     */
    public Food newFood(Food food) throws DAOException, MissingAuthorizationException {
        try {
            DAOFactoryAbstract.getInstance().getFoodDAO().insert(food);
        } catch (PropertyException | ResourceNotFoundException e) {
            throw new DAOException(ExceptionMessagesEnum.DAO.message, e);
        }
        foodCache.add(food);
        return food;
    }
    
    /**
     * Verifica se un Food è già in cache
     */
    private boolean isFoodInCache(Long id) {
        if (id == null) return false;
        for (Food f : foodCache) {
            if (f.getId() != null && f.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Pulisce la cache (utile per testing)
     */
    public void clearCache() {
        foodCache.clear();
    }
}