package org.example;

import org.example.use_cases.crea_ordine.CreaOrdineController;
import org.example.use_cases.crea_ordine.beans.FoodBean;
import org.example.use_cases.crea_ordine.beans.OrdineBean;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CreaOrdineController.
 * Tests the order creation business logic.
 * 
 * @author Daniele Pio Di Meo
 */
@DisplayName("CreaOrdineController Unit Tests")
class CreaOrdineControllerTest {

    private CreaOrdineController controller;

    @BeforeEach
    void setUp() {
        controller = new CreaOrdineController();
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
        assertEquals(2, food.getAddOnSelezionati().size());
    }

    @Test
    @DisplayName("FoodBean addons list should not be null")
    void testFoodBean_AddOnsNotNull() {
        // ARRANGE
        FoodBean food = new FoodBean();

        // ACT
        List<String> addOns = food.getAddOnSelezionati();

        // ASSERT
        assertNotNull(addOns);
    }

    @Test
    @DisplayName("FoodBean should start with empty addons list")
    void testFoodBean_AddOnsEmpty() {
        // ARRANGE
        FoodBean food = new FoodBean();

        // ASSERT
        assertTrue(food.getAddOnSelezionati().isEmpty());
    }

    @Test
    @DisplayName("CreaOrdineController instance should be created successfully")
    void testCreaOrdineControllerInstantiation() {
        // ACT & ASSERT
        assertNotNull(controller);
    }

    @Test
    @DisplayName("creaNuovoOrdine should return OrdineBean with valid clienteId")
    void testCreaNuovoOrdine() throws Exception {
        // ARRANGE
        String clienteId = "RSSMRA85M01H501Z";

        // ACT
        OrdineBean ordineBean = controller.creaNuovoOrdine(clienteId);

        // ASSERT
        assertNotNull(ordineBean);
        assertNotNull(ordineBean.getNumeroOrdine());
        assertTrue(ordineBean.getNumeroOrdine() > 0);
    }

    @Test
    @DisplayName("getProdottiBaseDisponibili should return non-null list")
    void testGetProdottiBaseDisponibili() throws Exception {
        // ACT
        List<FoodBean> prodotti = controller.getProdottiBaseDisponibili();

        // ASSERT
        assertNotNull(prodotti);
    }


    @Test
    @DisplayName("annullaOrdine should not throw with valid ordineId")
    void testAnnullaOrdine() throws Exception {
        // ARRANGE
        OrdineBean ordineBean = controller.creaNuovoOrdine("RSSMRA85M01H501Z");
        String ordineId = String.valueOf(ordineBean.getNumeroOrdine());

        // ACT & ASSERT
        assertDoesNotThrow(() -> controller.annullaOrdine(ordineId));
    }

    @Test
    @DisplayName("rimuoviVoucher should not throw with valid ordineId")
    void testRimuoviVoucher() throws Exception {
        // ARRANGE
        OrdineBean ordineBean = controller.creaNuovoOrdine("RSSMRA85M01H501Z");
        String ordineId = String.valueOf(ordineBean.getNumeroOrdine());

        // ACT & ASSERT
        assertDoesNotThrow(() -> controller.rimuoviVoucher(ordineId));
    }
}
