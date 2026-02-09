package org.example.model.food.DAO;

import org.example.exceptions.DAOException;
import org.example.exceptions.ObjectNotFoundException;
import org.example.model.food.*;

import java.util.ArrayList;
import java.util.List;

public class FoodDAODemo implements FoodDAOInterface {

    @Override
    public List<Food> getAllFoodBase() throws DAOException {
        List<Food> foods = new ArrayList<>();
        foods.add(new PaninoDonerKebab());
        foods.add(new PiadinaDonerKebab());
        foods.add(new KebabAlPiatto());
        return foods;
    }

    /**
     * @deprecated Usa getAllAddOnDescriptors() per i metadati degli add-on.
     *             Questo metodo esiste solo per retrocompatibilità.
     */
    @Override
    @Deprecated
    public List<Food> getAllAddOn() throws DAOException {
        throw new UnsupportedOperationException(
                "GoF Compliance: usare getAllAddOnDescriptors() per i metadati degli add-on. " +
                        "I Decorator vengono istanziati solo tramite FoodFactory.applicaDecorator().");
    }

    /**
     * Recupera i metadati degli add-on come AddOnDescriptor (Value Objects).
     * Non istanzia alcun Decorator, garantendo GoF compliance.
     */
    @Override
    public List<AddOnDescriptor> getAllAddOnDescriptors() throws DAOException {
        List<AddOnDescriptor> descriptors = new ArrayList<>();
        descriptors.add(new AddOnDescriptor(4L, "Cipolla", 0.50, 1, "Cipolla"));
        descriptors.add(new AddOnDescriptor(5L, "Patatine", 1.50, 3, "Patatine"));
        descriptors.add(new AddOnDescriptor(6L, "Salsa Yogurt", 0.50, 1, "SalsaYogurt"));
        descriptors.add(new AddOnDescriptor(7L, "Mix Verdure Grigliate", 1.00, 2, "MixVerdureGrigliate"));
        return descriptors;
    }

    @Override
    public void insert(Food food) {
        throw new UnsupportedOperationException("Insert not supported in demo mode");
    }

    @Override
    public void delete(Food food) {
        throw new UnsupportedOperationException("Delete not supported in demo mode");
    }

    @Override
    public void update(Food food) {
        throw new UnsupportedOperationException("Update not supported in demo mode");
    }

    @Override
    public Food getFoodById(Long id) throws ObjectNotFoundException {
        throw new ObjectNotFoundException("Food not found in Demo");
    }
}
