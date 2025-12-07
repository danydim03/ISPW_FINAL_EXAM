package org.example.model.food;

public class KebabAlPiatto extends Food {

    private static final double COSTO_BASE = 8.00;
    private static final int DURATA_BASE = 8;

    public KebabAlPiatto() {
        super();
        this.descrizione = "Kebab al Piatto";
        this.tipo = "BASE";
    }

    public KebabAlPiatto(Long id) {
        super(id, "Kebab al Piatto", "BASE");
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