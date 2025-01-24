package com.jmarqb.productsapi.infrastructure.adapters.output.persistence.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.jmarqb.productsapi.domain.model.Category;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.model.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {

	CategoryEntity save(Category category);

	@Query("SELECT c FROM CategoryEntity c WHERE c.deleted = false")
	List<CategoryEntity> searchAll(Pageable pageable);

	@Query("SELECT r FROM CategoryEntity r WHERE r.deleted = false AND lower(r.name) LIKE lower(concat('%', ?1, '%')) " +
		"OR lower(r.description) LIKE lower(concat('%', ?1, '%'))")
	List<CategoryEntity> searchAllByRegex(String regex, Pageable pageable);

	@Query("SELECT c FROM CategoryEntity c JOIN c.products p WHERE c.deleted = false AND p.uid = ?1")
	CategoryEntity searchByProductId(String productId);


	CategoryEntity findByUidAndDeletedFalse(String uid);
}
