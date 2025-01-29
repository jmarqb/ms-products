package com.jmarqb.productsapi.infrastructure.adapters.output.persistence;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.jmarqb.productsapi.domain.model.Product;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.mapper.ProductPersistenceMapper;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.model.CategoryEntity;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.model.ProductEntity;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.repository.ProductRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductPersistenceAdapterTest {

	private @Mock ProductRepository productRepository;

	private @Mock ProductPersistenceMapper productPersistenceMapper;

	private @InjectMocks ProductPersistenceAdapter productPersistenceAdapter;

	private CategoryEntity category;

	private Product product;

	private ProductEntity productEntity;

	private Pageable pageable;

	@BeforeEach
	void setUp() {
		category = Instancio.of(CategoryEntity.class)
			.set(field(CategoryEntity::isDeleted), false)
			.set(field(CategoryEntity::getDeletedAt), null)
			.set(field(CategoryEntity::getUid), UUID.randomUUID().toString())
			.supply(field(CategoryEntity::getProducts), () -> new ArrayList<>())
			.create();

		product = Instancio.of(Product.class)
			.set(field(Product::getUid), UUID.randomUUID().toString())
			.set(field(Product::isDeleted), false)
			.set(field(Product::getDeletedAt), null)
			.set(field(Product::getCategoryId), UUID.randomUUID().toString())
			.set(field(Product::getName), "nameProduct")
			.set(field(Product::getDescription), "description")
			.set(field(Product::getPrice), BigDecimal.valueOf(10.0))
			.set(field(Product::getStock), 10L)
			.create();

		productEntity = Instancio.of(ProductEntity.class)
			.set(field(ProductEntity::getUid), product.getUid())
			.set(field(ProductEntity::isDeleted), product.isDeleted())
			.set(field(ProductEntity::getDeletedAt), product.getDeletedAt())
			.set(field(ProductEntity::getCategory), category)
			.set(field(ProductEntity::getName), product.getName())
			.set(field(ProductEntity::getDescription), product.getDescription())
			.set(field(ProductEntity::getPrice), product.getPrice())
			.set(field(ProductEntity::getStock), product.getStock())
			.create();

		pageable = PageRequest.of(0, 20, Sort.Direction.ASC, "uid");
	}

	@Test
	void save() {
		when(productPersistenceMapper.toEntity(product)).thenReturn(productEntity);
		when(productRepository.save(productEntity)).thenReturn(productEntity);
		when(productPersistenceMapper.toDomain(productEntity)).thenReturn(product);

		Product result = productPersistenceAdapter.save(product);

		assertThat(result).isEqualTo(product);

		verify(productPersistenceMapper).toEntity(product);
		verify(productRepository).save(productEntity);
		verify(productPersistenceMapper).toDomain(productEntity);

	}

	@Test
	void searchAll() {
		when(productRepository.searchAll(pageable)).thenReturn(List.of(productEntity, productEntity));
		when(productPersistenceMapper.toProductList(List.of(productEntity, productEntity))).thenReturn(List.of(product, product));

		List<Product> result = productPersistenceAdapter.searchAll(pageable);

		assertThat(result).isEqualTo(List.of(product, product));

		verify(productRepository).searchAll(pageable);
		verify(productPersistenceMapper).toProductList(List.of(productEntity, productEntity));

	}

	@Test
	void searchAllByRegex() {
		when(productRepository.searchAllByRegex("nameProduct", pageable)).thenReturn(List.of(productEntity, productEntity));
		when(productPersistenceMapper.toProductList(List.of(productEntity, productEntity))).thenReturn(List.of(product, product));

		List<Product> result = productPersistenceAdapter.searchAllByRegex("nameProduct", pageable);

		assertThat(result).isEqualTo(List.of(product, product));

		verify(productRepository).searchAllByRegex("nameProduct", pageable);
		verify(productPersistenceMapper).toProductList(List.of(productEntity, productEntity));
	}

	@Test
	void searchAllByCategory() {
		when(productRepository.searchAllByCategory(category.getUid(), pageable)).thenReturn(List.of(productEntity, productEntity));
		when(productPersistenceMapper.toProductList(List.of(productEntity, productEntity))).thenReturn(List.of(product, product));

		List<Product> result = productPersistenceAdapter.searchAllByCategory(category.getUid(), pageable);

		assertThat(result).isEqualTo(List.of(product, product));

		verify(productRepository).searchAllByCategory(category.getUid(), pageable);
		verify(productPersistenceMapper).toProductList(List.of(productEntity, productEntity));
	}

	@Test
	void findByUidAndDeletedFalse() {
		when(productRepository.findByUidAndDeletedFalse(product.getUid())).thenReturn(productEntity);
		when(productPersistenceMapper.toDomain(productEntity)).thenReturn(product);

		Product result = productPersistenceAdapter.findByUidAndDeletedFalse(product.getUid());

		assertThat(result).isEqualTo(product);

		verify(productRepository).findByUidAndDeletedFalse(product.getUid());
		verify(productPersistenceMapper).toDomain(productEntity);
	}
}