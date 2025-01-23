package com.jmarqb.productsapi.infrastructure.adapters.input.rest.mapper;

import com.jmarqb.productsapi.domain.model.Category;
import com.jmarqb.productsapi.domain.model.Product;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.request.CreateCategoryDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.request.CreateProductDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.request.PatchCategoryDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.request.PatchProductDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.response.CategoryResponseDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.response.PaginatedResponseDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.response.ProductResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    Product toDomain(CreateProductDto createProductDto);

    Product toDomain(PatchProductDto patchProductDto);

    ProductResponseDto toResponse(Product product);

    @Mapping(target = "data",source = "products")
    PaginatedResponseDto toPaginatedResponse(List<?> products, int total, int page, int size, LocalDateTime timestamp);
}
