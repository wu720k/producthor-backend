package com.producthor.Producthor.service;

import com.producthor.Producthor.domain.dto.ProductDto;
import com.producthor.Producthor.domain.mapper.ProductMapper;
import com.producthor.Producthor.domain.model.Category;
import com.producthor.Producthor.domain.model.Product;
import com.producthor.Producthor.repository.CategoryRepository;
import com.producthor.Producthor.repository.ProductRepository;
import com.producthor.Producthor.rest.response.BaseResponse;
import com.producthor.Producthor.rest.response.ProductResponse;
import com.producthor.Producthor.rest.response.ProductsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public BaseResponse create(ProductDto dto) {
        if (dto == null || dto.getName() == null || dto.getName().isBlank()) {
            return new BaseResponse("ERROR", "Product name is required");
        }
        if (dto.getPrice() == null) {
            return new BaseResponse("ERROR", "Price is required");
        }

        Category category = null;
        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId())
                    .orElse(null);
        }

        Product entity = ProductMapper.toNewEntity(dto, category);
        productRepository.save(entity);
        return new BaseResponse("OK", "");
    }

    @Transactional
    public BaseResponse update(Long productId, ProductDto dto) {
        var opt = productRepository.findById(productId);
        if (opt.isEmpty()) {
            return new BaseResponse("ERROR", "Product not found");
        }
        var entity = opt.get();

        Category category = null;
        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
        }

        ProductMapper.updateEntityFromDto(dto, entity, category);
        productRepository.save(entity);
        return new BaseResponse("OK", "");
    }

    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {
        var opt = productRepository.findById(id);
        if (opt.isEmpty()) {
            return ProductResponse.builder()
                    .product(null)
                    .baseResponse(new BaseResponse("ERROR", "Product not found"))
                    .build();
        }

        var dao = ProductMapper.toDao(opt.get());
        return ProductResponse.builder()
                .product(dao)
                .baseResponse(new BaseResponse("OK", ""))
                .build();
    }

    @Transactional(readOnly = true)
    public ProductsResponse getByCategory(Long categoryId) {
        var list = productRepository.findByCategoryIdAndAvailableTrue(categoryId).stream()
                .map(ProductMapper::toDao)
                .collect(Collectors.toList());

        return ProductsResponse.builder()
                .products(list)
                .baseResponse(new BaseResponse("OK", ""))
                .build();
    }

    @Transactional(readOnly = true)
    public ProductsResponse getAvailables() {
        var list = productRepository.findByAvailableTrue().stream()
                .map(ProductMapper::toDao)
                .collect(Collectors.toList());

        return ProductsResponse.builder()
                .products(list)
                .baseResponse(new BaseResponse("OK", ""))
                .build();
    }

    @Transactional(readOnly = true)
    public ProductsResponse getAll() {
        var list = productRepository.findAll().stream()
                .map(ProductMapper::toDao)
                .collect(Collectors.toList());

        return ProductsResponse.builder()
                .products(list)
                .baseResponse(new BaseResponse("OK", ""))
                .build();
    }
}
