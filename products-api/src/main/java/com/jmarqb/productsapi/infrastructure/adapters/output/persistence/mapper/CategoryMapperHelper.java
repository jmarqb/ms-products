package com.jmarqb.productsapi.infrastructure.adapters.output.persistence.mapper;

import org.springframework.stereotype.Component;

import com.jmarqb.productsapi.infrastructure.adapters.exceptions.CategoryNotFoundException;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.model.CategoryEntity;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.repository.CategoryRepository;

@Component
public class CategoryMapperHelper {

	private final CategoryRepository categoryRepository;

	public CategoryMapperHelper(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public CategoryEntity map(String categoryId) {
		CategoryEntity categoryEntity = categoryRepository.findByUidAndDeletedFalse(categoryId);

		if (categoryEntity == null) {
			throw new CategoryNotFoundException("Category with %s not found".formatted(categoryId));
		}
		return categoryEntity;
	}
}
