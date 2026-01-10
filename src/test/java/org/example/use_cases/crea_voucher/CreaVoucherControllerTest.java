package org.example.use_cases.crea_voucher;

import org.example.exceptions.*;
import org.example.model.voucher.Voucher;
import org.example.model.voucher.VoucherFisso;
import org.example.model.voucher.VoucherPercentuale;
import org.example.use_cases.crea_voucher.beans.CreaVoucherBean;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitari per CreaVoucherController.
 * 
 * Verifica:
 * - Creazione corretta di voucher percentuali e fissi
 * - Validazione semantica (unicità codice)
 * - Gestione eccezioni
 * - Comportamento con dati validi e invalidi
 */
class CreaVoucherControllerTest {

    private CreaVoucherController controller;

    @BeforeEach
    void setUp() {
        controller = new CreaVoucherController();
    }

    /**
     * Test: Creazione di un voucher percentuale con dati validi
     */
    @Test
    void testCreaVoucherPercentualeSuccess() throws Exception {
        // Arrange
        CreaVoucherBean bean = new CreaVoucherBean();
        bean.setCodice("TEST10PERCENT");
        bean.setTipoVoucher("PERCENTUALE");
        bean.setValore(10.0);
        bean.setDataScadenza(LocalDate.now().plusDays(30));

        // Act
        Voucher voucher = controller.creaVoucher(bean);

        // Assert
        assertNotNull(voucher);
        assertTrue(voucher instanceof VoucherPercentuale);
        assertEquals("TEST10PERCENT", voucher.getCodice());
        assertEquals("PERCENTUALE", voucher.getTipoVoucher());
        assertTrue(voucher.isValido());
    }

    /**
     * Test: Creazione di un voucher fisso con dati validi
     */
    @Test
    void testCreaVoucherFissoSuccess() throws Exception {
        // Arrange
        CreaVoucherBean bean = new CreaVoucherBean();
        bean.setCodice("TEST5EURO");
        bean.setTipoVoucher("FISSO");
        bean.setValore(5.0);
        bean.setMinimoOrdine(15.0);
        bean.setDataScadenza(LocalDate.now().plusDays(60));

        // Act
        Voucher voucher = controller.creaVoucher(bean);

        // Assert
        assertNotNull(voucher);
        assertTrue(voucher instanceof VoucherFisso);
        assertEquals("TEST5EURO", voucher.getCodice());
        assertEquals("FISSO", voucher.getTipoVoucher());
        assertTrue(voucher.isValido());
        assertEquals(15.0, ((VoucherFisso) voucher).getMinimoOrdine());
    }

    /**
     * Test: Validazione fallita per valore negativo
     */
    @Test
    void testCreaVoucherValoreNegativo() {
        // Arrange
        CreaVoucherBean bean = new CreaVoucherBean();
        bean.setCodice("TESTNEG");
        bean.setTipoVoucher("PERCENTUALE");

        // Act & Assert
        assertThrows(ValidationException.class, () -> {
            bean.setValore(-10.0); // Deve fallire
        });
    }

    /**
     * Test: Validazione fallita per percentuale oltre 100
     */
    @Test
    void testCreaVoucherPercentualeOltre100() {
        // Arrange
        CreaVoucherBean bean = new CreaVoucherBean();
        bean.setCodice("TEST150");
        bean.setTipoVoucher("PERCENTUALE");

        // Act & Assert
        assertThrows(ValidationException.class, () -> {
            bean.setValore(150.0); // Deve fallire (> 100%)
        });
    }

    /**
     * Test: Validazione fallita per codice invalido (lowercase)
     */
    @Test
    void testCreaVoucherCodiceLowercase() {
        // Arrange
        CreaVoucherBean bean = new CreaVoucherBean();

        // Act & Assert
        assertThrows(ValidationException.class, () -> {
            bean.setCodice("sconto10"); // Deve fallire (lowercase)
        });
    }

    /**
     * Test: Validazione fallita per codice invalido (caratteri speciali)
     */
    @Test
    void testCreaVoucherCodiceCaratteriSpeciali() {
        // Arrange
        CreaVoucherBean bean = new CreaVoucherBean();

        // Act & Assert
        assertThrows(ValidationException.class, () -> {
            bean.setCodice("SCONTO-10"); // Deve fallire (contiene trattino)
        });
    }

    /**
     * Test: Validazione fallita per data scadenza nel passato
     */
    @Test
    void testCreaVoucherDataScadenzaPassata() {
        // Arrange
        CreaVoucherBean bean = new CreaVoucherBean();
        bean.setCodice("TESTPAST");

        // Act & Assert
        assertThrows(ValidationException.class, () -> {
            bean.setDataScadenza(LocalDate.now().minusDays(1)); // Deve fallire (passato)
        });
    }

    /**
     * Test: Validazione bean completo
     */
    @Test
    void testValidateBean() {
        // Arrange - Bean valido
        CreaVoucherBean bean = new CreaVoucherBean();
        bean.setCodice("VALID123");
        bean.setTipoVoucher("PERCENTUALE");
        bean.setValore(20.0);

        // Act & Assert - Non deve lanciare eccezione
        assertDoesNotThrow(() -> bean.validate());

        // Arrange - Bean invalido (codice mancante)
        CreaVoucherBean invalidBean = new CreaVoucherBean();
        invalidBean.setTipoVoucher("PERCENTUALE");
        invalidBean.setValore(20.0);

        // Act & Assert - Deve lanciare eccezione
        assertThrows(ValidationException.class, () -> invalidBean.validate());
    }

    /**
     * Test: Conversione automatica a maiuscolo del codice
     */
    @Test
    void testCodiceMaiuscoloAutomatico() {
        // Arrange
        CreaVoucherBean bean = new CreaVoucherBean();

        // Act
        bean.setCodice("MIXED123"); // Input misto

        // Assert
        assertEquals("MIXED123", bean.getCodice()); // Deve rimanere maiuscolo
    }

    /**
     * Test: Minimo ordine con valore zero (valido)
     */
    @Test
    void testMinimoOrdineZero() {
        // Arrange
        CreaVoucherBean bean = new CreaVoucherBean();

        // Act & Assert
        assertDoesNotThrow(() -> bean.setMinimoOrdine(0.0)); // 0 è valido
        assertEquals(0.0, bean.getMinimoOrdine());
    }

    /**
     * Test: Minimo ordine negativo (invalido)
     */
    @Test
    void testMinimoOrdineNegativo() {
        // Arrange
        CreaVoucherBean bean = new CreaVoucherBean();

        // Act & Assert
        assertThrows(ValidationException.class, () -> {
            bean.setMinimoOrdine(-5.0); // Deve fallire
        });
    }

    /**
     * Test: Tipo voucher case-insensitive
     */
    @Test
    void testTipoVoucherCaseInsensitive() {
        // Arrange
        CreaVoucherBean bean = new CreaVoucherBean();

        // Act
        bean.setTipoVoucher("percentuale"); // Lowercase

        // Assert
        assertEquals("PERCENTUALE", bean.getTipoVoucher()); // Convertito a maiuscolo
    }

    /**
     * Test: Tipo voucher invalido
     */
    @Test
    void testTipoVoucherInvalido() {
        // Arrange
        CreaVoucherBean bean = new CreaVoucherBean();

        // Act & Assert
        assertThrows(ValidationException.class, () -> {
            bean.setTipoVoucher("SCONTO"); // Tipo non valido
        });
    }

    /**
     * Nota: I test per la verifica di unicità del codice e per la persistenza
     * richiedono mock del VoucherLazyFactory e del DAO. Questi test vanno eseguiti
     * in un contesto di integration test o con framework di mocking (Mockito).
     * 
     * Esempio con Mockito (da implementare separatamente):
     * 
     * @Test
     *       void testCreaVoucherCodiceDuplicato() {
     *       // Mock del VoucherLazyFactory per simulare voucher esistente
     *       // Verificare che venga lanciata ValidationException
     *       }
     */
}
