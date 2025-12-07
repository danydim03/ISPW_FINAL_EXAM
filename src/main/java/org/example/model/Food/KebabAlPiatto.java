package org.example.model.Food;

public class KebabAlPiatto extends Food {

    public KebabAlPiatto() {
        descrizione = "Kebab al Piatto";
    }

    @Override
    public double getCosto() {
        return 8.00;
    }

    @Override
    public int getDurata() {
        return 8;
    }
}