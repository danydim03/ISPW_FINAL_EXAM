package org.example.enums;

import org.example.model.food.Food;
import org.example.model.food.decorator.*;

/**
 * Enum che rappresenta i tipi di add-on (Decorator) disponibili nel sistema.
 * Elimina le magic strings dalla FoodFactory seguendo best practices.
 */
public enum AddOnTypeEnum {

    CIPOLLA("Cipolla") {
        @Override
        public Food applyTo(Food food) {
            return new Cipolla(food);
        }
    },
    SALSA_YOGURT("SalsaYogurt") {
        @Override
        public Food applyTo(Food food) {
            return new SalsaYogurt(food);
        }
    },
    PATATINE("Patatine") {
        @Override
        public Food applyTo(Food food) {
            return new Patatine(food);
        }
    },
    MIX_VERDURE_GRIGLIATE("MixVerdureGrigliate") {
        @Override
        public Food applyTo(Food food) {
            return new MixVerdureGrigliate(food);
        }
    };

    private final String className;

    AddOnTypeEnum(String className) {
        this.className = className;
    }

    /**
     * Applica il decorator (add-on) al Food fornito.
     * 
     * @param food il prodotto base da decorare
     * @return Food decorato
     */
    public abstract Food applyTo(Food food);

    /**
     * Restituisce il nome della classe come stringa.
     * 
     * @return nome classe
     */
    public String getClassName() {
        return className;
    }

    /**
     * Trova l'enum dal nome della classe.
     * 
     * @param className nome della classe
     * @return AddOnTypeEnum corrispondente, o null se non trovato
     */
    public static AddOnTypeEnum fromClassName(String className) {
        if (className == null) {
            return null;
        }
        for (AddOnTypeEnum type : values()) {
            if (type.className.equals(className)) {
                return type;
            }
        }
        return null;
    }
}
