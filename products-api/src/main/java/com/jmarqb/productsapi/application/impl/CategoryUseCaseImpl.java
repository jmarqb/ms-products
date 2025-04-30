package com.jmarqb.productsapi.application.impl;

import com.jmarqb.productsapi.application.mapper.UpdateFieldMapper;
import com.jmarqb.productsapi.domain.model.Pagination;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import com.jmarqb.productsapi.application.ports.input.CategoryUseCase;
import com.jmarqb.productsapi.domain.model.Category;
import com.jmarqb.productsapi.domain.ports.output.persistence.CategoryPersistencePort;
import com.jmarqb.productsapi.domain.ports.output.persistence.ProductPersistencePort;
import com.jmarqb.productsapi.application.exceptions.CategoryNotFoundException;
import com.jmarqb.productsapi.application.exceptions.CategoryWithProductsException;
import com.jmarqb.productsapi.application.exceptions.ProductNotFoundException;

@RequiredArgsConstructor
@Component
public class CategoryUseCaseImpl implements CategoryUseCase {

	private final CategoryPersistencePort categoryPersistencePort;

	private final ProductPersistencePort productPersistencePort;

	private final UpdateFieldMapper updateFieldMapper;


	@Override
	public Category save(Category category) {
		category.setUid(UUID.randomUUID().toString());
		return categoryPersistencePort.save(category);
	}

	@Override
	public List<Category> search(String search, int page, int size, String sort) {
		List<Category> categories;

		Pagination pagination = new Pagination(page, size, sort, "uid");

		categories = search != null ? categoryPersistencePort.searchAllByRegex(search, pagination)
			: categoryPersistencePort.searchAll(pagination);

		return categories;
	}

	@Override
	public Category findCategory(String id) {
		return existCategory(id);
	}

	@Override
	public Category findCategoryByProductId(String productId) {
		if (productPersistencePort.findByUidAndDeletedFalse(productId) == null) {
			throw new ProductNotFoundException("Product with %s not found".formatted(productId));
		}

		Category category = categoryPersistencePort.searchByProductId(productId);
		if (category == null) {
			throw new CategoryNotFoundException("No category found for product with %s".formatted(productId));
		}
		return category;
	}

	@Override
	public Category updateCategory(Category dataToUpdateCategory) {
		Category actualCategory = existCategory(dataToUpdateCategory.getUid());
		updateFieldMapper.updateCategory(dataToUpdateCategory, actualCategory);
		return categoryPersistencePort.save(actualCategory);
	}

	@Override
	public void deleteCategory(String id) {
		Category category = existCategory(id);
		isCategoryWithProducts(category);
		category.setDeleted(true);
		category.setDeletedAt(LocalDateTime.now());
		categoryPersistencePort.save(category);
	}

	Category existCategory(String id) {
		Category category = categoryPersistencePort.findByUidAndDeletedFalse(id);
		if (category == null) {
			throw new CategoryNotFoundException("Category with %s not found".formatted(id));
		}
		return category;
	}

	private void isCategoryWithProducts(Category category) {
		if (category.getProducts() != null && !category.getProducts().isEmpty()) {
			throw new CategoryWithProductsException("Category with %s has products".formatted(category.getUid()));
		}
	}
}
