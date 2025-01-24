package com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.request;

import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for partially updating a category.
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Schema(description = "DTO for partially updating a category")
public class PatchCategoryDto {

	@JsonIgnore
	@Schema(hidden = true)
	private String uid;

	@Schema(description = "name must be between 3 and 30 characters", example = "clothes")
	@Size(min = 3, max = 30, message = "name must be between 3 and 30 characters")
	private String name;

	@Schema(description = "description must be between 3 and 50 characters", example = "clothes description")
	@Size(min = 3, max = 50, message = "description must be between 3 and 50 characters")
	private String description;
}
