package com.jmarqb.productsapi.domain.ports.output.persistence;

import org.springframework.data.domain.Pageable;

import java.util.List;

import com.jmarqb.productsapi.domain.model.Category;

public interface CategoryPersistencePort {

	Category save(Category category);

	List<Category> searchAll(Pageable pageable);

	List<Category> searchAllByRegex(String regex, Pageable pageable);

	Category searchByProductId(String productId);

	Category findByUidAndDeletedFalse(String uid);
}
