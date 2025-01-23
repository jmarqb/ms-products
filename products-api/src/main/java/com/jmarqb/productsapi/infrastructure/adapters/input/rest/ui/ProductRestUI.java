package com.jmarqb.productsapi.infrastructure.adapters.input.rest.ui;

import com.jmarqb.productsapi.domain.model.Error;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.request.*;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.response.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Products Management", description = "Endpoints for managing products")
public interface ProductRestUI {

    @PreAuthorize("hasAuthority('ROLE_MANAGERS')")
    @PostMapping
    @ApiResponse(responseCode = "201", description = "Product created successfully")
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Error.class),
            examples = @ExampleObject(value = OpenApiResponses.BAD_REQUEST_EXAMPLE)))
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Error.class),
            examples = @ExampleObject(value = OpenApiResponses.UNAUTHORIZED_EXAMPLE)))
    ResponseEntity<ProductResponseDto> create(@Valid @RequestBody CreateProductDto createProductDto);

    @PreAuthorize("hasAuthority('ROLE_USERS')")
    @PostMapping("/search")
    @ApiResponse(responseCode = "200", description = "Products found successfully")
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
    @PostMapping("/search/by-category")
    @Parameters({
        @Parameter(name = "categoryUid", description = "Category uid", required = true),
        @Parameter(name = "page", description = "Page number", required = false, example = "0"),
        @Parameter(name = "size", description = "Page size", required = false, example = "20"),
        @Parameter(name = "sort", description = "Sort", required = false, example = "ASC")

    })
    @ApiResponse(responseCode = "200", description = "Products found successfully")
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
    ResponseEntity<PaginatedResponseDto> searchProductsByCategory(@RequestParam String categoryUid,
                                                                  @RequestParam(required = false, defaultValue = "0") Integer page,
                                                                  @RequestParam(required = false, defaultValue = "20") Integer size,
                                                                  @RequestParam(required = false, defaultValue = "ASC") String sort);

    @PreAuthorize("hasAuthority('ROLE_USERS')")
    @GetMapping("/{uid}")
    @ApiResponse(responseCode = "200", description = "Product found successfully")
    @ApiResponse(responseCode = "404", description = "Product not found",
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
    ResponseEntity<ProductResponseDto> findProduct(@PathVariable String uid);

    @PreAuthorize("hasAuthority('ROLE_MANAGERS')")
    @PatchMapping("/{uid}")
    @ApiResponse(responseCode = "200", description = "Product updated successfully")
    @ApiResponse(responseCode = "404", description = "Product not found",
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
    ResponseEntity<ProductResponseDto> updateProduct(@PathVariable String uid, @RequestBody PatchProductDto patchProductDto);

    @DeleteMapping("/{uid}")
    @ApiResponse(responseCode = "200", description = "Product deleted successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeleteResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Product not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class),
                    examples = @ExampleObject(value = OpenApiResponses.ENTITY_NOT_FOUND_EXAMPLE)))
    @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Error.class),
                    examples = @ExampleObject(value = OpenApiResponses.UNAUTHORIZED_EXAMPLE
                    )))
    ResponseEntity<DeleteResponseDto> removeProduct(@PathVariable String uid);
}
