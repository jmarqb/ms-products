package com.jmarqb.productsapi.application.ports.input;

import com.jmarqb.productsapi.domain.model.Product;

import java.util.List;

public interface ProductUseCase {

    Product save(Product product);

    List<Product> search(String search, int page, int size, String sort);

    List<Product> searchByCategory(String categoryId,int page, int size, String sort);

    Product findProduct(String id);

    Product updateProduct(Product product);

    void deleteProduct(String id);
}
