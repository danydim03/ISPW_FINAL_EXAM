package org.example.exceptions;

public class EmailFormatException extends HabibiException {

    public EmailFormatException(String message) {
        super(message);
    }

    public EmailFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
