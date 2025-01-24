package com.jmarqb.productsapi.infrastructure.adapters.output.persistence.mapper;

import java.util.List;

import com.jmarqb.productsapi.domain.model.Product;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.model.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CategoryMapperHelper.class})
public interface ProductPersistenceMapper {

	@Mapping(target = "category", source = "categoryId")
	ProductEntity toEntity(Product product);

	@Mapping(target = "categoryId", source = "category.uid")
	Product toDomain(ProductEntity productEntity);

	List<Product> toProductList(List<ProductEntity> entitiesList);
}
