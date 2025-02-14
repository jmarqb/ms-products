package com.jmarqb.productsapi.application.service.exceptions;

public class CategoryNotFoundException extends RuntimeException {

	public CategoryNotFoundException(String message) {
		super(message);
	}
}
