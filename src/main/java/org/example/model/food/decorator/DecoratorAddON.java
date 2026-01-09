package org.example.model.food.decorator;

import org.example.model.food.Food;

public abstract class DecoratorAddON extends Food {

    protected Food foodDecorato;

    public DecoratorAddON(Food food) {
        this.foodDecorato = food;
        this.tipo = "ADDON";
    }

    @Override
    public abstract String getDescrizione();

    // Costo aggiuntivo dell'add-on
    public abstract double getCostoPlus();

    // Durata aggiuntiva dell'add-on
    public abstract int getDurataPlus();

    @Override
    public double getCosto() {
        // If used standalone (for listing addons), return only addon cost
        if (foodDecorato == null) {
            return getCostoPlus();
        }
        return foodDecorato.getCosto() + getCostoPlus();
    }

    @Override
    public int getDurata() {
        // If used standalone (for listing addons), return only addon duration
        if (foodDecorato == null) {
            return getDurataPlus();
        }
        return foodDecorato.getDurata() + getDurataPlus();
    }
}
