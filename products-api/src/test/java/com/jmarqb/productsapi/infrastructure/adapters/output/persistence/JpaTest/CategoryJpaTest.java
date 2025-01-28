package com.jmarqb.productsapi.infrastructure.adapters.output.persistence.JpaTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.model.CategoryEntity;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.model.ProductEntity;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.repository.CategoryRepository;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.repository.ProductRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

@DataJpaTest
@Transactional
class CategoryJpaTest {

	private @Autowired ProductRepository productRepository;
	private @Autowired CategoryRepository categoryRepository;

	private ProductEntity product;

	private List<CategoryEntity> categoriesList;

	@BeforeEach
	void setUp() {
		productRepository.deleteAll();
		categoryRepository.deleteAll();


		categoriesList = Instancio.ofList(CategoryEntity.class)
			.size(10)
			.generate(field(CategoryEntity::getName), gen -> gen.text().pattern("test.*"))
			.generate(field(CategoryEntity::getUid), gen -> gen.text().uuid())
			.generate(field(CategoryEntity::isDeleted), gen -> gen.booleans().probability(0))
			.supply(field(CategoryEntity::getDeletedAt), () -> null)
			.supply(field(CategoryEntity::getProducts), () -> new ArrayList<>())
			.set(field(CategoryEntity::getDescription), "description")
			.create();

		product = Instancio.of(ProductEntity.class)
			.set(field(ProductEntity::isDeleted), false)
			.set(field(ProductEntity::getDeletedAt), null)
			.set(field(ProductEntity::getCategory), categoriesList.getFirst())
			.create();


		categoriesList.getFirst().getProducts().forEach(product -> product.getCategory().setUid(categoriesList.getFirst().getUid()));

		categoryRepository.saveAll(categoriesList);
		productRepository.save(product);
	}

	@Test
	void save() {
		CategoryEntity category = Instancio.of(CategoryEntity.class)
			.set(field(CategoryEntity::isDeleted), false)
			.set(field(CategoryEntity::getDeletedAt), null)
			.set(field(CategoryEntity::getProducts), new ArrayList<>())
			.create();
		CategoryEntity categoryEntity = categoryRepository.save(category);
		category.setUid(categoryEntity.getUid());
		assertThat(categoryEntity).usingRecursiveComparison().ignoringFields("uid").isEqualTo(category);
	}

	@Test
	void searchAllByDeletedFalseAndNameContainsIgnoreCase() {
		Pageable pageable = PageRequest.of(0, 20, Sort.Direction.ASC, "uid");

		List<CategoryEntity> categories = categoryRepository.searchAllByRegex("test", pageable);
		assertThat(!categories.isEmpty()).isTrue();
		assertThat(categories).usingRecursiveComparison().ignoringFields("uid").isEqualTo(this.categoriesList);
	}

	@Test
	void searchAllByDeletedFalse() {
		Pageable pageable = PageRequest.of(0, 20, Sort.Direction.ASC, "uid");

		List<CategoryEntity> categories = categoryRepository.searchAll(pageable);
		assertThat(!categories.isEmpty()).isTrue();
		assertThat(this.categoriesList.size()).isEqualTo(categories.size());
		assertThat(categories).usingRecursiveComparison().ignoringFields("uid").isEqualTo(this.categoriesList);
	}

	@Test
	void findByUidAndDeletedFalse() {
		CategoryEntity category = categoryRepository.findByUidAndDeletedFalse(this.categoriesList.getFirst().getUid());
		assertThat(category).usingRecursiveComparison().ignoringFields("uid").isEqualTo(this.categoriesList.getFirst());
	}

	@Test
	void findByUidAndDeletedFalseNull() {
		CategoryEntity category = categoryRepository.findByUidAndDeletedFalse(UUID.randomUUID().toString());
		assertThat(category).isNull();
	}

	@Test
	void searchByProductId() {
		CategoryEntity category = categoryRepository.searchByProductId(product.getUid());
		assertThat(category).usingRecursiveComparison().ignoringFields("uid").isEqualTo(this.categoriesList.getFirst());
	}

	@Test
	void searchByProductIdNull() {
		CategoryEntity category = categoryRepository.searchByProductId(UUID.randomUUID().toString());
		assertThat(category).isNull();
	}
}
