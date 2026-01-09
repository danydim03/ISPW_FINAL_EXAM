package org.example.model.food.DAO;

import org.example.exceptions.*;
import org.example.instances_management_abstracts.DAODBAbstract;
import org.example.model.food.*;
import org.example.model.food.decorator.*;

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
        String descrizione = rs.getString(DESCRIZIONE);
        String classe = rs.getString(CLASSE);

        // Factory method per creare l'istanza corretta in base alla classe salvata
        return createFoodInstance(foodId, descrizione, classe);
    }

    /**
     * Factory method per creare l'istanza corretta di Food
     */
    private Food createFoodInstance(Long id, String descrizione, String classe) {
        Food food;

        switch (classe) {
            case "PaninoDonerKebab":
                food = new PaninoDonerKebab(id);
                break;
            case "PiadinaDonerKebab":
                food = new PiadinaDonerKebab(id);
                break;
            case "KebabAlPiatto":
                food = new KebabAlPiatto(id);
                break;
            case "Cipolla":
                food = new Cipolla(null); // Il food decorato verrà impostato dopo
                food.setId(id);
                break;
            case "SalsaYogurt":
                food = new SalsaYogurt(null);
                food.setId(id);
                break;
            case "Patatine":
                food = new Patatine(null);
                food.setId(id);
                break;
            case "MixVerdureGrigliate":
                food = new MixVerdureGrigliate(null);
                food.setId(id);
                break;
            default:
                // Fallback generico
                food = new PaninoDonerKebab(id);
                food.setDescrizione(descrizione);
        }

        return food;
    }

    @Override
    protected String setGetListQueryIdentifiersValue(Food food, int valueNumber) throws DAOException {
        if (valueNumber == 0) {
            return food.getTipo();
        }
        return null;
    }
}