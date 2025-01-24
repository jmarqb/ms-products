package com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.response;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.jmarqb.productsapi.domain.model.Product;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * CategoryResponseDto
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Schema(description = "CategoryResponseDto")
public class CategoryResponseDto {

	@Schema(description = "category id", example = "7b2f4f93-c450-4f2f-9c6f-d3c2f39f9a8c")
	private String uid;
	@Schema(description = "category name", example = "clothes")
	private String name;
	@Schema(description = "category description", example = "clothes description")
	private String description;

	@Schema(description = "deleted", example = "false")
	private boolean deleted;

	@Schema(description = "deletedAt", example = "null")
	private Date deletedAt;

	private List<Product> products;
}
