package com.jmarqb.productsapi.application.ports.output.persistence;

import com.jmarqb.productsapi.domain.model.Category;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CategoryPersistencePort {

    boolean existsById(String uid);

    Category save(Category category);

    List<Category> searchAll(Pageable pageable);

    List<Category> searchAllByRegex(String regex, Pageable pageable);

    Category searchByProductId(String productId);

    Category findByUidAndDeletedFalse(String uid);
}
