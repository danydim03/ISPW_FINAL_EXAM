package org.example.use_cases.crea_ordine;

import org.example.use_cases.crea_ordine.beans.FoodBean;
import org.example.use_cases.crea_ordine.beans.RiepilogoOrdineBean;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CreaOrdineController.
 * Tests the order creation business logic.
 */
@DisplayName("CreaOrdineController Unit Tests")
class CreaOrdineControllerTest {

    // No @BeforeEach needed for bean-only tests

    // ==================== FOOD BEAN TESTS ====================

    @Test
    @DisplayName("FoodBean should correctly store and retrieve data")
    void testFoodBean_GettersAndSetters() {
        // ARRANGE
        FoodBean food = new FoodBean();

        // ACT
        food.setDescrizione("Panino Doner Kebab");
        food.setCosto(5.50);
        food.setDurata(5);
        food.setTipo("BASE");
        food.setClasse("PaninoDonerKebab");

        // ASSERT
        assertEquals("Panino Doner Kebab", food.getDescrizione());
        assertEquals(5.50, food.getCosto(), 0.01);
        assertEquals(5, food.getDurata());
        assertEquals("BASE", food.getTipo());
        assertEquals("PaninoDonerKebab", food.getClasse());
    }

    @Test
    @DisplayName("FoodBean should correctly add and retrieve addons")
    void testFoodBean_AddOns() {
        // ARRANGE
        FoodBean food = new FoodBean();

        // ACT
        food.aggiungiAddOn("Cipolla");
        food.aggiungiAddOn("SalsaYogurt");

        // ASSERT
        assertNotNull(food.getAddOnSelezionati());
        assertEquals(2, food.getAddOnSelezionati().size());
        assertTrue(food.getAddOnSelezionati().contains("Cipolla"));
        assertTrue(food.getAddOnSelezionati().contains("SalsaYogurt"));
    }

    // ==================== RIEPILOGO ORDINE BEAN TESTS ====================

    @Test
    @DisplayName("RiepilogoOrdineBean should correctly format totale")
    void testRiepilogoOrdineBean_TotaleFormattato() {
        // ARRANGE
        RiepilogoOrdineBean riepilogo = new RiepilogoOrdineBean();

        // ACT
        riepilogo.setTotale(15.50);

        // ASSERT - Italian locale uses comma as decimal separator
        assertEquals("€15,50", riepilogo.getTotaleFormattato());
    }

    @Test
    @DisplayName("RiepilogoOrdineBean should correctly format durata")
    void testRiepilogoOrdineBean_DurataFormattata() {
        // ARRANGE
        RiepilogoOrdineBean riepilogo = new RiepilogoOrdineBean();

        // ACT
        riepilogo.setDurataTotale(25);

        // ASSERT
        assertEquals("25 min", riepilogo.getDurataFormattata());
    }

    @Test
    @DisplayName("RiepilogoOrdineBean should correctly format sconto")
    void testRiepilogoOrdineBean_ScontoFormattato() {
        // ARRANGE
        RiepilogoOrdineBean riepilogo = new RiepilogoOrdineBean();

        // ACT
        riepilogo.setSconto(3.00);

        // ASSERT - Italian locale uses comma
        assertEquals("-€3,00", riepilogo.getScontoFormattato());
    }

    @Test
    @DisplayName("RiepilogoOrdineBean should start with empty righe ordine")
    void testRiepilogoOrdineBean_InitiallyEmpty() {
        // ARRANGE & ACT
        RiepilogoOrdineBean riepilogo = new RiepilogoOrdineBean();

        // ASSERT
        assertNotNull(riepilogo.getRigheOrdine());
        assertTrue(riepilogo.getRigheOrdine().isEmpty());
    }

    // ==================== RIGA ORDINE BEAN TESTS ====================

    @Test
    @DisplayName("RigaOrdineBean should correctly format prezzo")
    void testRigaOrdineBean_PrezzoFormattato() {
        // ARRANGE
        RiepilogoOrdineBean.RigaOrdineBean riga = new RiepilogoOrdineBean.RigaOrdineBean("Panino Kebab", 5.50, 5);

        // ASSERT - Italian locale uses comma
        assertEquals("€5,50", riga.getPrezzoFormattato());
        assertEquals("Panino Kebab", riga.getDescrizione());
        assertEquals(5, riga.getDurata());
    }
}
