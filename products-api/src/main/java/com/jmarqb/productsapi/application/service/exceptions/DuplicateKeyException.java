package com.jmarqb.productsapi.application.service.exceptions;

public class DuplicateKeyException extends RuntimeException {
	public DuplicateKeyException(String message) {
		super(message);
	}
}
