package org.example.model.food;

import org.example.enums.AddOnTypeEnum;
import org.example.enums.FoodTypeEnum;

/**
 * Factory per la creazione di prodotti Food e applicazione di Decorators.
 * Centralizza la logica di istanziazione per rispettare GRASP (Low Coupling,
 * Creator).
 * 
 * Utilizza FoodTypeEnum e AddOnTypeEnum per evitare magic strings.
 */
public class FoodFactory {

    private FoodFactory() {
        // Utility class - previene istanziazione
    }

    /**
     * Crea un prodotto base dalla classe specificata.
     * 
     * @param classe il nome della classe del prodotto
     * @return il Food creato, o null se classe non riconosciuta
     */
    public static Food creaProdottoBase(String classe) {
        FoodTypeEnum foodType = FoodTypeEnum.fromClassName(classe);
        if (foodType == null) {
            return null;
        }
        return foodType.createInstance();
    }

    /**
     * Crea un prodotto base dal tipo enum.
     * 
     * @param foodType il tipo di prodotto
     * @return il Food creato
     */
    public static Food creaProdottoBase(FoodTypeEnum foodType) {
        if (foodType == null) {
            return null;
        }
        return foodType.createInstance();
    }

    /**
     * Applica un decorator (add-on) al prodotto usando il pattern Decorator.
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
        if (addOnType == null) {
            return food; // Ritorna food non modificato se add-on non trovato
        }
        return addOnType.applyTo(food);
    }

    /**
     * Applica un decorator (add-on) al prodotto usando il tipo enum.
     * 
     * @param food      il prodotto base da decorare
     * @param addOnType il tipo di add-on da applicare
     * @return il Food decorato
     */
    public static Food applicaDecorator(Food food, AddOnTypeEnum addOnType) {
        if (food == null || addOnType == null) {
            return food;
        }
        return addOnType.applyTo(food);
    }
}
