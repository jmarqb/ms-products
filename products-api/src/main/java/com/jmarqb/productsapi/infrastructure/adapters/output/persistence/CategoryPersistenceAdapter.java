package com.jmarqb.productsapi.infrastructure.adapters.output.persistence;

import com.jmarqb.productsapi.domain.model.Pagination;
import org.springframework.stereotype.Component;

import java.util.List;

import com.jmarqb.productsapi.domain.model.Category;
import com.jmarqb.productsapi.domain.ports.output.persistence.CategoryPersistencePort;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.mapper.CategoryPersistenceMapper;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.model.CategoryEntity;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.repository.CategoryRepository;
import org.springframework.transaction.annotation.Transactional;

import static com.jmarqb.productsapi.infrastructure.adapters.output.persistence.common.BuildPageable.buildPageable;

@Component
public class CategoryPersistenceAdapter implements CategoryPersistencePort {

	private final CategoryRepository categoryRepository;
	private final CategoryPersistenceMapper categoryPersistenceMapper;

	public CategoryPersistenceAdapter(CategoryRepository categoryRepository, CategoryPersistenceMapper categoryPersistenceMapper) {
		this.categoryRepository = categoryRepository;
		this.categoryPersistenceMapper = categoryPersistenceMapper;
	}

	@Transactional
	@Override
	public Category save(Category category) {
		CategoryEntity categoryEntity = this.categoryPersistenceMapper.toEntity(category);
		CategoryEntity returnedCategoryEntity = this.categoryRepository.save(categoryEntity);
		return this.categoryPersistenceMapper.toDomain(returnedCategoryEntity);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Category> searchAll(Pagination pagination) {
		return this.categoryPersistenceMapper.toCategoryList(this.categoryRepository.searchAll(buildPageable(pagination)));
	}

	@Transactional(readOnly = true)
	@Override
	public List<Category> searchAllByRegex(String regex, Pagination pagination) {
		return this.categoryPersistenceMapper.toCategoryList(this.categoryRepository.searchAllByRegex(regex, buildPageable(pagination)));
	}

	@Transactional(readOnly = true)
	@Override
	public Category searchByProductId(String productId) {
		return this.categoryPersistenceMapper.toDomain(this.categoryRepository.searchByProductId(productId));
	}

	@Transactional(readOnly = true)
	@Override
	public Category findByUidAndDeletedFalse(String uid) {
		return this.categoryPersistenceMapper.toDomain(this.categoryRepository.findByUidAndDeletedFalse(uid));
	}
}
