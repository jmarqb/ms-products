package com.jmarqb.productsapi.application.impl;

import com.jmarqb.productsapi.application.mapper.UpdateFieldMapper;
import com.jmarqb.productsapi.domain.model.Pagination;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import com.jmarqb.productsapi.application.ports.input.ProductUseCase;
import com.jmarqb.productsapi.domain.model.Product;
import com.jmarqb.productsapi.domain.ports.output.persistence.ProductPersistencePort;
import com.jmarqb.productsapi.application.exceptions.ProductNotFoundException;

@RequiredArgsConstructor
@Component
public class ProductUseCaseImpl implements ProductUseCase {

	private final ProductPersistencePort productPersistencePort;

	private final UpdateFieldMapper updateFieldMapper;


	@Override
	public Product save(Product product) {
		product.setUid(UUID.randomUUID().toString());
		return productPersistencePort.save(product);
	}

	@Override
	public List<Product> search(String search, int page, int size, String sort) {
		List<Product> products;

		Pagination pagination = new Pagination(page, size, sort, "uid");

		products = search != null ? productPersistencePort.searchAllByRegex(search, pagination) :
			productPersistencePort.searchAll(pagination);

		return products;
	}

	@Override
	public List<Product> searchByCategory(String categoryId, int page, int size, String sort) {
		List<Product> products;

		Pagination pagination = new Pagination(page, size, sort, "uid");

		products = productPersistencePort.searchAllByCategory(categoryId, pagination);

		return products;
	}

	@Override
	public Product findProduct(String id) {
		return existProduct(id);
	}

	@Override
	public Product updateProduct(Product dataToUpdateProduct) {
		Product actualProduct = existProduct(dataToUpdateProduct.getUid());
		updateFieldMapper.updateProduct(dataToUpdateProduct, actualProduct);
		return productPersistencePort.save(actualProduct);
	}

	@Override
	public void deleteProduct(String id) {
		Product product = existProduct(id);
		product.setDeleted(true);
		product.setDeletedAt(LocalDateTime.now());
		productPersistencePort.save(product);
	}

	Product existProduct(String id) {
		Product product = productPersistencePort.findByUidAndDeletedFalse(id);
		if (product == null) {
			throw new ProductNotFoundException("Product with %s not found".formatted(id));
		}
		return product;
	}
}
