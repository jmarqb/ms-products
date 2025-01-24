package com.jmarqb.productsapi.infrastructure.adapters.input.rest.mapper;

import java.time.LocalDateTime;
import java.util.List;

import com.jmarqb.productsapi.domain.model.Product;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.request.CreateProductDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.request.PatchProductDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.response.PaginatedResponseDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.response.ProductResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {

	ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

	@Mapping(target = "categoryId", source = "createProductDto.categoryId")
	Product toDomain(CreateProductDto createProductDto);

	Product toDomain(PatchProductDto patchProductDto);

	@Mapping(target = "uid", source = "product.uid")
	ProductResponseDto toResponse(Product product);

	@Mapping(target = "data", source = "products")
	PaginatedResponseDto toPaginatedResponse(List<?> products, int total, int page, int size, LocalDateTime timestamp);
}
