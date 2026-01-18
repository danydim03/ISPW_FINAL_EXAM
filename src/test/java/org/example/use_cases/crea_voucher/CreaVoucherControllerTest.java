package org.example.use_cases.crea_voucher;

import org.example.model.voucher.Voucher;
import org.example.model.voucher.VoucherPercentuale;
import org.example.use_cases.crea_voucher.beans.CreaVoucherBean;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CreaVoucherController.
 * Tests the voucher creation business logic.
 *   Daniele Pio Di meo
 */
@DisplayName("CreaVoucherController Unit Test")
class CreaVoucherControllerTest {

    private CreaVoucherController controller;

    @BeforeEach
    void setUp() {
        controller = new CreaVoucherController();
    }

    @Test
    @DisplayName("Creating a percentage voucher with valid data should succeed")
    void testCreaVoucherPercentualeSuccess() throws Exception {
        // ARRANGE
        CreaVoucherBean bean = new CreaVoucherBean();
        bean.setCodice("TEST10PERCENT");
        bean.setTipoVoucher("PERCENTUALE");
        bean.setValore(10.0);
        bean.setDataScadenza(LocalDate.now().plusDays(30));

        // ACT
        // qui la variabile voucher è di Tipo Voucher (generico) ma, dopo la chimata al controller vouvher dovrebbe cambiare tipo.
        Voucher voucher = controller.creaVoucher(bean);


        // ASSERT
        // andiamo a vedere se voucher ha cambiato dinamicamente il tipo della sua istanza
        assertTrue(voucher instanceof VoucherPercentuale);
    }
}
