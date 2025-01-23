package com.jmarqb.productsapi.application.ports.output.persistence;

import com.jmarqb.productsapi.domain.model.Product;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductPersistencePort {

    boolean existsById(String id);

    Product save(Product product);

    List<Product> searchAll(Pageable pageable);

    List<Product> searchAllByRegex(String regex, Pageable pageable);

    List<Product> searchAllByCategory(String category, Pageable pageable);

    Product findByUidAndDeletedFalse(String id);
}
