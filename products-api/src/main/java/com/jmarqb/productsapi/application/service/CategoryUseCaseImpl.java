package com.jmarqb.productsapi.application.service;

import com.jmarqb.productsapi.infrastructure.adapters.exceptions.CategoryNotFoundException;
import com.jmarqb.productsapi.infrastructure.adapters.exceptions.ProductNotFoundException;
import com.jmarqb.productsapi.application.ports.input.CategoryUseCase;
import com.jmarqb.productsapi.domain.model.Category;
import com.jmarqb.productsapi.application.ports.output.persistence.CategoryPersistencePort;
import com.jmarqb.productsapi.application.ports.output.persistence.ProductPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class CategoryUseCaseImpl implements CategoryUseCase {

    private final CategoryPersistencePort categoryPersistencePort;

    private final ProductPersistencePort productPersistencePort;

    @Override
    public Category save(Category category) {
        category.setUid(UUID.randomUUID().toString());
        return categoryPersistencePort.save(category);
    }

    @Override
    public List<Category> search(String search, int page, int size, String sort) {
        List<Category> categories;

        Pageable pageable = PageRequest.of(page, size, sort.equalsIgnoreCase("asc") ?
                Sort.Direction.ASC : Sort.Direction.DESC, "id");

        categories = (search != null) ? categoryPersistencePort.searchAllByRegex(search, pageable)
                : categoryPersistencePort.searchAll(pageable);

        return categories;
    }

    @Override
    public Category findCategory(String id) {
        return existCategory(id);
    }

    @Override
    public Category findCategoryByProductId(String productId) {
        if (!productPersistencePort.existsById(productId)) {
            throw new ProductNotFoundException(String.format("Product with %s not found", productId));
        }

        Category category = categoryPersistencePort.searchByProductId(productId);
        if (category == null) {
            throw new CategoryNotFoundException(String.format("No category found for product with %s", productId));
        }
        return category;
    }

    @Override
    public Category updateCategory(Category dataToUpdateCategory) {
        Category actualCategory = existCategory(dataToUpdateCategory.getUid());
        updateCategoryFields(actualCategory, dataToUpdateCategory);
        return categoryPersistencePort.save(actualCategory);
    }

    @Override
    public void deleteCategory(String id) {
        Category category = existCategory(id);
        category.setDeleted(true);
        category.setDeletedAt(LocalDateTime.now());
        categoryPersistencePort.save(category);
    }

    private Category existCategory(String id) {
        Category category = categoryPersistencePort.findByUidAndDeletedFalse(id);
        if (category == null) {
            throw new CategoryNotFoundException(String.format("Category with %s not found", id));
        }
        return category;
    }

    private void updateCategoryFields(Category actualCategory, Category dataToUpdateCategory) {
        if (dataToUpdateCategory.getName() != null) actualCategory.setName(dataToUpdateCategory.getName());
        if (dataToUpdateCategory.getDescription() != null) actualCategory.setDescription(dataToUpdateCategory.getDescription());
    }
}
