package com.jmarqb.productsapi.infrastructure.adapters.output.persistence.mapper;

import java.util.ArrayList;
import java.util.UUID;

import com.jmarqb.productsapi.application.exceptions.CategoryNotFoundException;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.model.CategoryEntity;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.repository.CategoryRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryMapperHelperTest {

	private @Mock CategoryRepository categoryRepository;

	private @InjectMocks CategoryMapperHelper categoryMapperHelper;


	@Test
	void map() {
		CategoryEntity category = Instancio.of(CategoryEntity.class)
			.set(field(CategoryEntity::isDeleted), false)
			.set(field(CategoryEntity::getDeletedAt), null)
			.set(field(CategoryEntity::getUid), UUID.randomUUID().toString())
			.supply(field(CategoryEntity::getProducts), () -> new ArrayList<>())
			.create();

		when(categoryRepository.findByUidAndDeletedFalse(category.getUid())).thenReturn(category);

		CategoryEntity result = categoryMapperHelper.map(category.getUid());

		assertThat(result).isEqualTo(category);
		verify(categoryRepository).findByUidAndDeletedFalse(category.getUid());
	}

	@Test
	void mapNotFound() {
		when(categoryRepository.findByUidAndDeletedFalse("")).thenReturn(null);

		CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> categoryMapperHelper.map(""));

		assertThat(exception.getMessage()).isEqualTo("Category with %s not found".formatted(""));
		verify(categoryRepository).findByUidAndDeletedFalse("");
	}
}