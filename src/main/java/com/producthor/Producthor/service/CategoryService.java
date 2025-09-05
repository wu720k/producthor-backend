package com.producthor.Producthor.service;

import com.producthor.Producthor.domain.dto.CategoryDto;
import com.producthor.Producthor.domain.mapper.CategoryMapper;
import com.producthor.Producthor.domain.model.Category;
import com.producthor.Producthor.repository.CategoryRepository;
import com.producthor.Producthor.repository.ProductRepository;
import com.producthor.Producthor.rest.response.BaseResponse;
import com.producthor.Producthor.rest.response.CategoriesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Transactional
    public BaseResponse create(CategoryDto dto) {
        if (dto == null || dto.getName() == null || dto.getName().isBlank()) {
            return new BaseResponse("ERROR", "Name is required");
        }
        if (categoryRepository.existsByName(dto.getName())) {
            return new BaseResponse("ERROR", "Category name already exists");
        }
        Category entity = CategoryMapper.toNewEntity(dto);
        categoryRepository.save(entity);
        return new BaseResponse("OK", "");
    }

    @Transactional
    public BaseResponse update(Long categoryId, CategoryDto dto) {
        var catOpt = categoryRepository.findById(categoryId);
        if (catOpt.isEmpty()) {
            return new BaseResponse("ERROR", "Category not found");
        }

        if (dto.getName() != null && !dto.getName().isBlank()) {
            boolean exists = categoryRepository.existsByName(dto.getName());
            if (exists && !dto.getName().equals(catOpt.get().getName())) {
                return new BaseResponse("ERROR", "Category name already exists");
            }
        }

        var cat = catOpt.get();
        CategoryMapper.updateEntityFromDto(dto, cat);
        categoryRepository.save(cat);
        return new BaseResponse("OK", "");
    }

    @Transactional
    public BaseResponse delete(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            return new BaseResponse("ERROR", "Category not found");
        }

        productRepository.clearCategoryFromProducts(categoryId);
        categoryRepository.deleteById(categoryId);
        return new BaseResponse("OK", "");
    }

    @Transactional(readOnly = true)
    public CategoriesResponse getAll() {
        var list = categoryRepository.findAll().stream()
                .map(CategoryMapper::toDao)
                .collect(Collectors.toList());

        return CategoriesResponse.builder()
                .categories(list)
                .baseResponse(new BaseResponse("OK", ""))
                .build();
    }
}
