package com.jmarqb.productsapi.infrastructure.adapters.output.persistence;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.jmarqb.productsapi.domain.model.Category;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.mapper.CategoryPersistenceMapper;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.model.CategoryEntity;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.repository.CategoryRepository;
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
class CategoryPersistenceAdapterTest {

	private @Mock CategoryRepository categoryRepository;

	private @Mock CategoryPersistenceMapper categoryPersistenceMapper;

	private @InjectMocks CategoryPersistenceAdapter categoryPersistenceAdapter;

	private CategoryEntity categoryEntity;

	private Category category;
	private Pageable pageable;


	@BeforeEach
	void setUp() {
		category = Instancio.of(Category.class)
			.set(field(Category::isDeleted), false)
			.set(field(Category::getDeletedAt), null)
			.set(field(Category::getUid), UUID.randomUUID().toString())
			.supply(field(Category::getProducts), () -> new ArrayList<>())
			.create();

		categoryEntity = Instancio.of(CategoryEntity.class)
			.set(field(CategoryEntity::isDeleted), category.isDeleted())
			.set(field(CategoryEntity::getDeletedAt), category.getDeletedAt())
			.set(field(CategoryEntity::getUid), category.getUid())
			.set(field(CategoryEntity::getProducts), new ArrayList<>())
			.create();

		pageable = PageRequest.of(0, 20, Sort.Direction.ASC, "uid");

	}

	@Test
	void save() {
		when(categoryPersistenceMapper.toEntity(category)).thenReturn(categoryEntity);
		when(categoryRepository.save(categoryEntity)).thenReturn(categoryEntity);
		when(categoryPersistenceMapper.toDomain(categoryEntity)).thenReturn(category);

		Category result = categoryPersistenceAdapter.save(category);

		assertThat(result).isEqualTo(category);
		verify(categoryPersistenceMapper).toEntity(category);
		verify(categoryRepository).save(categoryEntity);
		verify(categoryPersistenceMapper).toDomain(categoryEntity);
	}

	@Test
	void searchAll() {
		when(categoryRepository.searchAll(pageable)).thenReturn(List.of(categoryEntity, categoryEntity));
		when(categoryPersistenceMapper.toCategoryList(List.of(categoryEntity, categoryEntity))).thenReturn(List.of(category, category));

		List<Category> result = categoryPersistenceAdapter.searchAll(pageable);

		assertThat(result).isEqualTo(List.of(category, category));

		verify(categoryRepository).searchAll(pageable);
		verify(categoryPersistenceMapper).toCategoryList(List.of(categoryEntity, categoryEntity));
	}

	@Test
	void searchAllByRegex() {
		when(categoryRepository.searchAllByRegex("nameCategory", pageable)).thenReturn(List.of(categoryEntity, categoryEntity));
		when(categoryPersistenceMapper.toCategoryList(List.of(categoryEntity, categoryEntity))).thenReturn(List.of(category, category));

		List<Category> result = categoryPersistenceAdapter.searchAllByRegex("nameCategory", pageable);

		assertThat(result).isEqualTo(List.of(category, category));

		verify(categoryRepository).searchAllByRegex("nameCategory", pageable);
		verify(categoryPersistenceMapper).toCategoryList(List.of(categoryEntity, categoryEntity));
	}

	@Test
	void searchByProductId() {
		String productId = UUID.randomUUID().toString();
		when(categoryRepository.searchByProductId(productId)).thenReturn(categoryEntity);
		when(categoryPersistenceMapper.toDomain(categoryEntity)).thenReturn(category);

		Category result = categoryPersistenceAdapter.searchByProductId(productId);

		assertThat(result).isEqualTo(category);

		verify(categoryRepository).searchByProductId(productId);
		verify(categoryPersistenceMapper).toDomain(categoryEntity);
	}

	@Test
	void findByUidAndDeletedFalse() {
		when(categoryRepository.findByUidAndDeletedFalse(category.getUid())).thenReturn(categoryEntity);
		when(categoryPersistenceMapper.toDomain(categoryEntity)).thenReturn(category);

		Category result = categoryPersistenceAdapter.findByUidAndDeletedFalse(category.getUid());

		assertThat(result).isEqualTo(category);

		verify(categoryRepository).findByUidAndDeletedFalse(category.getUid());
		verify(categoryPersistenceMapper).toDomain(categoryEntity);
	}
}