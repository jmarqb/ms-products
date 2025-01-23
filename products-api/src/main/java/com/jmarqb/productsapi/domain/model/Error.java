package com.jmarqb.productsapi.domain.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
public class Error {

    private LocalDateTime timestamp;
    private int status;

    private String error;


    private String message;


    private List<FieldError> fieldErrors;



    @Builder
    @Getter
    @Setter
    public static class FieldError {
        private String field;

        private String rejectedValue;
        private String message;
    }
}
