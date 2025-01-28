package com.jmarqb.productsapi.infrastructure.adapters.output.persistence.JpaTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.model.CategoryEntity;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.model.ProductEntity;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.repository.CategoryRepository;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.repository.ProductRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

@DataJpaTest
@Transactional
class ProductJpaTest {

	private @Autowired ProductRepository productRepository;
	private @Autowired CategoryRepository categoryRepository;

	private CategoryEntity category;
	private List<ProductEntity> products;

	@BeforeEach
	void setUp() {
		productRepository.deleteAll();
		categoryRepository.deleteAll();

		category = Instancio.of(CategoryEntity.class)
			.set(field(CategoryEntity::isDeleted), false)
			.set(field(CategoryEntity::getDeletedAt), null)
			.set(field(CategoryEntity::getUid), UUID.randomUUID().toString())
			.supply(field(CategoryEntity::getProducts), () -> new ArrayList<>())
			.create();

		products = Instancio.ofList(ProductEntity.class)
			.size(10)
			.generate(field(ProductEntity::getName), gen -> gen.text().pattern("test.*"))
			.generate(field(ProductEntity::getUid), gen -> gen.text().uuid())
			.generate(field(ProductEntity::isDeleted), gen -> gen.booleans().probability(0))
			.generate(field(ProductEntity::getCategory), gen -> gen.oneOf(category))
			.supply(field(ProductEntity::getPrice), () -> BigDecimal.valueOf(10.0))
			.supply(field(ProductEntity::getStock), () -> 10L)
			.supply(field(ProductEntity::getDeletedAt), () -> null)
			.set(field(ProductEntity::getDescription), "description")
			.create();

		category.getProducts().forEach(product -> product.setCategory(category));

		categoryRepository.save(category);
		productRepository.saveAll(products);
	}

	@AfterEach
	void tearDown() {
		productRepository.deleteAll();
		categoryRepository.deleteAll();
	}

	@Test
	void save() {
		ProductEntity product = Instancio.of(ProductEntity.class)
			.set(field(ProductEntity::isDeleted), false)
			.set(field(ProductEntity::getDeletedAt), null)
			.set(field(ProductEntity::getCategory), category)
			.create();
		ProductEntity productEntity = productRepository.save(product);
		product.setUid(productEntity.getUid());

		assertThat(productEntity).usingRecursiveComparison().ignoringFields("uid").isEqualTo(product);
	}

	@Test
	void searchAllByDeletedFalseAndNameContainsIgnoreCase() {
		Pageable pageable = PageRequest.of(0, 20, Sort.Direction.ASC, "uid");

		List<ProductEntity> products = productRepository.searchAllByRegex("test", pageable);
		assertThat(!products.isEmpty()).isTrue();
		assertThat(products).usingRecursiveComparison().ignoringFields("uid").isEqualTo(this.products);
	}

	@Test
	void searchAllByDeletedFalse() {
		Pageable pageable = PageRequest.of(0, 20, Sort.Direction.ASC, "uid");

		List<ProductEntity> products = productRepository.searchAll(pageable);
		assertThat(!products.isEmpty()).isTrue();
		assertThat(this.products.size()).isEqualTo(products.size());
		assertThat(products).usingRecursiveComparison().ignoringFields("uid").isEqualTo(this.products);
	}

	@Test
	void searchAllByCategory() {
		List<ProductEntity> products = productRepository.searchAllByCategory(category.getUid(), PageRequest.of(0, 20, Sort.Direction.ASC, "uid"));
		assertThat(!products.isEmpty()).isTrue();
		assertThat(this.products.size()).isEqualTo(products.size());
		assertThat(products).usingRecursiveComparison().ignoringFields("uid").isEqualTo(this.products);
	}

	@Test
	void findByUidAndDeletedFalse() {
		ProductEntity product = productRepository.findByUidAndDeletedFalse(this.products.getFirst().getUid());
		assertThat(product).usingRecursiveComparison().ignoringFields("uid").isEqualTo(this.products.getFirst());
	}

	@Test
	void findByUidAndDeletedFalseNull() {
		ProductEntity product = productRepository.findByUidAndDeletedFalse(UUID.randomUUID().toString());
		assertThat(product).isNull();
	}


}
