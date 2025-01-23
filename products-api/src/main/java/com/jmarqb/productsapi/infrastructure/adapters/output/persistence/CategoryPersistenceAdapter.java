package com.jmarqb.productsapi.infrastructure.adapters.output.persistence;

import com.jmarqb.productsapi.application.ports.output.persistence.CategoryPersistencePort;
import com.jmarqb.productsapi.domain.model.Category;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.mapper.CategoryPersistenceMapper;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.model.CategoryEntity;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.repository.CategoryRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryPersistenceAdapter implements CategoryPersistencePort {

    private final CategoryRepository categoryRepository;
    private final CategoryPersistenceMapper categoryPersistenceMapper;

    public CategoryPersistenceAdapter(CategoryRepository categoryRepository, CategoryPersistenceMapper categoryPersistenceMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryPersistenceMapper = categoryPersistenceMapper;
    }

    @Override
    public boolean existsById(String uid) {
        return this.categoryRepository.existsById(uid);
    }

    @Override
    public Category save(Category category) {
        CategoryEntity categoryEntity = this.categoryRepository.save(this.categoryPersistenceMapper.toEntity(category));
        return this.categoryPersistenceMapper.toDomain(categoryEntity);
    }

    @Override
    public List<Category> searchAll(Pageable pageable) {
        return this.categoryPersistenceMapper.toCategoryList(this.categoryRepository.searchAll(pageable));
    }

    @Override
    public List<Category> searchAllByRegex(String regex, Pageable pageable) {
        return this.categoryPersistenceMapper.toCategoryList(this.categoryRepository.searchAllByRegex(regex, pageable));
    }

    @Override
    public Category searchByProductId(String productId) {
        return this.categoryPersistenceMapper.toDomain(this.categoryRepository.searchByProductId(productId));
    }

    @Override
    public Category findByUidAndDeletedFalse(String uid) {
        return this.categoryPersistenceMapper.toDomain(this.categoryRepository.findByUidAndDeletedFalse(uid));
    }
}
