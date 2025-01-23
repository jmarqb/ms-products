package com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.request;

import com.jmarqb.productsapi.application.service.enums.Sort;
import com.jmarqb.productsapi.application.service.validation.ValueOfEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SearchBodyDto {

    @Schema(description = "string to search",example = "abc")
    private String search;

    @Schema(description = "page number",example = "0")
    private int page = 0;

    @Schema(description = "page size",example = "20")
    @Min(value = 1, message = "Size must be at least 1")
    private int size = 20;

    @Schema(description = "sort",example = "ASC")
    @ValueOfEnum(enumClass = Sort.class,message = "Sort must be [ASC|DESC]")
    private String sort;

}
