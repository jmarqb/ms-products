package com.jmarqb.productsapi.infrastructure.adapters.exceptions;

public class CategoryWithProductsException extends RuntimeException {
	public CategoryWithProductsException(String message) {
		super(message);
	}
}
