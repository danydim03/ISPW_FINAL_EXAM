package org.example.model.food;

import java.util.HashMap;
import java.util.Map;

import java.util.function.Supplier;

/**
 * Registry per la creazione dinamica delle istanze di Food BASE.
 * 
 * GRASP: Pure Fabrication
 * - Disaccoppia il DAO dalle classi concrete di Food.
 * - Centralizza la logica di creazione "by name" (Factory).
 * 
 * NOTA: I Decorator (add-on) NON sono registrati qui.
 * - Per i METADATI degli add-on: usare AddOnDescriptor (Value Object)
 * - Per DECORARE un prodotto: usare FoodFactory.applicaDecorator()
 * 
 * Questo garantisce che i Decorator siano usati SOLO come veri decoratori
 * (GoF compliance), mai come contenitori standalone di metadati.
 */
public class FoodRegistry {

    // Mappa che associa il nome della classe al costruttore corrispondente
    private static final Map<String, Supplier<Food>> registry = new HashMap<>();

    static {
        // Registrazione SOLO dei Food Base
        registry.put("PaninoDonerKebab", PaninoDonerKebab::new);
        registry.put("PiadinaDonerKebab", PiadinaDonerKebab::new);
        registry.put("KebabAlPiatto", KebabAlPiatto::new);

        // I Decorator NON sono più registrati qui.
        // Per i metadati: DAO.getAllAddOnDescriptors() -> AddOnDescriptor
        // Per decorare: FoodFactory.applicaDecorator(food, addOnType)
    }

    private FoodRegistry() {
        // Impedisce l'istanziazione
    }

    /**
     * Crea un'istanza di Food basata sul nome della classe.
     * Restituisce un NullFood se la classe non è registrata.
     */
    public static Food create(String className, Long id) {
        Supplier<Food> supplier = registry.getOrDefault(className, NullFood::new);
        Food food = supplier.get();
        food.setId(id);
        return food;
    }
}
