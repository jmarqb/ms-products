package com.jmarqb.productsapi.domain.model;

public record Pagination(int page, int size, String sort, String sortBy) {
}
