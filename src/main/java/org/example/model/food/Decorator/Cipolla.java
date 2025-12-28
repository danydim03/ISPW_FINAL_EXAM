package org.example.model.food.Decorator;

import org.example.model.food.Food;

public class Cipolla extends DecoratorAddON {

    public Cipolla(Food food) {
        super(food);
    }

    @Override
    public String getDescrizione() {
        if (foodDecorato == null) {
            return "Cipolla";
        }
        return foodDecorato.getDescrizione() + ", Cipolla";
    }

    @Override
    public double getCostoPlus() {
        return 0.50;
    }

    @Override
    public int getDurataPlus() {
        return 1;
    }
}