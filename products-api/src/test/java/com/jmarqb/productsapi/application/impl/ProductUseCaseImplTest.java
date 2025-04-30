package com.jmarqb.productsapi.application.impl;

import com.jmarqb.productsapi.application.mapper.UpdateFieldMapper;
import com.jmarqb.productsapi.domain.model.Pagination;

import java.util.List;
import java.util.UUID;

import com.jmarqb.productsapi.domain.model.Product;
import com.jmarqb.productsapi.domain.ports.output.persistence.ProductPersistencePort;
import com.jmarqb.productsapi.application.exceptions.ProductNotFoundException;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.jmarqb.productsapi.Util.getPagination;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductUseCaseImplTest {

	private @Mock ProductPersistencePort productPersistencePort;

	private @Mock UpdateFieldMapper updateFieldMapper;

	private @InjectMocks ProductUseCaseImpl productUseCaseImpl;

	@Test
	void save() {
		Product inputProduct = Instancio.create(Product.class);
		inputProduct.setUid(null);

		when(productPersistencePort.save(any(Product.class)))
			.thenAnswer(invocation -> invocation.getArgument(0));

		Product actualProduct = productUseCaseImpl.save(inputProduct);

		assertThat(actualProduct.getUid()).isNotNull();
		assertThat(actualProduct.getUid()).hasSize(36);
		assertThat(actualProduct).usingRecursiveComparison()
			.ignoringFields("uid")
			.isEqualTo(inputProduct);
		verify(productPersistencePort).save(actualProduct);
	}

	@Test
	void searchContainsRegex() {
		Product product = Instancio.create(Product.class);
		String searchRegex = product.getName().substring(0, 3);
		int page = 0;
		int size = 10;
		String sort = "ASC";

		Pagination pagination = getPagination(page, size, sort);

		List<Product> expectedProducts = List.of(product);

		when(productPersistencePort
			.searchAllByRegex(searchRegex, pagination))
			.thenReturn(expectedProducts);

		List<Product> actualProducts = productUseCaseImpl.search(searchRegex, page, size, sort);

		assertThat(actualProducts).isEqualTo(expectedProducts);
		verify(productPersistencePort).searchAllByRegex(searchRegex, pagination);
	}

	@Test
	void searchNotRegex() {
		Product product = Instancio.create(Product.class);
		int page = 0;
		int size = 10;
		String sort = "ASC";

		Pagination pagination = getPagination(page, size, sort);

		List<Product> expectedProducts = List.of(product);

		when(productPersistencePort
			.searchAll(pagination))
			.thenReturn(expectedProducts);

		List<Product> actualProducts = productUseCaseImpl.search(null, page, size, sort);

		assertThat(actualProducts).isEqualTo(expectedProducts);
		verify(productPersistencePort).searchAll(pagination);
	}

	@Test
	void searchByCategory() {
		String someCategoryUid = UUID.randomUUID().toString();
		Product product = Instancio.create(Product.class);
		int page = 0;
		int size = 10;
		String sort = "ASC";

		Pagination pagination = getPagination(page, size, sort);

		List<Product> expectedProducts = List.of(product);

		when(productPersistencePort
			.searchAllByCategory(someCategoryUid, pagination))
			.thenReturn(expectedProducts);

		List<Product> actualProducts = productUseCaseImpl.searchByCategory(someCategoryUid, page, size, sort);

		assertThat(actualProducts).isEqualTo(expectedProducts);
		verify(productPersistencePort).searchAllByCategory(someCategoryUid, pagination);
	}

	@Test
	void findProduct() {
		Product product = Instancio.create(Product.class);
		String id = product.getUid();

		when(productPersistencePort.findByUidAndDeletedFalse(id))
			.thenReturn(product);

		Product actualProduct = productUseCaseImpl.findProduct(id);

		assertThat(actualProduct).isEqualTo(product);
		verify(productPersistencePort).findByUidAndDeletedFalse(id);
	}

	@Test
	void findProductNotFound() {
		String someUid = UUID.randomUUID().toString();

		when(productPersistencePort.findByUidAndDeletedFalse(someUid)).thenThrow(
			new ProductNotFoundException("Product with %s not found".formatted(someUid)));

		ProductNotFoundException exception = assertThrows(
			ProductNotFoundException.class, () -> productUseCaseImpl.findProduct(someUid));

		assertThat(exception.getMessage()).isEqualTo("Product with %s not found".formatted(someUid));
		verify(productPersistencePort).findByUidAndDeletedFalse(someUid);
	}

	@Test
	void updateProduct() {
		Product dataToUpdateProduct = Instancio.create(Product.class);
		String id = dataToUpdateProduct.getUid();

		Product product = Instancio.create(Product.class);
		product.setUid(id);
		product.setName(dataToUpdateProduct.getName());
		product.setDescription(dataToUpdateProduct.getDescription());

		when(productPersistencePort.findByUidAndDeletedFalse(id))
				.thenReturn(product);

		when(productPersistencePort.save(product)).thenReturn(product);

		doNothing().when(updateFieldMapper).updateProduct(dataToUpdateProduct, product);

		Product actualProduct = productUseCaseImpl.updateProduct(dataToUpdateProduct);

		assertThat(actualProduct).isEqualTo(product);
		verify(productPersistencePort).findByUidAndDeletedFalse(id);
		verify(productPersistencePort).save(product);
	}

	@Test
	void updateProductNotFound() {
		String someUid = UUID.randomUUID().toString();
		Product dataToUpdateProduct = Instancio.create(Product.class);
		dataToUpdateProduct.setUid(someUid);

		when(productPersistencePort.findByUidAndDeletedFalse(someUid)).thenThrow(
			new ProductNotFoundException("Product with %s not found".formatted(someUid)));

		ProductNotFoundException exception = assertThrows(
			ProductNotFoundException.class, () -> productUseCaseImpl.updateProduct(dataToUpdateProduct));

		assertThat(exception.getMessage()).isEqualTo("Product with %s not found".formatted(someUid));
		verify(productPersistencePort).findByUidAndDeletedFalse(someUid);
	}

	@Test
	void deleteProduct() {
		Product product = Instancio.create(Product.class);
		String id = product.getUid();

		when(productPersistencePort.findByUidAndDeletedFalse(id))
			.thenReturn(product);

		productUseCaseImpl.deleteProduct(id);

		assertThat(product.isDeleted()).isTrue();
		assertThat(product.getDeletedAt()).isNotNull();

		verify(productPersistencePort).findByUidAndDeletedFalse(id);
		verify(productPersistencePort).save(product);
	}

	@Test
	void deleteProductNotFound() {
		String someUid = UUID.randomUUID().toString();

		when(productPersistencePort.findByUidAndDeletedFalse(someUid)).thenThrow(
			new ProductNotFoundException("Product with %s not found".formatted(someUid)));

		ProductNotFoundException exception = assertThrows(
			ProductNotFoundException.class, () -> productUseCaseImpl.deleteProduct(someUid));

		assertThat(exception.getMessage()).isEqualTo("Product with %s not found".formatted(someUid));
		verify(productPersistencePort).findByUidAndDeletedFalse(someUid);
	}

	@Test
	void existProduct() {
		String someUid = UUID.randomUUID().toString();
		when(productPersistencePort.findByUidAndDeletedFalse(someUid)).thenReturn(null);

		ProductNotFoundException exception = assertThrows(
			ProductNotFoundException.class, () -> productUseCaseImpl.existProduct(someUid));

		assertThat(exception.getMessage()).isEqualTo("Product with %s not found".formatted(someUid));
		verify(productPersistencePort).findByUidAndDeletedFalse(someUid);
	}
}