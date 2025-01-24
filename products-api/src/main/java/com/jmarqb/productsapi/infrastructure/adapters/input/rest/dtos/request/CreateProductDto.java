package com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.UUID;

/**
 * DTO for creating a new product.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Schema(description = "Request DTO for creating a new product")
public class CreateProductDto {

	@Schema(description = "name must be between 3 and 30 characters", example = "T-shirt")
	@NotBlank
	@Size(min = 3, max = 30, message = "name must be between 3 and 30 characters")
	private String name;

	@Schema(description = "description must be between 3 and 50 characters", example = "T-shirt Description")
	@NotBlank
	@Size(min = 3, max = 50, message = "description must be between 3 and 50 characters")
	private String description;

	@Schema(description = "categoryId must be a valid UUID", example = "7b2f4f93-c450-4f2f-9c6f-d3c2f39f9a8c")
	@NotBlank
	@UUID(message = "categoryId must be a valid UUID")
	private String categoryId;

	@Schema(description = "price must be a positive number", example = "10.99")
	@PositiveOrZero(message = "price must be at least 0")
	private BigDecimal price;

	@Schema(description = "stock must be a positive number", example = "10")
	@PositiveOrZero(message = "stock must be at least 0")
	private Long stock;
}
