package com.producthor.Producthor.rest.controller;

import com.producthor.Producthor.domain.dto.CategoryDto;
import com.producthor.Producthor.rest.response.BaseResponse;
import com.producthor.Producthor.rest.response.CategoriesResponse;
import com.producthor.Producthor.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/public/getAll")
    public CategoriesResponse getAll() {
        return categoryService.getAll();
    }

    @PostMapping("/admin/create")
    public BaseResponse create(@RequestBody CategoryDto dto) {
        return categoryService.create(dto);
    }

    @PutMapping("/admin/update/{categoryId}")
    public BaseResponse update(@PathVariable Long categoryId,
                               @RequestBody CategoryDto dto) {
        return categoryService.update(categoryId, dto);
    }

    @DeleteMapping("/admin/delete/{categoryId}")
    public BaseResponse delete(@PathVariable Long categoryId) {
        return categoryService.delete(categoryId);
    }
}