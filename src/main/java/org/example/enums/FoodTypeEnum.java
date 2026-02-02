package org.example.enums;

/**
 * Enum che rappresenta i tipi di prodotti base (Food) disponibili nel sistema.
 * 
 * GRASP Compliance:
 * - High Cohesion: responsabilità unica di identificare i tipi di Food
 * - La creazione degli oggetti è delegata a FoodFactory (Creator)
 */
public enum FoodTypeEnum {

    PANINO_DONER_KEBAB("PaninoDonerKebab"),
    PIADINA_DONER_KEBAB("PiadinaDonerKebab"),
    KEBAB_AL_PIATTO("KebabAlPiatto");

    private final String className;

    FoodTypeEnum(String className) {
        this.className = className;
    }

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
