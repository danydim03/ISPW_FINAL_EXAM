package org.example.enums;

/**
 * Enum che rappresenta i tipi di add-on (Decorator) disponibili nel sistema.
 * 
 * GRASP Compliance:
 * - High Cohesion: responsabilità unica di identificare i tipi di AddOn
 * - L'applicazione dei decorator è delegata a FoodFactory (Creator)
 */
public enum AddOnTypeEnum {

    CIPOLLA("Cipolla"),
    SALSA_YOGURT("SalsaYogurt"),
    PATATINE("Patatine"),
    MIX_VERDURE_GRIGLIATE("MixVerdureGrigliate");

    private final String className;

    AddOnTypeEnum(String className) {
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
