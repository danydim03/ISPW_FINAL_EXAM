package org.example. model.food;

public class PiadinaDonerKebab extends Food {

    private static final double COSTO_BASE = 6.00;
    private static final int DURATA_BASE = 6;

    public PiadinaDonerKebab() {
        super();
        this.descrizione = "Piadina Doner Kebab";
        this.tipo = "BASE";
    }

    public PiadinaDonerKebab(Long id) {
        super(id, "Piadina Doner Kebab", "BASE");
    }

    @Override
    public double getCosto() {
        return COSTO_BASE;
    }

    @Override
    public int getDurata() {
        return DURATA_BASE;
    }
}