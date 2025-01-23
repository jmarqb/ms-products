package com.jmarqb.productsapi.infrastructure.adapters.exceptions;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException(String message) {
        super(message);
    }
}
