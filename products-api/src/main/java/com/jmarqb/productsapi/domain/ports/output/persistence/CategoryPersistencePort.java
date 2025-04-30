package com.jmarqb.productsapi.domain.ports.output.persistence;

import com.jmarqb.productsapi.domain.model.Pagination;

import java.util.List;

import com.jmarqb.productsapi.domain.model.Category;

public interface CategoryPersistencePort {

	Category save(Category category);

	List<Category> searchAll(Pagination pagination);

	List<Category> searchAllByRegex(String regex, Pagination pagination);

	Category searchByProductId(String productId);

	Category findByUidAndDeletedFalse(String uid);
}
