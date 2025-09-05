package com.producthor.Producthor.rest.controller;

import com.producthor.Producthor.domain.dto.ProductDto;
import com.producthor.Producthor.rest.response.BaseResponse;
import com.producthor.Producthor.rest.response.ProductResponse;
import com.producthor.Producthor.rest.response.ProductsResponse;
import com.producthor.Producthor.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/admin/create")
    public BaseResponse create(@RequestBody ProductDto dto) {
        return productService.create(dto);
    }

    @PutMapping("/admin/update/{id}")
    public BaseResponse update(@PathVariable Long id, @RequestBody ProductDto dto) {
        return productService.update(id, dto);
    }

    @GetMapping("/public/get")
    public ProductResponse getById(@RequestParam("id") Long id) {
        return productService.getById(id);
    }

    @GetMapping("/public/byCategory")
    public ProductsResponse getByCategory(@RequestParam("categoryId") Long categoryId) {
        return productService.getByCategory(categoryId);
    }

    @GetMapping("/public/availables")
    public ProductsResponse getAvailables() {
        return productService.getAvailables();
    }

    @GetMapping("/public/all")
    public ProductsResponse getAll() {
        return productService.getAll();
    }

}
