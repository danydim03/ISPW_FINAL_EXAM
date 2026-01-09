package org.example.model.food.decorator;

import org.example.model.food.Food;

public class Patatine extends DecoratorAddON {

    public Patatine(Food food) {
        super(food);
    }

    @Override
    public String getDescrizione() {
        if (foodDecorato == null) {
            return "Patatine";
        }
        return foodDecorato.getDescrizione() + ", Patatine";
    }

    @Override
    public double getCostoPlus() {
        return 2.00;
    }

    @Override
    public int getDurataPlus() {
        return 3;
    }
}