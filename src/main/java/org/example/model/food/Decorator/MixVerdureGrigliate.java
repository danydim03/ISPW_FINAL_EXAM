package org.example.model.food.Decorator;

import org.example.model.food.Food;

public class MixVerdureGrigliate extends DecoratorAddON {

    public MixVerdureGrigliate(Food food) {
        super(food);
    }

    @Override
    public String getDescrizione() {
        if (foodDecorato == null) {
            return "Mix Verdure Grigliate";
        }
        return foodDecorato.getDescrizione() + ", Mix Verdure Grigliate";
    }

    @Override
    public double getCostoPlus() {
        return 1.50;
    }

    @Override
    public int getDurataPlus() {
        return 2;
    }
}