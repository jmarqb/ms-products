package com.jmarqb.productsapi.infrastructure.adapters.input.rest.controller;

import com.jmarqb.productsapi.application.ports.input.CategoryUseCase;
import com.jmarqb.productsapi.domain.model.Category;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.request.CreateCategoryDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.request.PatchCategoryDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.request.SearchBodyDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.response.CategoryResponseDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.response.DeleteResponseDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.dtos.response.PaginatedResponseDto;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.mapper.CategoryMapper;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.ui.CategoryRestUI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Controller
public class CategoryRestController implements CategoryRestUI {

    private final CategoryUseCase categoryUseCase;

    private final CategoryMapper categoryMapper;

    public CategoryRestController(CategoryUseCase categoryUseCase, CategoryMapper categoryMapper) {
        this.categoryUseCase = categoryUseCase;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public ResponseEntity<CategoryResponseDto> create(CreateCategoryDto createcategoryDto) {
        Category category = categoryUseCase.save(categoryMapper.toDomain(createcategoryDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryMapper.toResponse(category));
    }

    @Override
    public ResponseEntity<PaginatedResponseDto> search(SearchBodyDto searchBodyDto) {
        List<Category> categoryList = categoryUseCase.search(searchBodyDto.getSearch(), searchBodyDto.getPage(),
                searchBodyDto.getSize(), searchBodyDto.getSort());

        List<CategoryResponseDto> response = new ArrayList<>();
        categoryList.forEach(category -> response.add(categoryMapper.toResponse(category)));

        return ResponseEntity.status(HttpStatus.OK).body(categoryMapper.toPaginatedResponse(response, categoryList.size(),
                searchBodyDto.getPage(), searchBodyDto.getSize(), LocalDateTime.now()));
    }

    @Override
    public ResponseEntity<CategoryResponseDto> findCategory(String uid) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryMapper.toResponse(categoryUseCase.findCategory(uid)));
    }

    @Override
    public ResponseEntity<CategoryResponseDto> findCategoryByProduct(String productUid) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryMapper.toResponse(categoryUseCase.findCategoryByProductId(productUid)));
    }

    @Override
    public ResponseEntity<CategoryResponseDto> updateCategory(String uid, PatchCategoryDto patchCategoryDto) {
        patchCategoryDto.setUid(uid);
        Category category = categoryUseCase.updateCategory((categoryMapper.toDomain(patchCategoryDto)));
        return ResponseEntity.status(HttpStatus.OK).body(categoryMapper.toResponse(category));
    }

    @Override
    public ResponseEntity<DeleteResponseDto> removeCategory(String uid) {
        categoryUseCase.deleteCategory(uid);
        return new ResponseEntity<>(new DeleteResponseDto(true, 1), HttpStatus.OK);
    }
}
