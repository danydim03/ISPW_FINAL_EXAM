package org.example;

import java.util.Scanner;

/**
 * Base class for all CLI graphic controllers.
 * Provides common utilities for console input/output and session management.
 */
public abstract class BaseCLIGraphicController {

    protected static final Scanner scanner = new Scanner(System.in);
    protected String tokenKey;

    protected static final String SEPARATOR = "═══════════════════════════════════════════════════════════════";
    protected static final String THIN_SEPARATOR = "───────────────────────────────────────────────────────────────";

    public BaseCLIGraphicController() {
    }

    public BaseCLIGraphicController(String tokenKey) {
        this.tokenKey = tokenKey;
    }

    /**
     * Main method to start the controller's view.
     * Each concrete controller must implement this.
     */
    public abstract void start();

    /**
     * Prints a styled header for the view
     */
    protected void printHeader(String title) {
        clearScreen();
        System.out.println("\n" + SEPARATOR);
        System.out.println("  🥙 HABIBI SHAWARMA - " + title);
        System.out.println(SEPARATOR + "\n");
    }

    /**
     * Prints a menu option in a formatted way
     */
    protected void printMenuOption(int number, String description) {
        System.out.printf("  [%d] %s%n", number, description);
    }

    /**
     * Prints a menu option with an emoji
     */
    protected void printMenuOption(int number, String emoji, String description) {
        System.out.printf("  [%d] %s %s%n", number, emoji, description);
    }

    /**
     * Reads an integer choice from the user
     */
    protected int readChoice() {
        System.out.print("\n  👉 Scelta: ");
        try {
            String input = scanner.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Reads a string input from the user
     */
    protected String readInput(String prompt) {
        System.out.print("  " + prompt + ": ");
        return scanner.nextLine().trim();
    }

    /**
     * Reads a password (hides input if possible, otherwise just reads)
     */
    protected String readPassword(String prompt) {
        System.out.print("  " + prompt + ": ");
        // In a real scenario, we'd use Console.readPassword(), but it doesn't work in
        // IDEs
        return scanner.nextLine().trim();
    }

    /**
     * Displays an error message
     */
    protected void showError(String message) {
        System.out.println("\n  ❌ ERRORE: " + message);
    }

    /**
     * Displays a success message
     */
    protected void showSuccess(String message) {
        System.out.println("\n  ✅ " + message);
    }

    /**
     * Displays an info message
     */
    protected void showInfo(String message) {
        System.out.println("\n  ℹ️  " + message);
    }

    /**
     * Displays a warning message
     */
    protected void showWarning(String message) {
        System.out.println("\n  ⚠️  " + message);
    }

    /**
     * Waits for user to press Enter
     */
    protected void waitForEnter() {
        System.out.print("\n  Premi INVIO per continuare...");
        scanner.nextLine();
    }

    /**
     * Clears the screen (works on most terminals)
     */
    protected void clearScreen() {
        // ANSI escape code to clear screen
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Prints a formatted table row
     */
    protected void printTableRow(String... columns) {
        StringBuilder row = new StringBuilder("  │");
        for (String col : columns) {
            row.append(String.format(" %-15s│", truncate(col, 15)));
        }
        System.out.println(row);
    }

    /**
     * Truncates a string to max length with ellipsis
     */
    protected String truncate(String text, int maxLength) {
        if (text == null)
            return "";
        if (text.length() <= maxLength)
            return text;
        return text.substring(0, maxLength - 3) + "...";
    }

    /**
     * Formats a price for display
     */
    protected String formatPrice(double price) {
        return String.format("€%.2f", price);
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }
}