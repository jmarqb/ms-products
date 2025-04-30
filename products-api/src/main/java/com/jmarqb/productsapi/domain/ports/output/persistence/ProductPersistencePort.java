package com.jmarqb.productsapi.domain.ports.output.persistence;

import com.jmarqb.productsapi.domain.model.Pagination;

import java.util.List;

import com.jmarqb.productsapi.domain.model.Product;

public interface ProductPersistencePort {

	Product save(Product product);

	List<Product> searchAll(Pagination pagination);

	List<Product> searchAllByRegex(String regex, Pagination pagination);

	List<Product> searchAllByCategory(String category, Pagination pagination);

	Product findByUidAndDeletedFalse(String id);
}
