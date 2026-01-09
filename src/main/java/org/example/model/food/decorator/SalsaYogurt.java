package org.example.model.food.decorator;

import org.example.model.food.Food;

public class SalsaYogurt extends DecoratorAddON {

    public SalsaYogurt(Food food) {
        super(food);
    }

    @Override
    public String getDescrizione() {
        if (foodDecorato == null) {
            return "Salsa Yogurt";
        }
        return foodDecorato.getDescrizione() + ", Salsa Yogurt";
    }

    @Override
    public double getCostoPlus() {
        return 0.80;
    }

    @Override
    public int getDurataPlus() {
        return 0;
    }
}