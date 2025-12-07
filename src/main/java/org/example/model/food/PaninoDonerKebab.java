package org.example.model. food;

public class PaninoDonerKebab extends Food {

    private static final double COSTO_BASE = 5.50;
    private static final int DURATA_BASE = 5;

    public PaninoDonerKebab() {
        super();
        this.descrizione = "Panino Doner Kebab";
        this.tipo = "BASE";
    }

    public PaninoDonerKebab(Long id) {
        super(id, "Panino Doner Kebab", "BASE");
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