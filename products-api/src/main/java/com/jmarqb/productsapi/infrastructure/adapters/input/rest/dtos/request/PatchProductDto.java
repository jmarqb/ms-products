package com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.request;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for partially updating a product.
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Schema(description = "DTO for partially updating a product")
public class PatchProductDto {

	@JsonIgnore
	@Schema(hidden = true)
	private String uid;

	@Schema(description = "Optional name update", example = "Updated T-shirt")
	@Size(min = 3, max = 30, message = "name must be between 3 and 30 characters")
	private String name;

	@Schema(description = "Optional description update", example = "Updated T-shirt Description")
	@Size(min = 3, max = 50, message = "description must be between 3 and 50 characters")
	private String description;

	@Schema(description = "Optional price update", example = "15.99")
	@PositiveOrZero(message = "price must be at least 0")
	private BigDecimal price;

	@Schema(description = "Optional stock update", example = "20")
	@PositiveOrZero(message = "stock must be at least 0")
	private Long stock;
}
