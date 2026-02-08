package org.example.model.food.decorator;

import org.example.model.food.Food;

/**
 * ConcreteDecorator: aggiunge cipolla al Food decorato.
 * 
 * Pattern GoF: Decorator
 * - Estende FoodDecorator
 * - Chiama super.metodo() e aggiunge il proprio contributo
 */
public class Cipolla extends FoodDecorator {

    private static final double COSTO_ADDON = 0.50;
    private static final int DURATA_ADDON = 1;

    public Cipolla(Food food) {
        super(food);
        this.descrizione = "Cipolla";
    }

    @Override
    public String getDescrizione() {
        return super.getDescrizione() + ", Cipolla";
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