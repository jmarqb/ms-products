package com.jmarqb.productsapi.infrastructure.adapters.input.rest.controller;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmarqb.productsapi.application.ports.input.CategoryUseCase;
import com.jmarqb.productsapi.domain.model.Category;
import com.jmarqb.productsapi.domain.model.Product;
import com.jmarqb.productsapi.infrastructure.adapters.exceptions.CategoryNotFoundException;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.advice.HandlerExceptionController;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.request.CreateCategoryDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.request.PatchCategoryDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.request.SearchBodyDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.response.CategoryResponseDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.response.DeleteResponseDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.response.PaginatedResponseDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.mapper.CategoryMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryRestControllerTest {

	private MockMvc mockMvc;
	private @Mock CategoryUseCase categoryUseCase;

	private @Mock CategoryMapper categoryMapper;

	private ObjectMapper objectMapper;

	private CategoryResponseDto categoryResponseDto;

	private List<Product> productList;

	private List<Category> categories;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
		mockMvc = MockMvcBuilders.standaloneSetup(new CategoryRestController(categoryUseCase, categoryMapper))
			.setControllerAdvice(HandlerExceptionController.class).build();

		String productUid = UUID.randomUUID().toString();
		String categoryUid = UUID.randomUUID().toString();

		categoryResponseDto = Instancio.of(CategoryResponseDto.class)
			.set(field(CategoryResponseDto::getUid), categoryUid)
			.set(field(CategoryResponseDto::isDeleted), false)
			.set(field(CategoryResponseDto::getDeletedAt), null)
			.set(field(CategoryResponseDto::getName), "nameCategory")
			.set(field(CategoryResponseDto::getDescription), "description")
			.set(field(CategoryResponseDto::getProducts), new ArrayList<Product>())
			.create();

		productList = List.of(Instancio.of(Product.class)
				.set(field(Product::isDeleted), false)
				.set(field(Product::getDeletedAt), null)
				.set(field(Product::getUid), productUid)
				.set(field(Product::getCategoryId), categoryUid)
				.set(field(Product::getName), "nameProduct")
				.set(field(Product::getDescription), "description")
				.set(field(Product::getPrice), BigDecimal.valueOf(10.0))
				.set(field(Product::getStock), 10L)
				.create(),
			Instancio.of(Product.class)
				.set(field(Product::isDeleted), false)
				.set(field(Product::getDeletedAt), null)
				.set(field(Product::getUid), productUid)
				.set(field(Product::getCategoryId), categoryUid)
				.set(field(Product::getName), "nameProduct")
				.set(field(Product::getDescription), "description")
				.set(field(Product::getPrice), BigDecimal.valueOf(10.0))
				.set(field(Product::getStock), 10L)
				.create());

		categories = List.of(Instancio.of(Category.class)
			.set(field(Category::isDeleted), categoryResponseDto.isDeleted())
			.set(field(Category::getDeletedAt), categoryResponseDto.getDeletedAt())
			.set(field(Category::getUid), categoryResponseDto.getUid())
			.set(field(Category::getName), categoryResponseDto.getName())
			.set(field(Category::getDescription), categoryResponseDto.getDescription())
			.set(field(Category::getProducts), productList)
			.create());
	}

	@Test
	void create() throws Exception {
		CreateCategoryDto createCategoryDto = Instancio.of(CreateCategoryDto.class)
			.set(field(CreateCategoryDto::getName), "nameCategory")
			.set(field(CreateCategoryDto::getDescription), "description")
			.create();

		when(categoryMapper.toDomain(any(CreateCategoryDto.class))).thenReturn(categories.getFirst());
		when(categoryUseCase.save(categories.getFirst())).thenReturn(categories.getFirst());
		when(categoryMapper.toResponse(categories.getFirst())).thenReturn(categoryResponseDto);

		mockMvc.perform(post("/api/v1/categories").contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(createCategoryDto)))
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.uid").value(categoryResponseDto.getUid()))
			.andExpect(jsonPath("$.name").value(categoryResponseDto.getName()))
			.andExpect(jsonPath("$.description").value(categoryResponseDto.getDescription()))
			.andExpect(jsonPath("$.deleted").value(categoryResponseDto.isDeleted()))
			.andExpect(jsonPath("$.deletedAt").value(categoryResponseDto.getDeletedAt()));

		verify(categoryMapper).toDomain(any(CreateCategoryDto.class));
		verify(categoryUseCase).save(categories.getFirst());
		verify(categoryMapper).toResponse(categories.getFirst());
	}

	@Test
	void search() throws Exception {
		SearchBodyDto searchBodyDto = SearchBodyDto.builder()
			.search(null)
			.page(0)
			.size(10)
			.sort("ASC")
			.build();

		PaginatedResponseDto expected = Instancio.of(PaginatedResponseDto.class)
			.set(field(PaginatedResponseDto::getTimestamp), LocalDateTime.now())
			.set(field(PaginatedResponseDto::getTotal), categories.size())
			.set(field(PaginatedResponseDto::getPage), searchBodyDto.getPage())
			.set(field(PaginatedResponseDto::getSize), searchBodyDto.getSize())
			.set(field(PaginatedResponseDto::getData), categories)
			.create();

		when(categoryUseCase.search(searchBodyDto.getSearch(), searchBodyDto.getPage(), searchBodyDto.getSize(),
			searchBodyDto.getSort())).thenReturn(categories);

		when(categoryMapper.toPaginatedResponse(any(List.class), anyInt(), anyInt(),
			anyInt(), any(LocalDateTime.class)))
			.thenReturn(expected);

		mockMvc.perform(post("/api/v1/categories/search").contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(searchBodyDto)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.total").value(expected.getTotal()))
			.andExpect(jsonPath("$.page").value(expected.getPage()))
			.andExpect(jsonPath("$.size").value(expected.getSize()))
			.andExpect(jsonPath("$.data[0].uid").value(categoryResponseDto.getUid()))
			.andExpect(jsonPath("$.data[0].name").value(categoryResponseDto.getName()))
			.andExpect(jsonPath("$.data[0].description").value(categoryResponseDto.getDescription()))
			.andExpect(jsonPath("$.data[0].deleted").value(categoryResponseDto.isDeleted()))
			.andExpect(jsonPath("$.data[0].deletedAt").value(categoryResponseDto.getDeletedAt()));

		verify(categoryUseCase).search(searchBodyDto.getSearch(), searchBodyDto.getPage(), searchBodyDto.getSize(),
			searchBodyDto.getSort());

		verify(categoryMapper).toPaginatedResponse(any(List.class), anyInt(), anyInt(),
			anyInt(), any(LocalDateTime.class));

	}

	@Test
	void findCategory() throws Exception {
		when(categoryUseCase.findCategory(categoryResponseDto.getUid())).thenReturn(categories.getFirst());
		when(categoryMapper.toResponse(categories.getFirst())).thenReturn(categoryResponseDto);

		mockMvc.perform(get("/api/v1/categories/{uid}", categoryResponseDto.getUid()))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.uid").value(categoryResponseDto.getUid()))
			.andExpect(jsonPath("$.name").value(categoryResponseDto.getName()))
			.andExpect(jsonPath("$.description").value(categoryResponseDto.getDescription()))
			.andExpect(jsonPath("$.deleted").value(categoryResponseDto.isDeleted()))
			.andExpect(jsonPath("$.deletedAt").value(categoryResponseDto.getDeletedAt()));

		verify(categoryUseCase).findCategory(categoryResponseDto.getUid());
		verify(categoryMapper).toResponse(categories.getFirst());
	}

	@Test
	void findCategoryByProduct() throws Exception {
		when(categoryUseCase.findCategoryByProductId(productList.getFirst().getUid())).thenReturn(categories.getFirst());
		when(categoryMapper.toResponse(categories.getFirst())).thenReturn(categoryResponseDto);

		mockMvc.perform(get("/api/v1/categories/by-product/{productUid}", productList.getFirst().getUid()))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.uid").value(categoryResponseDto.getUid()))
			.andExpect(jsonPath("$.name").value(categoryResponseDto.getName()))
			.andExpect(jsonPath("$.description").value(categoryResponseDto.getDescription()))
			.andExpect(jsonPath("$.deleted").value(categoryResponseDto.isDeleted()))
			.andExpect(jsonPath("$.deletedAt").value(categoryResponseDto.getDeletedAt()));

		verify(categoryUseCase).findCategoryByProductId(productList.getFirst().getUid());
		verify(categoryMapper).toResponse(categories.getFirst());

	}

	@Test
	void updateCategory() throws Exception {
		PatchCategoryDto patchCategoryDto = PatchCategoryDto.builder()
			.name(categoryResponseDto.getName())
			.description(categoryResponseDto.getDescription())
			.build();

		when(categoryMapper.toDomain(any(PatchCategoryDto.class))).thenReturn(categories.getFirst());
		when(categoryUseCase.updateCategory(any(Category.class))).thenReturn(categories.getFirst());
		when(categoryMapper.toResponse(categories.getFirst())).thenReturn(categoryResponseDto);

		mockMvc.perform(patch("/api/v1/categories/{uid}", categoryResponseDto.getUid())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(patchCategoryDto)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.uid").value(categoryResponseDto.getUid()))
			.andExpect(jsonPath("$.name").value(categoryResponseDto.getName()))
			.andExpect(jsonPath("$.description").value(categoryResponseDto.getDescription()))
			.andExpect(jsonPath("$.deleted").value(categoryResponseDto.isDeleted()))
			.andExpect(jsonPath("$.deletedAt").value(categoryResponseDto.getDeletedAt()));

		verify(categoryMapper).toDomain(any(PatchCategoryDto.class));
		verify(categoryUseCase).updateCategory(any(Category.class));
		verify(categoryMapper).toResponse(categories.getFirst());
	}

	@Test
	void removeCategory() throws Exception {
		DeleteResponseDto response = DeleteResponseDto.builder().deletedCount(1).acknowledged(true).build();

		mockMvc.perform(delete("/api/v1/categories/{uid}", categoryResponseDto.getUid()))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.deletedCount").value(response.getDeletedCount()))
			.andExpect(jsonPath("$.acknowledged").value(response.isAcknowledged()));

		verify(categoryUseCase).deleteCategory(categoryResponseDto.getUid());
	}

	@Test
	void handleCategoryNotFoundException() throws Exception {

		when(categoryUseCase.findCategory(anyString())).
			thenThrow(new CategoryNotFoundException("Category not found"));

		mockMvc.perform(get("/api/v1/categories/{uid}", UUID.randomUUID().toString()))
			.andExpect(status().isNotFound())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.message").value("Category not found"))
			.andExpect(jsonPath("$.status").value(404))
			.andExpect(jsonPath("$.error").value("NOT FOUND"))
			.andExpect(jsonPath("$.timestamp").exists());

		verify(categoryUseCase).findCategory(anyString());
	}
}