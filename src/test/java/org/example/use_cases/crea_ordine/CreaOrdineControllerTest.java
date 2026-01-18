package org.example.use_cases.crea_ordine;

import org.example.use_cases.crea_ordine.beans.FoodBean;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CreaOrdineController.
 * Tests the order creation business logic.
 * Daniele Pio Di meo
 */
@DisplayName("CreaOrdineController Unit Test")
class CreaOrdineControllerTest {

    @Test
    @DisplayName("FoodBean should correctly add and retrieve addons")
    void testFoodBean_AddOns() {
        // ARRANGE
        FoodBean food = new FoodBean();

        // ACT
        food.aggiungiAddOn("Cipolla");
        food.aggiungiAddOn("SalsaYogurt");

        // ASSERT
        assertEquals(2, food.getAddOnSelezionati().size());
    }
}
