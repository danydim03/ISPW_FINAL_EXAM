package org.example.model.food.decorator;

import org.example.model.food.Food;

/**
 * ConcreteDecorator: aggiunge mix verdure grigliate al Food decorato.
 * 
 * Pattern GoF: Decorator
 * - Estende FoodDecorator
 * - Chiama super.metodo() e aggiunge il proprio contributo
 */
public class MixVerdureGrigliate extends FoodDecorator {

    private static final double COSTO_ADDON = 1.50;
    private static final int DURATA_ADDON = 2;

    public MixVerdureGrigliate(Food food) {
        super(food);
        this.descrizione = "Mix Verdure Grigliate";
    }

    @Override
    public String getDescrizione() {
        return super.getDescrizione() + ", Mix Verdure Grigliate";
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