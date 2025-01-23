package com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;
import java.util.List;
/**
 * DTO for paginated responses.
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Paginated response")
public class PaginatedResponseDto {

    @Schema(description = "total number of elements", example = "10")
    private int total;

    @Schema(description = "page number", example = "0")
    private int page;

    @Schema(description = "page size", example = "20")
    private int size;

    @Schema(description = "data", example = "[]")
    private List<?> data;

    @Schema(description = "timestamp", example = "2021-01-01T00:00:00.000Z")
    private Date timestamp;
}
