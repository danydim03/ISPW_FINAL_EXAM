package org.example.exceptions;

public class VoucherNotValidException extends HabibiException {
    public VoucherNotValidException(String message) {
        super(message);
    }

    public VoucherNotValidException(String message, Throwable cause) {
        super(message, cause);
    }
}
