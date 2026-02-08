package org.example.model.food.DAO;

import org.example.exceptions.*;
import org.example.instances_management_abstracts.DAODBAbstract;
import org.example.model.food.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class FoodDAODB extends DAODBAbstract<Food> implements FoodDAOInterface {

    private static final String FOOD = "FOOD";
    private static final String ID = "id";
    private static final String DESCRIZIONE = "descrizione";
    private static final String TIPO = "tipo";
    private static final String CLASSE = "classe";
    private static final String COSTO = "costo";
    private static final String DURATA = "durata";

    protected static FoodDAOInterface instance;

    private FoodDAODB() {
    }

    public static synchronized FoodDAOInterface getInstance() {
        if (instance == null) {
            instance = new FoodDAODB();
        }
        return instance;
    }

    @Override
    public Food getFoodById(Long id)
            throws DAOException, ObjectNotFoundException, PropertyException, ResourceNotFoundException,
            UserNotFoundException, UnrecognizedRoleException, MissingAuthorizationException,
            WrongListQueryIdentifierValue {
        return getQuery(
                FOOD,
                List.of(ID),
                List.of(id),
                List.of());
    }

    @Override
    public List<Food> getAllFoodBase() throws DAOException, PropertyException, ResourceNotFoundException,
            UserNotFoundException, UnrecognizedRoleException, ObjectNotFoundException, MissingAuthorizationException,
            WrongListQueryIdentifierValue {
        return getListQuery(
                FOOD,
                List.of(TIPO),
                List.of("BASE"),
                List.of(),
                List.of(),
                Boolean.FALSE);
    }

    @Override
    public List<Food> getAllAddOn() throws DAOException, PropertyException, ResourceNotFoundException,
            UserNotFoundException, UnrecognizedRoleException, ObjectNotFoundException, MissingAuthorizationException,
            WrongListQueryIdentifierValue {
        return getListQuery(
                FOOD,
                List.of(TIPO),
                List.of("ADDON"),
                List.of(),
                List.of(),
                Boolean.FALSE);
    }

    @Override
    public void insert(Food food)
            throws DAOException, PropertyException, ResourceNotFoundException, MissingAuthorizationException {
        insertQuery(
                FOOD,
                List.of(
                        food.getDescrizione(),
                        food.getTipo(),
                        food.getClass().getSimpleName(),
                        food.getCosto(),
                        food.getDurata()));
    }

    @Override
    public void delete(Food food) throws DAOException, PropertyException, ResourceNotFoundException {
        deleteQuery(
                FOOD,
                List.of(ID),
                List.of(food.getId()));
    }

    @Override
    public void update(Food food)
            throws DAOException, PropertyException, ResourceNotFoundException, MissingAuthorizationException {
        updateQuery(
                FOOD,
                List.of(DESCRIZIONE, TIPO, CLASSE, COSTO, DURATA),
                List.of(food.getDescrizione(), food.getTipo(), food.getClass().getSimpleName(), food.getCosto(),
                        food.getDurata()),
                List.of(ID),
                List.of(food.getId()));
    }

    @Override
    protected Food queryObjectBuilder(ResultSet rs, List<Object> objects) throws SQLException, DAOException,
            PropertyException, ResourceNotFoundException, UserNotFoundException, UnrecognizedRoleException,
            ObjectNotFoundException, MissingAuthorizationException, WrongListQueryIdentifierValue {

        Long foodId = rs.getLong(ID);

        String classe = rs.getString(CLASSE);

        // Factory method per creare l'istanza corretta in base alla classe salvata
        return FoodRegistry.create(classe, foodId);
    }

    /**
     * Factory method per creare l'istanza corretta di Food
     */

    @Override
    protected String setGetListQueryIdentifiersValue(Food food, int valueNumber) throws DAOException {
        if (valueNumber == 0) {
            return food.getTipo();
        }
        return null;
    }
}