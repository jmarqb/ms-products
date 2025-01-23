package com.jmarqb.productsapi.infrastructure.adapters.output.persistence.mapper;

import com.jmarqb.productsapi.domain.model.Product;
import com.jmarqb.productsapi.infrastructure.adapters.output.persistence.model.ProductEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductPersistenceMapper {

    ProductEntity toEntity(Product product);

    Product toDomain(ProductEntity productEntity);

    List<Product> toProductList(List<ProductEntity> entitiesList);
}
