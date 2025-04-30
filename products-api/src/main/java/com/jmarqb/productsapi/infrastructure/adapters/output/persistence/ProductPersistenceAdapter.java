package com.jmarqb.productsapi.infrastructure.adapters.output.persistence;

import com.jmarqb.productsapi.domain.model.Pagination;
import org.springframework.stereotype.Component;

import java.util.List;

import com.jmarqb.productsapi.domain.model.Product;
import com.jmarqb.productsapi.domain.ports.output.persistence.ProductPersistencePort;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.mapper.ProductPersistenceMapper;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.model.ProductEntity;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;

import static com.jmarqb.productsapi.infrastructure.adapters.output.persistence.common.BuildPageable.buildPageable;

@Component
public class ProductPersistenceAdapter implements ProductPersistencePort {

	private final ProductRepository productRepository;

	private final ProductPersistenceMapper productPersistenceMapper;

	public ProductPersistenceAdapter(ProductRepository productRepository, ProductPersistenceMapper productPersistenceMapper) {
		this.productRepository = productRepository;
		this.productPersistenceMapper = productPersistenceMapper;
	}

	@Transactional
	@Override
	public Product save(Product product) {
		ProductEntity productEntity = this.productRepository.save(productPersistenceMapper.toEntity(product));
		return this.productPersistenceMapper.toDomain(productEntity);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Product> searchAll(Pagination pagination) {
		return this.productPersistenceMapper.toProductList(this.productRepository.searchAll(buildPageable(pagination)));
	}

	@Transactional(readOnly = true)
	@Override
	public List<Product> searchAllByRegex(String regex, Pagination pagination) {
		return this.productPersistenceMapper.toProductList(this.productRepository.searchAllByRegex(regex, buildPageable(pagination)));
	}

	@Transactional(readOnly = true)
	@Override
	public List<Product> searchAllByCategory(String category, Pagination pagination) {
		return this.productPersistenceMapper.toProductList(this.productRepository.searchAllByCategory(category, buildPageable(pagination)));
	}

	@Transactional(readOnly = true)
	@Override
	public Product findByUidAndDeletedFalse(String id) {
		return productPersistenceMapper.toDomain(this.productRepository.findByUidAndDeletedFalse(id));
	}
}
