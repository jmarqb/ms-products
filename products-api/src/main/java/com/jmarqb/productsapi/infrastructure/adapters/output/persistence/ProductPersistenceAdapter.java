package com.jmarqb.productsapi.infrastructure.adapters.output.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

import com.jmarqb.productsapi.domain.model.Product;
import com.jmarqb.productsapi.domain.ports.output.persistence.ProductPersistencePort;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.mapper.ProductPersistenceMapper;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.model.ProductEntity;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.repository.ProductRepository;

@Component
public class ProductPersistenceAdapter implements ProductPersistencePort {

	private final ProductRepository productRepository;

	private final ProductPersistenceMapper productPersistenceMapper;

	public ProductPersistenceAdapter(ProductRepository productRepository, ProductPersistenceMapper productPersistenceMapper) {
		this.productRepository = productRepository;
		this.productPersistenceMapper = productPersistenceMapper;
	}

	@Override
	public Product save(Product product) {
		ProductEntity productEntity = this.productRepository.save(productPersistenceMapper.toEntity(product));
		return this.productPersistenceMapper.toDomain(productEntity);
	}

	@Override
	public List<Product> searchAll(Pageable pageable) {
		return this.productPersistenceMapper.toProductList(this.productRepository.searchAll(pageable));
	}

	@Override
	public List<Product> searchAllByRegex(String regex, Pageable pageable) {
		return this.productPersistenceMapper.toProductList(this.productRepository.searchAllByRegex(regex, pageable));
	}

	@Override
	public List<Product> searchAllByCategory(String category, Pageable pageable) {
		return this.productPersistenceMapper.toProductList(this.productRepository.searchAllByCategory(category, pageable));
	}

	@Override
	public Product findByUidAndDeletedFalse(String id) {
		return productPersistenceMapper.toDomain(this.productRepository.findByUidAndDeletedFalse(id));
	}
}
