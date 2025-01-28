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
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmarqb.productsapi.application.ports.input.ProductUseCase;
import com.jmarqb.productsapi.domain.model.Product;
import com.jmarqb.productsapi.infrastructure.adapters.exceptions.DuplicateKeyException;
import com.jmarqb.productsapi.infrastructure.adapters.exceptions.ProductNotFoundException;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.advice.HandlerExceptionController;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.request.CreateProductDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.request.PatchProductDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.request.SearchBodyDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.response.DeleteResponseDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.response.PaginatedResponseDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.response.ProductResponseDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.mapper.ProductMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductRestControllerTest {

	private MockMvc mockMvc;

	private @Mock ProductUseCase productUseCase;

	private @Mock ProductMapper productMapper;

	private ObjectMapper objectMapper;

	private ProductResponseDto productResponseDto;

	private List<Product> productList;

	private CreateProductDto createProductDto;


	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
		mockMvc = MockMvcBuilders.standaloneSetup(new ProductRestController(productUseCase, productMapper))
			.setControllerAdvice(HandlerExceptionController.class).build();

		String productUid = UUID.randomUUID().toString();
		String categoryUid = UUID.randomUUID().toString();

		productResponseDto = Instancio.of(ProductResponseDto.class)
			.set(field(ProductResponseDto::getUid), productUid)
			.set(field(ProductResponseDto::isDeleted), false)
			.set(field(ProductResponseDto::getDeletedAt), null)
			.set(field(ProductResponseDto::getCategoryId), categoryUid)
			.set(field(ProductResponseDto::getName), "nameProduct")
			.set(field(ProductResponseDto::getDescription), "description")
			.set(field(ProductResponseDto::getPrice), BigDecimal.valueOf(10.0))
			.set(field(ProductResponseDto::getStock), 10L)
			.create();

		productList = List.of(Instancio.of(Product.class)
				.set(field(Product::isDeleted), false)
				.set(field(Product::getDeletedAt), null)
				.set(field(Product::getUid), productResponseDto.getUid())
				.set(field(Product::getCategoryId), productResponseDto.getCategoryId())
				.set(field(Product::getName), productResponseDto.getName())
				.set(field(Product::getDescription), productResponseDto.getDescription())
				.set(field(Product::getPrice), productResponseDto.getPrice())
				.set(field(Product::getStock), productResponseDto.getStock())
				.create(),
			Instancio.of(Product.class)
				.set(field(Product::isDeleted), false)
				.set(field(Product::getDeletedAt), null)
				.set(field(Product::getUid), UUID.randomUUID().toString()).create());
		createProductDto = Instancio.of(CreateProductDto.class)
			.set(field(CreateProductDto::getName), productResponseDto.getName())
			.set(field(CreateProductDto::getDescription), productResponseDto.getDescription())
			.set(field(CreateProductDto::getPrice), productResponseDto.getPrice())
			.set(field(CreateProductDto::getStock), productResponseDto.getStock())
			.set(field(CreateProductDto::getCategoryId), productResponseDto.getCategoryId())
			.create();
	}

	@Test
	void create() throws Exception {

		Product product = productMapper.toDomain(createProductDto);

		when(productUseCase.save(product)).thenReturn(product);
		when(productMapper.toResponse(product)).thenReturn(productResponseDto);

		mockMvc.perform(post("/api/v1/products")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(createProductDto)))
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.uid").value(productResponseDto.getUid()))
			.andExpect(jsonPath("$.name").value(productResponseDto.getName()))
			.andExpect(jsonPath("$.description").value(productResponseDto.getDescription()))
			.andExpect(jsonPath("$.price").value(productResponseDto.getPrice()))
			.andExpect(jsonPath("$.stock").value(productResponseDto.getStock()))
			.andExpect(jsonPath("$.categoryId").value(productResponseDto.getCategoryId()))
			.andExpect(jsonPath("$.deleted").value(productResponseDto.isDeleted()))
			.andExpect(jsonPath("$.deletedAt").value(productResponseDto.getDeletedAt()));

		verify(productMapper).toDomain(createProductDto);
		verify(productUseCase).save(product);
		verify(productMapper).toResponse(product);

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
			.set(field(PaginatedResponseDto::getTotal), productList.size())
			.set(field(PaginatedResponseDto::getPage), searchBodyDto.getPage())
			.set(field(PaginatedResponseDto::getSize), searchBodyDto.getSize())
			.set(field(PaginatedResponseDto::getData), productList)
			.create();
		when(productUseCase.search(searchBodyDto.getSearch(), searchBodyDto.getPage(), searchBodyDto.getSize(),
			searchBodyDto.getSort())).thenReturn(productList);

		when(productMapper.toPaginatedResponse(eq(productList), eq(productList.size()), eq(searchBodyDto.getPage()),
			eq(searchBodyDto.getSize()), any(LocalDateTime.class)))
			.thenReturn(expected);


		mockMvc.perform(post("/api/v1/products/search").contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(searchBodyDto)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.total").value(expected.getTotal()))
			.andExpect(jsonPath("$.page").value(expected.getPage()))
			.andExpect(jsonPath("$.size").value(expected.getSize()))
			.andExpect(jsonPath("$.data[0].uid").value(productResponseDto.getUid()))
			.andExpect(jsonPath("$.data[0].name").value(productResponseDto.getName()))
			.andExpect(jsonPath("$.data[0].description").value(productResponseDto.getDescription()))
			.andExpect(jsonPath("$.data[0].price").value(productResponseDto.getPrice()))
			.andExpect(jsonPath("$.data[0].stock").value(productResponseDto.getStock()))
			.andExpect(jsonPath("$.data[0].categoryId").value(productResponseDto.getCategoryId()))
			.andExpect(jsonPath("$.data[0].deleted").value(productResponseDto.isDeleted()))
			.andExpect(jsonPath("$.data[0].deletedAt").value(productResponseDto.getDeletedAt()));


		verify(productUseCase).search(eq(searchBodyDto.getSearch()), eq(searchBodyDto.getPage()), eq(searchBodyDto.getSize()),
			eq(searchBodyDto.getSort()));

		verify(productMapper).toPaginatedResponse(eq(productList), eq(productList.size()), eq(searchBodyDto.getPage()),
			eq(searchBodyDto.getSize()), any(LocalDateTime.class));
	}

	@Test
	void searchProductsByCategory() throws Exception {
		String categoryUid = UUID.randomUUID().toString();
		int page = 0;
		int size = 10;
		String sort = "ASC";

		PaginatedResponseDto expected = Instancio.of(PaginatedResponseDto.class)
			.set(field(PaginatedResponseDto::getTimestamp), LocalDateTime.now())
			.set(field(PaginatedResponseDto::getTotal), productList.size())
			.set(field(PaginatedResponseDto::getPage), page)
			.set(field(PaginatedResponseDto::getSize), size)
			.set(field(PaginatedResponseDto::getData), productList)
			.create();
		when(productUseCase.searchByCategory(categoryUid, page, size, sort)).thenReturn(productList);

		when(productMapper.toPaginatedResponse(eq(productList), eq(productList.size()),
			eq(page), eq(size), any(LocalDateTime.class))).thenReturn(expected);

		mockMvc.perform(post("/api/v1/products/search/by-category")
			.param("categoryUid", categoryUid)
			.param("page", String.valueOf(page))
			.param("size", String.valueOf(size))
			.param("sort", sort)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.total").value(expected.getTotal()))
			.andExpect(jsonPath("$.page").value(expected.getPage()))
			.andExpect(jsonPath("$.size").value(expected.getSize()))
			.andExpect(jsonPath("$.data[0].uid").value(productResponseDto.getUid()))
			.andExpect(jsonPath("$.data[0].name").value(productResponseDto.getName()))
			.andExpect(jsonPath("$.data[0].description").value(productResponseDto.getDescription()))
			.andExpect(jsonPath("$.data[0].price").value(productResponseDto.getPrice()))
			.andExpect(jsonPath("$.data[0].stock").value(productResponseDto.getStock()))
			.andExpect(jsonPath("$.data[0].categoryId").value(productResponseDto.getCategoryId()))
			.andExpect(jsonPath("$.data[0].deleted").value(productResponseDto.isDeleted()))
			.andExpect(jsonPath("$.data[0].deletedAt").value(productResponseDto.getDeletedAt()));

		verify(productUseCase).searchByCategory(categoryUid, page, size, sort);
		verify(productMapper).toPaginatedResponse(eq(productList), eq(productList.size()),
			eq(page), eq(size), any(LocalDateTime.class));

	}

	@Test
	void findProduct() throws Exception {
		when(productUseCase.findProduct(productResponseDto.getUid())).thenReturn(productList.getFirst());
		when(productMapper.toResponse(productList.getFirst())).thenReturn(productResponseDto);

		mockMvc.perform(get("/api/v1/products/{uid}", productResponseDto.getUid())
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("uid").value(productResponseDto.getUid()))
			.andExpect(jsonPath("name").value(productResponseDto.getName()))
			.andExpect(jsonPath("description").value(productResponseDto.getDescription()))
			.andExpect(jsonPath("price").value(productResponseDto.getPrice()))
			.andExpect(jsonPath("stock").value(productResponseDto.getStock()))
			.andExpect(jsonPath("categoryId").value(productResponseDto.getCategoryId()))
			.andExpect(jsonPath("deleted").value(productResponseDto.isDeleted()))
			.andExpect(jsonPath("deletedAt").value(productResponseDto.getDeletedAt()));

		verify(productUseCase).findProduct(productResponseDto.getUid());
		verify(productMapper).toResponse(productList.getFirst());
	}

	@Test
	void updateProduct() throws Exception {
		PatchProductDto patchProductDto = PatchProductDto.builder()
			.name(productResponseDto.getName())
			.description(productResponseDto.getDescription())
			.price(productResponseDto.getPrice())
			.stock(productResponseDto.getStock())
			.build();


		when(productMapper.toDomain(any(PatchProductDto.class))).thenReturn(productList.getFirst());
		when(productUseCase.updateProduct(any(Product.class))).thenReturn(productList.getFirst());
		when(productMapper.toResponse(productList.getFirst())).thenReturn(productResponseDto);

		mockMvc.perform(patch("/api/v1/products/{uid}", productResponseDto.getUid())
			.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(patchProductDto)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("uid").value(productResponseDto.getUid()))
			.andExpect(jsonPath("name").value(productResponseDto.getName()))
			.andExpect(jsonPath("description").value(productResponseDto.getDescription()))
			.andExpect(jsonPath("price").value(productResponseDto.getPrice()))
			.andExpect(jsonPath("stock").value(productResponseDto.getStock()))
			.andExpect(jsonPath("categoryId").value(productResponseDto.getCategoryId()))
			.andExpect(jsonPath("deleted").value(productResponseDto.isDeleted()))
			.andExpect(jsonPath("deletedAt").value(productResponseDto.getDeletedAt()));

		verify(productMapper).toDomain(any(PatchProductDto.class));
		verify(productUseCase).updateProduct(any(Product.class));
		verify(productMapper).toResponse(any(Product.class));

	}

	@Test
	void removeProduct() throws Exception {
		DeleteResponseDto response = DeleteResponseDto.builder().deletedCount(1).acknowledged(true).build();

		mockMvc.perform(delete("/api/v1/products/{uid}", productResponseDto.getUid())
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.deletedCount").value(response.getDeletedCount()))
			.andExpect(jsonPath("$.acknowledged").value(response.isAcknowledged()));

		verify(productUseCase).deleteProduct(productResponseDto.getUid());
	}

	@Test
	void handleDuplicatedKeyException() throws Exception {

		Product product = productMapper.toDomain(createProductDto);
		when(productUseCase.save(product)).thenThrow(new DuplicateKeyException("Duplicate key"));

		mockMvc.perform(post("/api/v1/products")
			.content(objectMapper.writeValueAsString(createProductDto)).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.message").value("Could not execute statement: Duplicate key or Duplicate entry"))
			.andExpect(jsonPath("$.status").value(400))
			.andExpect(jsonPath("$.error").value("Duplicate Key"))
			.andExpect(jsonPath("$.timestamp").exists());

		verify(productUseCase).save(product);
	}

	@Test
	void handleProductNotFoundException() throws Exception {

		when(productUseCase.findProduct(any())).thenThrow(new ProductNotFoundException("Product not found"));

		mockMvc.perform(get("/api/v1/products/{uid}", UUID.randomUUID().toString())).andExpect(status().isNotFound())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.message").value("Product not found"))
			.andExpect(jsonPath("$.status").value(404))
			.andExpect(jsonPath("$.error").value("NOT FOUND"))
			.andExpect(jsonPath("$.timestamp").exists());

		verify(productUseCase).findProduct(any());
	}
}