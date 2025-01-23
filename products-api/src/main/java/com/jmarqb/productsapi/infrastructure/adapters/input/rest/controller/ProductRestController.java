package com.jmarqb.productsapi.infrastructure.adapters.input.rest.controller;

import com.jmarqb.productsapi.application.ports.input.ProductUseCase;
import com.jmarqb.productsapi.domain.model.Product;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.request.CreateProductDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.request.PatchProductDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.request.SearchBodyDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.response.DeleteResponseDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.response.PaginatedResponseDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.response.ProductResponseDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.mapper.ProductMapper;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.ui.ProductRestUI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ProductRestController implements ProductRestUI {

    private final ProductUseCase productUseCase;

    private final ProductMapper productMapper;

    public ProductRestController(ProductUseCase productUseCase, ProductMapper productMapper) {
        this.productUseCase = productUseCase;
        this.productMapper = productMapper;
    }

    @Override
    public ResponseEntity<ProductResponseDto> create(CreateProductDto createProductDto) {
        Product product = productUseCase.save(productMapper.toDomain(createProductDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.toResponse(product));
    }

    @Override
    public ResponseEntity<PaginatedResponseDto> search(SearchBodyDto searchBodyDto) {
        List<Product> productList = productUseCase.search(searchBodyDto.getSearch(), searchBodyDto.getPage(),
                searchBodyDto.getSize(), searchBodyDto.getSort());

        return ResponseEntity.status(HttpStatus.OK).body(productMapper.toPaginatedResponse(productList, productList.size(),
                searchBodyDto.getPage(), searchBodyDto.getSize(), LocalDateTime.now()));
    }

    @Override
    public ResponseEntity<PaginatedResponseDto> searchProductsByCategory(String categoryUid, Integer page, Integer size, String sort) {
        List<Product> productList = productUseCase.searchByCategory(categoryUid, page, size, sort);
        return ResponseEntity.status(HttpStatus.OK).body(productMapper.toPaginatedResponse(productList, productList.size(), page, size, LocalDateTime.now()));
    }

    @Override
    public ResponseEntity<ProductResponseDto> findProduct(String uid) {
        return ResponseEntity.status(HttpStatus.OK).body(productMapper.toResponse(productUseCase.findProduct(uid)));
    }

    @Override
    public ResponseEntity<ProductResponseDto> updateProduct(String uid, PatchProductDto patchProductDto) {
       patchProductDto.setUid(uid);
       Product product = productUseCase.updateProduct(productMapper.toDomain(patchProductDto));
       return ResponseEntity.status(HttpStatus.OK).body(productMapper.toResponse(product));
    }

    @Override
    public ResponseEntity<DeleteResponseDto> removeProduct(String uid) {
        productUseCase.deleteProduct(uid);
        return new ResponseEntity<>(new DeleteResponseDto(true, 1), HttpStatus.OK);
    }
}
