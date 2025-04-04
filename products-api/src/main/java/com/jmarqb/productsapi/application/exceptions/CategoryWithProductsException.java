package com.jmarqb.productsapi.application.exceptions;

public class CategoryWithProductsException extends RuntimeException {
	public CategoryWithProductsException(String message) {
		super(message);
	}
}
