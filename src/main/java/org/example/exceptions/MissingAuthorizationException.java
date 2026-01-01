package org.example.exceptions;

public class MissingAuthorizationException extends HabibiException {
    public MissingAuthorizationException(String message) {
        super(message);
    }

    public MissingAuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
