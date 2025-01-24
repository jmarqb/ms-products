package com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for creating a new category.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Schema(description = "DTO for creating a new category.")
public class CreateCategoryDto {

	@Schema(description = "name must be between 3 and 30 characters", example = "clothes")
	@NotBlank
	@Size(min = 3, max = 30, message = "name must be between 3 and 30 characters")
	private String name;

	@Schema(description = "description must be between 3 and 50 characters", example = "clothes description")
	@NotBlank
	@Size(min = 3, max = 50, message = "description must be between 3 and 50 characters")
	private String description;
}
