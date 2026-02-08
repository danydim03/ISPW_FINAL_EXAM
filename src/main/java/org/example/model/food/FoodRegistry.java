package org.example.model.food;

import org.example.model.food.decorator.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Registry per la creazione dinamica delle istanze di Food.
 * 
 * GRASP: Pure Fabrication
 * - Disaccoppia il DAO dalle classi concrete di Food.
 * - Centralizza la logica di creazione "by name" (Factory).
 */
public class FoodRegistry {

    // Mappa che associa il nome della classe al costruttore corrispondente
    private static final Map<String, Supplier<Food>> registry = new HashMap<>();

    static {
        // Registrazione dei Food Base
        registry.put("PaninoDonerKebab", PaninoDonerKebab::new);
        registry.put("PiadinaDonerKebab", PiadinaDonerKebab::new);
        registry.put("KebabAlPiatto", KebabAlPiatto::new);

        // Registrazione dei Decorator (istanziati come componenti base per il DAO)
        registry.put("Cipolla", () -> new Cipolla(null));
        registry.put("SalsaYogurt", () -> new SalsaYogurt(null));
        registry.put("Patatine", () -> new Patatine(null));
        registry.put("MixVerdureGrigliate", () -> new MixVerdureGrigliate(null));
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
