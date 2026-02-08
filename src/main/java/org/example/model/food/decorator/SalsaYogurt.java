package org.example.model.food.decorator;

import org.example.model.food.Food;

/**
 * ConcreteDecorator: aggiunge salsa yogurt al Food decorato.
 * 
 * Pattern GoF: Decorator
 * - Estende FoodDecorator
 * - Chiama super.metodo() e aggiunge il proprio contributo
 */
public class SalsaYogurt extends FoodDecorator {

    private static final double COSTO_ADDON = 0.80;
    private static final int DURATA_ADDON = 0;

    public SalsaYogurt(Food food) {
        super(food);
        this.descrizione = "Salsa Yogurt";
    }

    @Override
    public String getDescrizione() {
        return super.getDescrizione() + ", Salsa Yogurt";
    }

    @Override
    public double getCosto() {
        return super.getCosto() + COSTO_ADDON;
    }

    @Override
    public int getDurata() {
        return super.getDurata() + DURATA_ADDON;
    }
}