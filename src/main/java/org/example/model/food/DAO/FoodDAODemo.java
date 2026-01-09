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

    @Override
    public List<Food> getAllAddOn() throws DAOException {
        List<Food> addons = new ArrayList<>();
        // Using null as inner food for info-only objects
        addons.add(new org.example.model.food.Decorator.Cipolla(null));
        addons.add(new org.example.model.food.Decorator.Patatine(null));
        addons.add(new org.example.model.food.Decorator.SalsaYogurt(null));
        addons.add(new org.example.model.food.Decorator.MixVerdureGrigliate(null));
        return addons;
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
