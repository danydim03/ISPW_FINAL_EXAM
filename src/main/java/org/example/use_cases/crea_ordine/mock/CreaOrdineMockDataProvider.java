package org.example.use_cases.crea_ordine.mock;

import org.example.use_cases.crea_ordine.beans.FoodBean;

import java.util.Arrays;
import java.util.List;

/**
 * Fornisce dati fittizi per lo use-case Crea Ordine quando il DB non è
 * disponibile.
 * Nessuna logica di business: solo liste statiche di bean.
 */
public class CreaOrdineMockDataProvider {

    private static final String ADDON_TYPE = "ADDON";

    public List<FoodBean> getProdottiBase() {
        return Arrays.asList(
                new FoodBean(null, "Panino Doner Kebab", 5.50, 5, "BASE", "PaninoDonerKebab"),
                new FoodBean(null, "Piadina Doner Kebab", 6.00, 6, "BASE", "PiadinaDonerKebab"),
                new FoodBean(null, "Kebab al Piatto", 8.00, 8, "BASE", "KebabAlPiatto"));
    }

    public List<FoodBean> getAddOn() {
        return Arrays.asList(
                new FoodBean(null, "Cipolla", 0.50, 1, ADDON_TYPE, "Cipolla"),
                new FoodBean(null, "Salsa Yogurt", 0.80, 0, ADDON_TYPE, "SalsaYogurt"),
                new FoodBean(null, "Patatine", 2.00, 3, ADDON_TYPE, "Patatine"),
                new FoodBean(null, "Mix Verdure Grigliate", 1.50, 2, ADDON_TYPE, "MixVerdureGrigliate"));
    }
}