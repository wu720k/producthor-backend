package com.producthor.Producthor.domain.mapper;

import com.producthor.Producthor.domain.dao.ProductDao;
import com.producthor.Producthor.domain.dto.ProductDto;
import com.producthor.Producthor.domain.model.Category;
import com.producthor.Producthor.domain.model.Product;

import java.util.HashMap;

public final class ProductMapper {
    private ProductMapper() {}

    public static Product toNewEntity(ProductDto dto, Category categoryOrNull) {
        if (dto == null) return null;

        var specifications = dto.getSpecifications() != null
                ? new HashMap<>(dto.getSpecifications())
                : new HashMap<String, String>();

        boolean available = dto.getAvailable() == null ? true : dto.getAvailable();

        return Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .imageUrl(dto.getImageUrl())
                .category(categoryOrNull)
                .available(available)
                .specifications(specifications)
                .build();
    }

    /** Update: ha a DTO-ban egy mező null, NEM írjuk felül. */
    public static void updateEntityFromDto(ProductDto dto, Product entity, Category categoryOrNull) {
        if (dto == null || entity == null) return;

        if (dto.getName() != null)        entity.setName(dto.getName());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        if (dto.getPrice() != null)       entity.setPrice(dto.getPrice());
        if (dto.getImageUrl() != null)    entity.setImageUrl(dto.getImageUrl());

        if (dto.getAvailable() != null)   entity.setAvailable(dto.getAvailable());

        if (dto.getCategoryId() != null)  entity.setCategory(categoryOrNull);

        if (dto.getSpecifications() != null) {
            entity.getSpecifications().clear();
            entity.getSpecifications().putAll(dto.getSpecifications());
        }
    }

    public static ProductDao toDao(Product entity) {
        if (entity == null) return null;
        return ProductDao.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .imageUrl(entity.getImageUrl())
                .specifications(entity.getSpecifications())
                .categoryId(entity.getCategory() != null ? entity.getCategory().getId() : null)
                .available(entity.isAvailable())
                .build();
    }
}
