package com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
/**
 * Delete response.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Delete response")
public class DeleteResponseDto {
    @Schema(description = "Acknowledged", example = "true")
    private boolean acknowledged;

    @Schema(description = "Deleted count", example = "1")
    private int deletedCount;
}
