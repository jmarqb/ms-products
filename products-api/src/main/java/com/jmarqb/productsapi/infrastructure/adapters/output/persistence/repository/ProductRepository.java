package com.jmarqb.productsapi.infrastructure.adapters.output.persistence.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.jmarqb.productsapi.domain.model.Product;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.model.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {

	ProductEntity save(Product product);

	@Query("SELECT p FROM ProductEntity p WHERE p.deleted = false")
	List<ProductEntity> searchAll(Pageable pageable);

	@Query("SELECT r FROM ProductEntity r WHERE r.deleted = false AND lower(r.name) LIKE lower(concat('%', ?1, '%')) " +
		"OR lower(r.description) LIKE lower(concat('%', ?1, '%'))")
	List<ProductEntity> searchAllByRegex(String regex, Pageable pageable);

	@Query("SELECT r FROM ProductEntity r WHERE r.deleted = false AND r.category.uid = ?1")
	List<ProductEntity> searchAllByCategory(String category, Pageable pageable);

	ProductEntity findByUidAndDeletedFalse(String id);
}
