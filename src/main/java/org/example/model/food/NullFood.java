package org.example.model.food;

/**
 * Implementazione del Null Object Pattern per Food.
 * Rappresenta un Food "vuoto" o nullo, evitando l'uso di null references.
 * 
 * GRASP: Polymorphism (intercambiabile con altri Food)
 * GoF: Null Object (Special Case)
 */
public class NullFood extends Food {

    public NullFood() {
        super(null, "", "NULL");
    }

    @Override
    public double getCosto() {
        return 0.0;
    }

    @Override
    public int getDurata() {
        return 0;
    }

    @Override
    public String getDescrizione() {
        return "";
    }
}
