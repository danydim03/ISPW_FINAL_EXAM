package org.example.mappers;

import org.example.model.food.AddOnDescriptor;
import org.example.model.food.Food;
import org.example.use_cases.crea_ordine.beans.FoodBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper per convertire entity Food e AddOnDescriptor in FoodBean.
 * 
 * Pattern GRASP applicato: Pure Fabrication
 * - Non rappresenta un concetto di dominio
 * - Creato per aumentare High Cohesion nei Controller
 * - Centralizza la logica di conversione Food/AddOnDescriptor ↔ FoodBean
 * 
 * Benefici:
 * - CreaOrdineController non ha più metodi di conversione
 * - Se cambia FoodBean, si modifica solo questo Mapper
 * - Riutilizzabile da altri Controller se necessario
 * 
 * GoF Compliance:
 * - AddOnDescriptor (Value Object) contiene solo metadati degli add-on
 * - I Decorator vengono istanziati solo tramite FoodFactory.applicaDecorator()
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
        bean.setDescrizione(food.getDescrizione());
        bean.setCosto(food.getCosto());
        bean.setDurata(food.getDurata());
        bean.setTipo(food.getTipo());
        bean.setClasse(food.getClass().getSimpleName());
        return bean;
    }

    /**
     * Converte un AddOnDescriptor (Value Object) in un FoodBean.
     * 
     * <p>
     * <b>GoF Compliance:</b> Questo metodo converte i metadati degli add-on
     * (letti dal DB come Value Objects) in bean per la GUI, senza mai
     * istanziare un Decorator.
     * </p>
     * 
     * @param descriptor il descriptor da convertire
     * @return FoodBean popolato, null se descriptor è null
     */
    public static FoodBean toBean(AddOnDescriptor descriptor) {
        if (descriptor == null) {
            return null;
        }

        FoodBean bean = new FoodBean();
        bean.setDescrizione(descriptor.getNome());
        bean.setCosto(descriptor.getCosto());
        bean.setDurata(descriptor.getDurata());
        bean.setTipo("ADDON");
        bean.setClasse(descriptor.getClassName());
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

    /**
     * Converte una lista di AddOnDescriptor in lista di FoodBean.
     * 
     * <p>
     * <b>GoF Compliance:</b> Converte metadati degli add-on in bean senza
     * istanziare Decorator.
     * </p>
     * 
     * @param descriptors la lista di descriptor da convertire
     * @return lista di FoodBean, lista vuota se input è null
     */
    public static List<FoodBean> descriptorsToBeanList(List<AddOnDescriptor> descriptors) {
        if (descriptors == null) {
            return new ArrayList<>();
        }

        List<FoodBean> beans = new ArrayList<>();
        for (AddOnDescriptor descriptor : descriptors) {
            FoodBean bean = toBean(descriptor);
            if (bean != null) {
                beans.add(bean);
            }
        }
        return beans;
    }
}
