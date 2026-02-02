package org.example.enums;

import org.example.model.food.*;

/**
 * Enum che rappresenta i tipi di prodotti base (Food) disponibili nel sistema.
 * Elimina le magic strings dalla FoodFactory seguendo best practices.
 */
public enum FoodTypeEnum {

    PANINO_DONER_KEBAB("PaninoDonerKebab") {
        @Override
        public Food createInstance() {
            return new PaninoDonerKebab();
        }
    },
    PIADINA_DONER_KEBAB("PiadinaDonerKebab") {
        @Override
        public Food createInstance() {
            return new PiadinaDonerKebab();
        }
    },
    KEBAB_AL_PIATTO("KebabAlPiatto") {
        @Override
        public Food createInstance() {
            return new KebabAlPiatto();
        }
    };

    private final String className;

    FoodTypeEnum(String className) {
        this.className = className;
    }

    /**
     * Crea una nuova istanza del Food corrispondente.
     * 
     * @return nuova istanza di Food
     */
    public abstract Food createInstance();

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
     * @return FoodTypeEnum corrispondente, o null se non trovato
     */
    public static FoodTypeEnum fromClassName(String className) {
        if (className == null) {
            return null;
        }
        for (FoodTypeEnum type : values()) {
            if (type.className.equals(className)) {
                return type;
            }
        }
        return null;
    }
}
