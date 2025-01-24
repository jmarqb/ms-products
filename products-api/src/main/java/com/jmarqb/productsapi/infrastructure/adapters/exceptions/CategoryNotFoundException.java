package com.jmarqb.productsapi.infrastructure.adapters.exceptions;

public class CategoryNotFoundException extends RuntimeException {

	public CategoryNotFoundException(String message) {
		super(message);
	}
}
