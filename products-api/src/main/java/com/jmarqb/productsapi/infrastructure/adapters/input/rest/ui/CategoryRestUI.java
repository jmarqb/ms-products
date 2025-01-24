package com.jmarqb.productsapi.infrastructure.adapters.input.rest.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.jmarqb.productsapi.domain.model.Error;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.request.CreateCategoryDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.request.PatchCategoryDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.request.SearchBodyDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.response.CategoryResponseDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.response.DeleteResponseDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.response.OpenApiResponses;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.response.PaginatedResponseDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/categories")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Category Management", description = "Endpoints for managing categories")
public interface CategoryRestUI {

	@PreAuthorize("hasAuthority('ROLE_MANAGERS')")
	@PostMapping
	@ApiResponse(responseCode = "201", description = "Category created successfully")
	@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json",
		schema = @Schema(implementation = Error.class),
		examples = @ExampleObject(value = OpenApiResponses.BAD_REQUEST_EXAMPLE)))
	@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json",
		schema = @Schema(implementation = Error.class),
		examples = @ExampleObject(value = OpenApiResponses.UNAUTHORIZED_EXAMPLE)))
	ResponseEntity<CategoryResponseDto> create(@Valid @RequestBody CreateCategoryDto createCategoryDto);

	@PreAuthorize("hasAuthority('ROLE_USERS')")
	@PostMapping("/search")
	@ApiResponse(responseCode = "200", description = "Categories found successfully")
	@ApiResponse(responseCode = "400", description = "Bad request",
		content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = Error.class),
			examples = @ExampleObject(value = OpenApiResponses.BAD_REQUEST_EXAMPLE)))
	@ApiResponse(
		responseCode = "401",
		description = "Unauthorized",
		content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = Error.class),
			examples = @ExampleObject(value = OpenApiResponses.UNAUTHORIZED_EXAMPLE
			)))
	ResponseEntity<PaginatedResponseDto> search(@Valid @RequestBody SearchBodyDto searchBodyDto);

	@PreAuthorize("hasAuthority('ROLE_USERS')")
	@GetMapping("/{uid}")
	@ApiResponse(responseCode = "200", description = "Category found successfully")
	@ApiResponse(responseCode = "404", description = "Category not found",
		content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = Error.class),
			examples = @ExampleObject(value = OpenApiResponses.ENTITY_NOT_FOUND_EXAMPLE)))
	@ApiResponse(
		responseCode = "401",
		description = "Unauthorized",
		content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = Error.class),
			examples = @ExampleObject(value = OpenApiResponses.UNAUTHORIZED_EXAMPLE
			)))
	ResponseEntity<CategoryResponseDto> findCategory(@PathVariable String uid);

	@PreAuthorize("hasAuthority('ROLE_USERS')")
	@GetMapping("/by-product/{productUid}")
	@ApiResponse(responseCode = "200", description = "Category found successfully")
	@ApiResponse(responseCode = "404", description = "Category not found",
		content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = Error.class),
			examples = @ExampleObject(value = OpenApiResponses.ENTITY_NOT_FOUND_EXAMPLE)))
	@ApiResponse(
		responseCode = "401",
		description = "Unauthorized",
		content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = Error.class),
			examples = @ExampleObject(value = OpenApiResponses.UNAUTHORIZED_EXAMPLE
			)))
	ResponseEntity<CategoryResponseDto> findCategoryByProduct(@PathVariable String productUid);

	@PreAuthorize("hasAuthority('ROLE_MANAGERS')")
	@PatchMapping("/{uid}")
	@ApiResponse(responseCode = "200", description = "Category updated successfully")
	@ApiResponse(responseCode = "404", description = "Category not found",
		content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = Error.class),
			examples = @ExampleObject(value = OpenApiResponses.ENTITY_NOT_FOUND_EXAMPLE)))
	@ApiResponse(responseCode = "400", description = "Bad request",
		content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = Error.class),
			examples = @ExampleObject(value = OpenApiResponses.BAD_REQUEST_EXAMPLE)))
	@ApiResponse(
		responseCode = "401",
		description = "Unauthorized",
		content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = Error.class),
			examples = @ExampleObject(value = OpenApiResponses.UNAUTHORIZED_EXAMPLE
			)))
	ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable String uid, @RequestBody PatchCategoryDto patchCategoryDto);

	@DeleteMapping("/{uid}")
	@ApiResponse(responseCode = "200", description = "Category deleted successfully",
		content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeleteResponseDto.class)))
	@ApiResponse(responseCode = "404", description = "Category not found",
		content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class),
			examples = @ExampleObject(value = OpenApiResponses.ENTITY_NOT_FOUND_EXAMPLE)))
	@ApiResponse(
		responseCode = "401",
		description = "Unauthorized",
		content = @Content(mediaType = "application/json",
			schema = @Schema(implementation = Error.class),
			examples = @ExampleObject(value = OpenApiResponses.UNAUTHORIZED_EXAMPLE
			)))
	ResponseEntity<DeleteResponseDto> removeCategory(@PathVariable String uid);
}
