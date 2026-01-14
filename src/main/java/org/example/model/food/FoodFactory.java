package org.example.model.food;

import org.example.model.food.decorator.*;

/**
 * Factory per la creazione di prodotti Food e applicazione di Decorators.
 * Centralizza la logica di istanziazione per rispettare GRASP (Low Coupling,
 * Creator).
 */
public class FoodFactory {

    private FoodFactory() {
        // Utility class
    }

    /**
     * Crea un prodotto base dalla classe specificata.
     * 
     * @param classe il nome della classe del prodotto
     * @return il Food creato, o null se classe non riconosciuta
     */
    public static Food creaProdottoBase(String classe) {
        if (classe == null) {
            return null;
        }

        switch (classe) {
            case "PaninoDonerKebab":
                return new PaninoDonerKebab();
            case "PiadinaDonerKebab":
                return new PiadinaDonerKebab();
            case "KebabAlPiatto":
                return new KebabAlPiatto();
            default:
                return null;
        }
    }

    /**
     * Applica un decorator (add-on) al prodotto usando il pattern Decorator.
     * 
     * @param food        il prodotto base da decorare
     * @param addOnClasse la classe del decorator da applicare
     * @return il Food decorato
     */
    public static Food applicaDecorator(Food food, String addOnClasse) {
        if (addOnClasse == null || food == null) {
            return food;
        }

        switch (addOnClasse) {
            case "Cipolla":
                return new Cipolla(food);
            case "SalsaYogurt":
                return new SalsaYogurt(food);
            case "Patatine":
                return new Patatine(food);
            case "MixVerdureGrigliate":
                return new MixVerdureGrigliate(food);
            default:
                return food;
        }
    }
}
