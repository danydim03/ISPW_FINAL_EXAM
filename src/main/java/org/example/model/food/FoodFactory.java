package org.example.model.food;

import org.example.enums.AddOnTypeEnum;
import org.example.enums.FoodTypeEnum;
import org.example.model.food.decorator.*;

/**
 * Factory per la creazione di prodotti Food e applicazione di Decorators.
 * 
 * GRASP Compliance:
 * - Creator: questa classe è responsabile della creazione di Food e Decorator
 * - Low Coupling: centralizza la logica di istanziazione
 * - High Cohesion: unica responsabilità di creare oggetti Food
 * 
 * Utilizza FoodTypeEnum e AddOnTypeEnum per identificare i tipi (no magic
 * strings).
 */
public class FoodFactory {

    private FoodFactory() {
        // Utility class - previene istanziazione
    }

    /**
     * Crea un prodotto base dalla classe specificata (stringa).
     * Mantiene retrocompatibilità con codice esistente.
     * 
     * @param classe il nome della classe del prodotto
     * @return il Food creato, o null se classe non riconosciuta
     */
    public static Food creaProdottoBase(String classe) {
        FoodTypeEnum foodType = FoodTypeEnum.fromClassName(classe);
        return creaProdottoBase(foodType);
    }

    /**
     * Crea un prodotto base dal tipo enum.
     * GRASP Creator: FoodFactory è responsabile della creazione.
     * 
     * @param foodType il tipo di prodotto
     * @return il Food creato, o null se tipo non valido
     */
    public static Food creaProdottoBase(FoodTypeEnum foodType) {
        if (foodType == null) {
            return null;
        }

        // Switch expression su enum - type-safe, no magic strings
        return switch (foodType) {
            case PANINO_DONER_KEBAB -> new PaninoDonerKebab();
            case PIADINA_DONER_KEBAB -> new PiadinaDonerKebab();
            case KEBAB_AL_PIATTO -> new KebabAlPiatto();
        };
    }

    /**
     * Applica un decorator (add-on) al prodotto (da stringa).
     * Mantiene retrocompatibilità con codice esistente.
     * 
     * @param food        il prodotto base da decorare
     * @param addOnClasse la classe del decorator da applicare
     * @return il Food decorato
     */
    public static Food applicaDecorator(Food food, String addOnClasse) {
        if (food == null) {
            return null;
        }

        AddOnTypeEnum addOnType = AddOnTypeEnum.fromClassName(addOnClasse);
        return applicaDecorator(food, addOnType);
    }

    /**
     * Applica un decorator (add-on) al prodotto usando il tipo enum.
     * GRASP Creator: FoodFactory è responsabile della creazione dei Decorator.
     * 
     * @param food      il prodotto base da decorare
     * @param addOnType il tipo di add-on da applicare
     * @return il Food decorato
     */
    public static Food applicaDecorator(Food food, AddOnTypeEnum addOnType) {
        if (food == null) {
            return food;
        }

        if (addOnType == null) {
            return food; // Ritorna food non modificato se add-on non specificato
        }

        // Switch expression su enum - type-safe, no magic strings
        return switch (addOnType) {
            case CIPOLLA -> new Cipolla(food);
            case SALSA_YOGURT -> new SalsaYogurt(food);
            case PATATINE -> new Patatine(food);
            case MIX_VERDURE_GRIGLIATE -> new MixVerdureGrigliate(food);
        };
    }
}
