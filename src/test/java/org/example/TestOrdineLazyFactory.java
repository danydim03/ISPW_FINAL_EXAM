package org.example;

import org.example.model.ordine.OrdineLazyFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per OrdineLazyFactory.
 */
class TestOrdineLazyFactory {

    @Test
    void testSingletonInstance() {
        OrdineLazyFactory instance1 = OrdineLazyFactory.getInstance();
        OrdineLazyFactory instance2 = OrdineLazyFactory.getInstance();

        assertNotNull(instance1);
        assertSame(instance1, instance2, "getInstance() deve ritornare sempre la stessa istanza");
    }
}
