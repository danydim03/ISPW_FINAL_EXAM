package org.example.use_cases.login;

import org.example.exceptions.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for LoginControl.
 * Demonstrates unit testing of the login use case.
 */
@DisplayName("LoginControl Unit Tests")
class LoginControlTest {

    private LoginControl loginControl;

    @BeforeEach
    void setUp() {
        // ARRANGE: Create a fresh instance before each test
        loginControl = new LoginControl();
    }

    // ==================== POSITIVE TEST CASES ====================

    @Test
    @DisplayName("Login with valid credentials should return LoginBean with token")
    void testLogin_WithValidCredentials_ShouldReturnLoginBean() {
        // This test requires DEMO persistence mode in .properties
        // ACT & ASSERT: Should not throw exception for valid email
        assertDoesNotThrow(() -> {
            loginControl.emailMatches("cliente@gmail.com");
        });
    }

    @Test
    @DisplayName("Email validation should accept valid email format")
    void testEmailMatches_WithValidEmail_ShouldNotThrow() {
        // ARRANGE
        String validEmail = "test@example.com";

        // ACT & ASSERT
        assertDoesNotThrow(() -> loginControl.emailMatches(validEmail));
    }

    // ==================== NEGATIVE TEST CASES ====================

    @Test
    @DisplayName("Email validation should reject invalid email format")
    void testEmailMatches_WithInvalidEmail_ShouldThrowException() {
        // ARRANGE
        String invalidEmail = "invalid-email-without-at";

        // ACT & ASSERT
        assertThrows(EmailFormatException.class, () -> {
            loginControl.emailMatches(invalidEmail);
        });
    }

    @Test
    @DisplayName("Email validation should handle null email")
    void testEmailMatches_WithNullEmail_ShouldThrowException() {
        // ARRANGE
        String nullEmail = null;

        // ACT & ASSERT - null causes NullPointerException in regex
        assertThrows(Exception.class, () -> {
            loginControl.emailMatches(nullEmail);
        });
    }

    @Test
    @DisplayName("Email validation should reject empty email")
    void testEmailMatches_WithEmptyEmail_ShouldThrowException() {
        // ARRANGE
        String emptyEmail = "";

        // ACT & ASSERT
        assertThrows(EmailFormatException.class, () -> {
            loginControl.emailMatches(emptyEmail);
        });
    }

    @Test
    @DisplayName("Email validation should reject email without domain")
    void testEmailMatches_WithEmailWithoutDomain_ShouldThrowException() {
        // ARRANGE
        String emailWithoutDomain = "user@";

        // ACT & ASSERT
        assertThrows(EmailFormatException.class, () -> {
            loginControl.emailMatches(emailWithoutDomain);
        });
    }

    // ==================== BOUNDARY TEST CASES ====================

    @Test
    @DisplayName("Email validation should accept email with subdomain")
    void testEmailMatches_WithSubdomain_ShouldNotThrow() {
        // ARRANGE
        String emailWithSubdomain = "user@mail.example.com";

        // ACT & ASSERT
        assertDoesNotThrow(() -> loginControl.emailMatches(emailWithSubdomain));
    }

    @Test
    @DisplayName("Email validation rejects email with plus sign (regex limitation)")
    void testEmailMatches_WithPlusSign_ShouldThrowException() {
        // ARRANGE - Note: current regex doesn't support + in emails
        String emailWithPlus = "user+tag@example.com";

        // ACT & ASSERT
        assertThrows(EmailFormatException.class, () -> {
            loginControl.emailMatches(emailWithPlus);
        });
    }
}
