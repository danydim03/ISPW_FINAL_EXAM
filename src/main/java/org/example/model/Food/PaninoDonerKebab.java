package org.example.model.Food;

public class PaninoDonerKebab extends Food {

    public PaninoDonerKebab() {
        descrizione = "Panino Doner Kebab";
    }

    @Override
    public double getCosto() {
        return 5.50;
    }

    @Override
    public int getDurata() {
        return 5; // 5 minuti
    }
}