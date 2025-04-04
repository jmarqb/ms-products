package com.jmarqb.productsapi.application.exceptions;

public class CategoryNotFoundException extends RuntimeException {

	public CategoryNotFoundException(String message) {
		super(message);
	}
}
