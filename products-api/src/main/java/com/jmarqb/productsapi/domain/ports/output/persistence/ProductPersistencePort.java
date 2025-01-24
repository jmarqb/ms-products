package com.jmarqb.productsapi.domain.ports.output.persistence;

import org.springframework.data.domain.Pageable;

import java.util.List;

import com.jmarqb.productsapi.domain.model.Product;

public interface ProductPersistencePort {

	Product save(Product product);

	List<Product> searchAll(Pageable pageable);

	List<Product> searchAllByRegex(String regex, Pageable pageable);

	List<Product> searchAllByCategory(String category, Pageable pageable);

	Product findByUidAndDeletedFalse(String id);
}
