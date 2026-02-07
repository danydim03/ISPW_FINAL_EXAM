package org.example;

import org.example.exceptions.EmailFormatException;
import org.example.use_cases.login.LoginControl;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for LoginControl.
 * Demonstrates unit testing of the login use case.
 * 
 * @author Daniele Pio Di Meo
 */
@DisplayName("LoginControl Unit Tests")
class LoginControlTest {

    private LoginControl loginControl;

    @BeforeEach
    void setUp() {
        loginControl = new LoginControl();
    }

    @Test
    @DisplayName("emailMatches should not throw with valid email")
    void testEmailMatches_WithValidEmail_ShouldNotThrow() {
        // ARRANGE
        String validEmail = "test@example.com";

        // ACT & ASSERT
        assertDoesNotThrow(() -> loginControl.emailMatches(validEmail));
    }

    @Test
    @DisplayName("emailMatches should not throw with complex valid email")
    void testEmailMatches_WithComplexValidEmail_ShouldNotThrow() {
        // ARRANGE
        String validEmail = "mario.rossi_123@subdomain.example.it";

        // ACT & ASSERT
        assertDoesNotThrow(() -> loginControl.emailMatches(validEmail));
    }

    @Test
    @DisplayName("emailMatches should throw EmailFormatException with invalid email")
    void testEmailMatches_WithInvalidEmail_ShouldThrow() {
        // ARRANGE
        String invalidEmail = "email-senza-chiocciola.com";

        // ACT & ASSERT
        assertThrows(EmailFormatException.class, () -> loginControl.emailMatches(invalidEmail));
    }

    @Test
    @DisplayName("emailMatches should throw EmailFormatException with empty email")
    void testEmailMatches_WithEmptyEmail_ShouldThrow() {
        // ARRANGE
        String emptyEmail = "";

        // ACT & ASSERT
        assertThrows(EmailFormatException.class, () -> loginControl.emailMatches(emptyEmail));
    }

    @Test
    @DisplayName("emailMatches should throw EmailFormatException with spaces only")
    void testEmailMatches_WithSpacesOnly_ShouldThrow() {
        // ARRANGE
        String spacesEmail = "   ";

        // ACT & ASSERT
        assertThrows(EmailFormatException.class, () -> loginControl.emailMatches(spacesEmail));
    }

    @Test
    @DisplayName("LoginControl instance should be created successfully")
    void testLoginControlInstantiation() {
        // ACT & ASSERT
        assertNotNull(loginControl);
    }
}
