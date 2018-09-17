package com.cardinity.exceptions;

public class ValidationException extends CardinityException {

    private static final long serialVersionUID = -2152842451308691156L;

    public ValidationException(String message) {
        super(message);
    }
}
