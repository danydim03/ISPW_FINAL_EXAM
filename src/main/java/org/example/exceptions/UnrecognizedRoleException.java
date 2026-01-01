package org.example.exceptions;

public class UnrecognizedRoleException extends HabibiException {

    public UnrecognizedRoleException(String message) {
        super(message);
    }

    public UnrecognizedRoleException(String message, Throwable cause) {
        super(message, cause);
    }
}
