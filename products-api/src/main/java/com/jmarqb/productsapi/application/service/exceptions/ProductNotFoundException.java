package com.jmarqb.productsapi.application.service.exceptions;

public class ProductNotFoundException extends RuntimeException {

	public ProductNotFoundException(String message) {
		super(message);
	}
}
