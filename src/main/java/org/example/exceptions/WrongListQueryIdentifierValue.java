package org.example.exceptions;

public class WrongListQueryIdentifierValue extends HabibiException {
    public WrongListQueryIdentifierValue(String message) {
        super(message);
    }

    public WrongListQueryIdentifierValue(String message, Throwable cause) {
        super(message, cause);
    }
}
