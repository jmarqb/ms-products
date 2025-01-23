package com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

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

    @Schema(description = "name must be between 3 and 30 characters",example = "T-shirt")
    @NotBlank
    @Size(min = 3, max = 30, message = "name must be between 3 and 30 characters")
    private String name;

    @Schema(description = "description must be between 3 and 50 characters",example = "T-shirt Description")
    @NotBlank
    @Size(min = 3, max = 50, message = "description must be between 3 and 50 characters")
    private String description;

    @Schema(description = "price must be a positive number", example = "10.99")
    @PositiveOrZero(message = "price must be at least 0")
    private BigDecimal price;

    @Schema(description = "stock must be a positive number", example = "10")
    @PositiveOrZero(message = "stock must be at least 0")
    private Long stock;
}
