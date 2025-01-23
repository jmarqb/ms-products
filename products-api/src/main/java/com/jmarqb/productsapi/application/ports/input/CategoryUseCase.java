package com.jmarqb.productsapi.application.ports.input;

import com.jmarqb.productsapi.domain.model.Category;

import java.util.List;

public interface CategoryUseCase {

    Category save(Category category);

    List<Category> search(String search, int page, int size, String sort);

    Category findCategory(String id);
    Category findCategoryByProductId(String productId);

    Category updateCategory(Category category);

    void deleteCategory(String id);
}
