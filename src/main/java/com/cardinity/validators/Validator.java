package com.cardinity.validators;

public interface Validator<T> {

    void validate(T object);
}
