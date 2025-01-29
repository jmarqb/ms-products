package com.jmarqb.productsapi.application.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

import com.jmarqb.productsapi.domain.model.Category;
import com.jmarqb.productsapi.domain.model.Product;
import com.jmarqb.productsapi.domain.ports.output.persistence.CategoryPersistencePort;
import com.jmarqb.productsapi.domain.ports.output.persistence.ProductPersistencePort;
import com.jmarqb.productsapi.infrastructure.adapters.exceptions.CategoryNotFoundException;
import com.jmarqb.productsapi.infrastructure.adapters.exceptions.CategoryWithProductsException;
import com.jmarqb.productsapi.infrastructure.adapters.exceptions.ProductNotFoundException;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryUseCaseImplTest {

	private @Mock CategoryPersistencePort categoryPersistencePort;

	private @Mock ProductPersistencePort productPersistencePort;

	private @InjectMocks CategoryUseCaseImpl categoryUseCaseImpl;

	@Test
	void save() {
		Category category = Instancio.create(Category.class);
		category.setUid(null);

		when(categoryPersistencePort.save(any(Category.class)))
			.thenAnswer(invocation -> invocation.getArgument(0));

		Category actualCategory = categoryUseCaseImpl.save(category);

		assertThat(actualCategory.getUid()).isNotNull();
		assertThat(actualCategory.getUid()).hasSize(36);
		assertThat(actualCategory).usingRecursiveComparison()
			.ignoringFields("uid")
			.isEqualTo(category);
		verify(categoryPersistencePort).save(actualCategory);
	}

	@Test
	void searchContainsRegex() {
		Category category = Instancio.create(Category.class);
		String searchRegex = category.getName().substring(0, 3);
		int page = 0;
		int size = 10;
		String sort = "ASC";

		List<Category> expectedCategories = List.of(category);

		when(categoryPersistencePort
			.searchAllByRegex(searchRegex, PageRequest.of(page, size, Sort.Direction.ASC, "id")))
			.thenReturn(expectedCategories);

		List<Category> actualCategories = categoryUseCaseImpl.search(searchRegex, page, size, sort);

		assertThat(actualCategories).isEqualTo(expectedCategories);
		verify(categoryPersistencePort).searchAllByRegex(searchRegex, PageRequest.of(page, size, Sort.Direction.ASC, "id"));
	}

	@Test
	void searchNotRegex() {
		Category category = Instancio.create(Category.class);
		int page = 0;
		int size = 10;
		String sort = "ASC";

		List<Category> expectedCategories = List.of(category);

		when(categoryPersistencePort
			.searchAll(PageRequest.of(page, size, Sort.Direction.ASC, "id")))
			.thenReturn(expectedCategories);

		List<Category> actualCategories = categoryUseCaseImpl.search(null, page, size, sort);

		assertThat(actualCategories).isEqualTo(expectedCategories);
		verify(categoryPersistencePort).searchAll(PageRequest.of(page, size, Sort.Direction.ASC, "id"));
	}

	@Test
	void findCategory() {
		Category category = Instancio.create(Category.class);
		String uid = category.getUid();

		when(categoryPersistencePort.findByUidAndDeletedFalse(uid))
			.thenReturn(category);

		Category actualCategory = categoryUseCaseImpl.findCategory(uid);

		assertThat(actualCategory).isEqualTo(category);
		verify(categoryPersistencePort).findByUidAndDeletedFalse(uid);
	}

	@Test
	void findCategoryNotFound() {
		String someUid = UUID.randomUUID().toString();

		when(categoryPersistencePort.findByUidAndDeletedFalse(someUid)).thenThrow(
			new CategoryNotFoundException("Category with %s not found".formatted(someUid)));

		CategoryNotFoundException exception = assertThrows(
			CategoryNotFoundException.class, () -> categoryUseCaseImpl.findCategory(someUid));

		assertThat(exception.getMessage()).isEqualTo("Category with %s not found".formatted(someUid));
		verify(categoryPersistencePort).findByUidAndDeletedFalse(someUid);
	}

	@Test
	void findCategoryByProductId() {
		String someUid = UUID.randomUUID().toString();
		Product product = Instancio.create(Product.class);
		product.setUid(someUid);
		Category category = Instancio.create(Category.class);
		category.setUid(UUID.randomUUID().toString());

		when(productPersistencePort.findByUidAndDeletedFalse(someUid)).thenReturn(product);
		when(categoryPersistencePort.searchByProductId(someUid)).thenReturn(category);

		Category actualCategory = categoryUseCaseImpl.findCategoryByProductId(someUid);

		assertThat(actualCategory).isEqualTo(category);
		verify(productPersistencePort).findByUidAndDeletedFalse(someUid);
		verify(categoryPersistencePort).searchByProductId(someUid);
	}

	@Test
	void findCategoryByProductIdThrowProductNotFoundException() {
		String someUid = UUID.randomUUID().toString();
		when(productPersistencePort.findByUidAndDeletedFalse(someUid)).thenReturn(null);

		ProductNotFoundException exception = assertThrows(
			ProductNotFoundException.class, () -> categoryUseCaseImpl.findCategoryByProductId(someUid));

		assertThat(exception.getMessage()).isEqualTo("Product with %s not found".formatted(someUid));
		verify(productPersistencePort).findByUidAndDeletedFalse(someUid);
	}

	@Test
	void findCategoryByProductIdThrowCategoryNotFoundException() {
		String someUid = UUID.randomUUID().toString();
		when(productPersistencePort.findByUidAndDeletedFalse(someUid)).thenReturn(Instancio.create(Product.class));

		CategoryNotFoundException exception = assertThrows(
			CategoryNotFoundException.class, () -> categoryUseCaseImpl.findCategoryByProductId(someUid));

		assertThat(exception.getMessage()).isEqualTo("No category found for product with %s".formatted(someUid));
		verify(productPersistencePort).findByUidAndDeletedFalse(someUid);
	}

	@Test
	void updateCategory() {
		Category dataToUpdateCategory = Instancio.create(Category.class);
		String uid = dataToUpdateCategory.getUid();

		Category actualCategory = Instancio.create(Category.class);
		actualCategory.setUid(uid);
		actualCategory.setName(dataToUpdateCategory.getName());
		actualCategory.setDescription(dataToUpdateCategory.getDescription());

		when(categoryPersistencePort.findByUidAndDeletedFalse(uid))
			.thenReturn(actualCategory);

		when(categoryPersistencePort.save(actualCategory))
			.thenReturn(actualCategory);

		Category updatedCategory = categoryUseCaseImpl.updateCategory(dataToUpdateCategory);

		assertThat(updatedCategory).isEqualTo(actualCategory);
		verify(categoryPersistencePort).findByUidAndDeletedFalse(uid);
		verify(categoryPersistencePort).save(actualCategory);
	}

	@Test
	void updateCategoryNotFound() {
		Category dataToUpdateCategory = Instancio.create(Category.class);
		String someUid = UUID.randomUUID().toString();
		dataToUpdateCategory.setUid(someUid);

		when(categoryPersistencePort.findByUidAndDeletedFalse(someUid)).thenThrow(
			new CategoryNotFoundException("Category with %s not found".formatted(someUid)));

		CategoryNotFoundException exception = assertThrows(
			CategoryNotFoundException.class, () -> categoryUseCaseImpl.updateCategory(dataToUpdateCategory));

		assertThat(exception.getMessage()).isEqualTo("Category with %s not found".formatted(someUid));
		verify(categoryPersistencePort).findByUidAndDeletedFalse(someUid);
	}

	@Test
	void deleteCategory() {
		Category category = Instancio.create(Category.class);
		String uid = category.getUid();
		category.getProducts().clear();

		when(categoryPersistencePort.findByUidAndDeletedFalse(uid))
			.thenReturn(category);

		categoryUseCaseImpl.deleteCategory(uid);

		assertThat(category.isDeleted()).isTrue();
		assertThat(category.getDeletedAt()).isNotNull();
		verify(categoryPersistencePort).findByUidAndDeletedFalse(uid);
		verify(categoryPersistencePort).save(category);
	}

	@Test
	void deleteCategoryWithProducts() {
		Category category = Instancio.create(Category.class);
		String uid = category.getUid();
		category.getProducts().add(Instancio.create(Product.class));

		when(categoryPersistencePort.findByUidAndDeletedFalse(uid))
			.thenReturn(category);

		CategoryWithProductsException exception = assertThrows(
			CategoryWithProductsException.class, () -> categoryUseCaseImpl.deleteCategory(uid));

		assertThat(exception.getMessage()).isEqualTo("Category with %s has products".formatted(uid));
		verify(categoryPersistencePort).findByUidAndDeletedFalse(uid);
	}

	@Test
	void deleteCategoryNotFound() {
		String someUid = UUID.randomUUID().toString();

		when(categoryPersistencePort.findByUidAndDeletedFalse(someUid)).thenThrow(
			new CategoryNotFoundException("Category with %s not found".formatted(someUid)));

		CategoryNotFoundException exception = assertThrows(
			CategoryNotFoundException.class, () -> categoryUseCaseImpl.deleteCategory(someUid));

		assertThat(exception.getMessage()).isEqualTo("Category with %s not found".formatted(someUid));
		verify(categoryPersistencePort).findByUidAndDeletedFalse(someUid);
	}

	@Test
	void existCategory() {
		String someUid = UUID.randomUUID().toString();
		when(categoryPersistencePort.findByUidAndDeletedFalse(someUid)).thenReturn(null);

		CategoryNotFoundException exception = assertThrows(
			CategoryNotFoundException.class, () -> categoryUseCaseImpl.existCategory(someUid));

		assertThat(exception.getMessage()).isEqualTo("Category with %s not found".formatted(someUid));
		verify(categoryPersistencePort).findByUidAndDeletedFalse(someUid);

	}
}