package com.jmarqb.productsapi.application.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import com.jmarqb.productsapi.application.ports.input.ProductUseCase;
import com.jmarqb.productsapi.domain.model.Product;
import com.jmarqb.productsapi.domain.ports.output.persistence.ProductPersistencePort;
import com.jmarqb.productsapi.application.service.exceptions.ProductNotFoundException;

@RequiredArgsConstructor
@Component
public class ProductUseCaseImpl implements ProductUseCase {

	private final ProductPersistencePort productPersistencePort;

	@Override
	public Product save(Product product) {
		product.setUid(UUID.randomUUID().toString());
		return productPersistencePort.save(product);
	}

	@Override
	public List<Product> search(String search, int page, int size, String sort) {
		List<Product> products;

		Pageable pageable = PageRequest.of(page, size, "asc".equalsIgnoreCase(sort) ?
			Sort.Direction.ASC : Sort.Direction.DESC, "id");

		products = search != null ? productPersistencePort.searchAllByRegex(search, pageable) :
			productPersistencePort.searchAll(pageable);

		return products;
	}

	@Override
	public List<Product> searchByCategory(String categoryId, int page, int size, String sort) {
		List<Product> products;

		Pageable pageable = PageRequest.of(page, size, "asc".equalsIgnoreCase(sort) ?
			Sort.Direction.ASC : Sort.Direction.DESC, "id");

		products = productPersistencePort.searchAllByCategory(categoryId, pageable);

		return products;
	}

	@Override
	public Product findProduct(String id) {
		return existProduct(id);
	}

	@Override
	public Product updateProduct(Product dataToUpdateProduct) {
		Product actualProduct = existProduct(dataToUpdateProduct.getUid());
		updateProductFields(actualProduct, dataToUpdateProduct);
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

	private void updateProductFields(Product actualProduct, Product dataToUpdateProduct) {
		if (dataToUpdateProduct.getName() != null) {
			actualProduct.setName(dataToUpdateProduct.getName());
		}
		if (dataToUpdateProduct.getDescription() != null) {
			actualProduct.setDescription(dataToUpdateProduct.getDescription());
		}
		if (dataToUpdateProduct.getPrice() != null) {
			actualProduct.setPrice(dataToUpdateProduct.getPrice());
		}
		if (dataToUpdateProduct.getStock() != null) {
			actualProduct.setStock(dataToUpdateProduct.getStock());
		}
	}
}
