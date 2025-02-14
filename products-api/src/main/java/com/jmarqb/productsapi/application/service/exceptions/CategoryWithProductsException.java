package com.jmarqb.productsapi.application.service.exceptions;

public class CategoryWithProductsException extends RuntimeException {
	public CategoryWithProductsException(String message) {
		super(message);
	}
}
