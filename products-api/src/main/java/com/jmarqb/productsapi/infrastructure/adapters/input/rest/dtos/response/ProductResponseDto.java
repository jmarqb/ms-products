package com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.response;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * ProductResponseDto
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Schema(description = "ProductResponseDto")
public class ProductResponseDto {

	@Schema(description = "product id", example = "7b2f4f93-c450-4f2f-9c6f-d3c2f39f9a8c")
	private String uid;

	@Schema(description = "product name", example = "T-shirt")
	private String name;

	@Schema(description = "product description", example = "T-shirt description")
	private String description;

	@Schema(description = "product price" , example = "10.99")
	private BigDecimal price;


	@Schema(description = "product stock", example = "10")
	private Long stock;


	@Schema(description = "product category" , example = "8b2f4f93-c650-4f2f-9c6f-d3c2f39f7a8c")
	private String categoryId;


	@Schema(description = "deleted", example = "false")
	private boolean deleted;


	@Schema(description = "deletedAt", example = "null")
	private Date deletedAt;
}
