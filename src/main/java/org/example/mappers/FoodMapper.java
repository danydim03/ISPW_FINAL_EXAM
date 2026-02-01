package org.example.mappers;

import org.example.model.food.Food;
import org.example.use_cases.crea_ordine.beans.FoodBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper per convertire entity Food in FoodBean.
 * 
 * Pattern GRASP applicato: Pure Fabrication
 * - Non rappresenta un concetto di dominio
 * - Creato per aumentare High Cohesion nei Controller
 * - Centralizza la logica di conversione Food ↔ FoodBean
 * 
 * Benefici:
 * - CreaOrdineController non ha più metodi di conversione
 * - Se cambia FoodBean, si modifica solo questo Mapper
 * - Riutilizzabile da altri Controller se necessario
 */
public class FoodMapper {

    private FoodMapper() {
        // Utility class - previene istanziazione
    }

    /**
     * Converte un'entity Food in un FoodBean.
     * 
     * @param food l'entity da convertire
     * @return FoodBean popolato, null se food è null
     */
    public static FoodBean toBean(Food food) {
        if (food == null) {
            return null;
        }

        FoodBean bean = new FoodBean();
        bean.setId(food.getId());
        bean.setDescrizione(food.getDescrizione());
        bean.setCosto(food.getCosto());
        bean.setDurata(food.getDurata());
        bean.setTipo(food.getTipo());
        bean.setClasse(food.getClass().getSimpleName());
        return bean;
    }

    /**
     * Converte una lista di entity Food in lista di FoodBean.
     * 
     * @param foodList la lista di entity da convertire
     * @return lista di FoodBean, lista vuota se input è null
     */
    public static List<FoodBean> toBeanList(List<Food> foodList) {
        if (foodList == null) {
            return new ArrayList<>();
        }

        List<FoodBean> beans = new ArrayList<>();
        for (Food food : foodList) {
            FoodBean bean = toBean(food);
            if (bean != null) {
                beans.add(bean);
            }
        }
        return beans;
    }
}
