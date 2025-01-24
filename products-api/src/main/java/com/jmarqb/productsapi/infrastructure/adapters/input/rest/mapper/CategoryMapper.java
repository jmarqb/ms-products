package com.jmarqb.productsapi.infrastructure.adapters.input.rest.mapper;

import java.time.LocalDateTime;
import java.util.List;

import com.jmarqb.productsapi.domain.model.Category;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.request.CreateCategoryDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.request.PatchCategoryDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.response.CategoryResponseDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.response.PaginatedResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

	CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

	Category toDomain(CreateCategoryDto createCategoryDto);

	Category toDomain(PatchCategoryDto patchCategoryDto);

	@Mapping(target = "uid", source = "category.uid")
	@Mapping(target = "products", source = "category.products")
	CategoryResponseDto toResponse(Category category);

	@Mapping(target = "data", source = "categories")
	PaginatedResponseDto toPaginatedResponse(List<?> categories, int total, int page, int size, LocalDateTime timestamp);
}
