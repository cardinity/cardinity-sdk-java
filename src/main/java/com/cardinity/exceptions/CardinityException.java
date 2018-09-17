package com.cardinity.exceptions;

public abstract class CardinityException extends RuntimeException {

    private static final long serialVersionUID = -4551051157520060255L;

    public CardinityException(String message) {
        super(message, null);
    }

}
