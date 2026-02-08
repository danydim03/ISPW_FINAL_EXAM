package org.example.model.food.decorator;

import org.example.model.food.Food;

/**
 * ConcreteDecorator: aggiunge patatine al Food decorato.
 * 
 * Pattern GoF: Decorator
 * - Estende FoodDecorator
 * - Chiama super.metodo() e aggiunge il proprio contributo
 */
public class Patatine extends FoodDecorator {

    private static final double COSTO_ADDON = 2.00;
    private static final int DURATA_ADDON = 3;

    public Patatine(Food food) {
        super(food);
    }

    @Override
    public String getDescrizione() {
        return super.getDescrizione() + ", Patatine";
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