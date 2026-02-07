package org.example;

import org.example.use_cases.usa_voucher.UsaVoucherController;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for UsaVoucherController.
 * Demonstrates unit testing of the voucher use case.
 * 
 * @author Daniele Pio Di Meo
 */
@DisplayName("UsaVoucherController Unit Tests")
class UsaVoucherControlTest {

    private UsaVoucherController controller;

    @BeforeEach
    void setUp() {
        controller = new UsaVoucherController();
    }

    @Test
    @DisplayName("isVoucherValido should return false with null voucher")
    void testIsVoucherValidoWithNull() {
        // ACT
        boolean result = controller.isVoucherValido(null);

        // ASSERT
        assertFalse(result);
    }

    @Test
    @DisplayName("hasVoucherApplicato should return false with null ordine")
    void testHasVoucherApplicatoWithNullOrdine() {
        // ACT
        boolean result = controller.hasVoucherApplicato(null);

        // ASSERT
        assertFalse(result);
    }

    @Test
    @DisplayName("getVoucherApplicato should return null with null ordine")
    void testGetVoucherApplicatoWithNullOrdine() {
        // ACT
        var result = controller.getVoucherApplicato(null);

        // ASSERT
        assertNull(result);
    }

    @Test
    @DisplayName("rimuoviVoucherDaOrdine should not throw with null ordine")
    void testRimuoviVoucherDaOrdineWithNullOrdine() {
        // ACT & ASSERT
        assertDoesNotThrow(() -> controller.rimuoviVoucherDaOrdine(null));
    }

    @Test
    @DisplayName("UsaVoucherController instance should be created successfully")
    void testUsaVoucherControllerInstantiation() {
        // ACT & ASSERT
        assertNotNull(controller);
    }

    @Test
    @DisplayName("applicaVoucherAOrdine should return null with null codice")
    void testApplicaVoucherAOrdineWithNullCodice() throws Exception {
        // ACT
        var result = controller.applicaVoucherAOrdine(null, null);

        // ASSERT
        assertNull(result);
    }

    @Test
    @DisplayName("applicaVoucherAOrdine should return null with empty codice")
    void testApplicaVoucherAOrdineWithEmptyCodice() throws Exception {
        // ACT
        var result = controller.applicaVoucherAOrdine(null, "");

        // ASSERT
        assertNull(result);
    }
}