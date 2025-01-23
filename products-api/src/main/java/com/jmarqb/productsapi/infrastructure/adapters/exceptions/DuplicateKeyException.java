package com.jmarqb.productsapi.infrastructure.adapters.exceptions;

public class DuplicateKeyException extends RuntimeException{
    public DuplicateKeyException(String message) {
        super(message);
    }
}
