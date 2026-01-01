package org.example.exceptions;

public class PropertyException extends HabibiException {

    public PropertyException(String message) {
        super(message);
    }

    public PropertyException(String message, Throwable cause) {
        super(message, cause);
    }
}
