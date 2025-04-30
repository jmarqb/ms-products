package com.jmarqb.productsapi.infrastructure.adapters.input.rest.advice;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import com.jmarqb.productsapi.application.exceptions.CategoryNotFoundException;
import com.jmarqb.productsapi.application.exceptions.CategoryWithProductsException;
import com.jmarqb.productsapi.application.exceptions.DuplicateKeyException;
import com.jmarqb.productsapi.application.exceptions.ProductNotFoundException;

@Slf4j
@RestControllerAdvice
public class HandlerExceptionController {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Error> handleValidationException(MethodArgumentNotValidException ex) {
        List<Error.FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> Error.FieldError.builder()
                        .field(error.getField())
                        .rejectedValue(error.getRejectedValue() != null ? error.getRejectedValue().toString() : "null")
                        .message(error.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());

        Error response = Error.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Validation failed")
                .fieldErrors(fieldErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler({DuplicateKeyException.class, DataIntegrityViolationException.class})
    public ResponseEntity<Error> handleDuplicateValidationException(Exception ex) {

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Could not execute statement: Duplicate key or Duplicate entry", "Duplicate Key");

    }

    @ExceptionHandler({HttpClientErrorException.Unauthorized.class, AccessDeniedException.class})
    public ResponseEntity<Error> handleUnauthorizedValidationException(Exception ex) {

        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", "Unauthorized");
    }

    @ExceptionHandler({CategoryNotFoundException.class, ProductNotFoundException.class})
    public ResponseEntity<Error> handleValidationException(Exception ex) {

        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), "NOT FOUND");
    }

    @ExceptionHandler({CategoryWithProductsException.class})
    public ResponseEntity<Error> handleIsCategoryWithProductsException(Exception ex) {

        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), "CONFLICT");
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<Error> handleValidationException(HttpMessageNotReadableException ex,
                                                           WebRequest request) {

        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), "Json Error");
    }

    private ResponseEntity<Error> buildErrorResponse(HttpStatus status, String message, String error) {
        Error response = Error.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(error)
                .message(message)
                .build();
        return ResponseEntity.status(status).body(response);
    }
}
