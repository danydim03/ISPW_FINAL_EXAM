package org.example.use_cases.login;

import org.example.exceptions.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for LoginControl.
 * Demonstrates unit testing of the login use case.
 * Daniele Pio Di meo
 */
@DisplayName("LoginControl Unit Test")
class LoginControlTest {

    private LoginControl loginControl;

    @BeforeEach
    void setUp() {
        loginControl = new LoginControl();
    }

    @Test
    @DisplayName("Email validation should accept valid email format")
    void testEmailMatches_WithValidEmail_ShouldNotThrow() {
        // ARRANGE
        String validEmail = "test@example.com";

        // ACT & ASSERT
        assertDoesNotThrow(() -> loginControl.emailMatches(validEmail));
    }
}
