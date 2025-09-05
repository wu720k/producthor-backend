package com.producthor.Producthor.domain.mapper;

import com.producthor.Producthor.domain.dao.CategoryDao;
import com.producthor.Producthor.domain.dto.CategoryDto;
import com.producthor.Producthor.domain.model.Category;

public final class CategoryMapper {
    private CategoryMapper() {}

    public static Category toNewEntity(CategoryDto dto) {
        if (dto == null) return null;
        return Category.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    public static void updateEntityFromDto(CategoryDto dto, Category entity) {
        if (dto == null || entity == null) return;
        if (dto.getName() != null)        entity.setName(dto.getName());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
    }

    public static CategoryDao toDao(Category entity) {
        if (entity == null) return null;
        return CategoryDao.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }
}
