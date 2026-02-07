package org.example;

import org.example.use_cases.usa_voucher.UsaVoucherController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UsaVoucherControlTest {

    @Test
    void testIsVoucherValidoWithNull() {
        UsaVoucherController controller = new UsaVoucherController();

        boolean result = controller.isVoucherValido(null);

        assertFalse(result);
    }
}