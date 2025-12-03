package org.example.exceptions;

public class BeanFormatException extends Exception{
    public BeanFormatException(String message) {
        super(message);
    }

    public BeanFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
