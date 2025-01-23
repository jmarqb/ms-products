package com.jmarqb.productsapi.infrastructure.adapters.output.persistence.mapper;

import com.jmarqb.productsapi.domain.model.Category;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.model.CategoryEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryPersistenceMapper {

    CategoryEntity toEntity(Category category);

    Category toDomain(CategoryEntity categoryEntity);

    List<Category> toCategoryList(List<CategoryEntity> categoryEntities);


}
