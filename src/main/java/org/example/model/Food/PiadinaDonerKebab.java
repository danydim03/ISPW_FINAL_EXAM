package org.example.model.Food;

public class PiadinaDonerKebab extends Food {

    public PiadinaDonerKebab() {
        descrizione = "Piadina Doner Kebab";
    }

    @Override
    public double getCosto() {
        return 6.00;
    }

    @Override
    public int getDurata() {
        return 6;
    }
}