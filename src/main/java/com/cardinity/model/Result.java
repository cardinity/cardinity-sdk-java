package com.cardinity.model;

public class Result<T> {

    private T item;
    private CardinityError cardinityError;

    public Result(T item) {
        this.item = item;
    }

    public Result(CardinityError cardinityError) {
        this.cardinityError = cardinityError;
    }

    public T getItem() {
        return item;
    }

    public CardinityError getCardinityError() {
        return cardinityError;
    }

    public boolean isValid() {
        return item != null;
    }
}
