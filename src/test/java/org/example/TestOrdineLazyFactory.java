package org.example;

import org.example.dao_manager.DAOFactoryAbstract;
import org.example.exceptions.DAOException;
import org.example.exceptions.MissingAuthorizationException;
import org.example.exceptions.PropertyException;
import org.example.exceptions.ResourceNotFoundException;
import org.example.model.ordine.Ordine;
import org.example.model.ordine.OrdineLazyFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;

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

    @Test
    void TestCRUD() throws DAOException, MissingAuthorizationException, PropertyException, ResourceNotFoundException {
        OrdineLazyFactory instance = OrdineLazyFactory.getInstance();
        Ordine ordine = instance.newOrdine("1");
        // 1. Prima insert: DEVE avere successo
        DAOFactoryAbstract.getInstance().getOrdineDAO().insert(ordine);

        // 2. Seconda insert: DEVE fallire (assertThrows è fuori dal catch)
        assertThrows(DAOException.class, () -> DAOFactoryAbstract.getInstance().getOrdineDAO().insert(ordine));
    }

    @Test
    void TestType() throws Exception {
        OrdineLazyFactory instance1 = OrdineLazyFactory.getInstance();
        Ordine ordine = instance1.newOrdine("1");

        DAOFactoryAbstract.getInstance().getOrdineDAO().insert(ordine);

        Ordine retrievedOrdine = DAOFactoryAbstract.getInstance().getOrdineDAO()
                .getOrdineByNumero(ordine.getNumeroOrdine());
        assertInstanceOf(Ordine.class, ordine);
        assertNotNull(retrievedOrdine);
        assertEquals(ordine.getNumeroOrdine(), retrievedOrdine.getNumeroOrdine());
    }




}
