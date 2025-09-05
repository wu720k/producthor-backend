package com.producthor.Producthor.service;


import com.producthor.Producthor.domain.model.Product;
import com.producthor.Producthor.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProductServiceTest {

    @MockBean
    private ProductRepository productRepository;

    @Resource
    private ProductService productService;

    private Product sample;

    @BeforeEach
    void setup() {
        sample = Product.builder()
                .name("Test Product")
                .price(BigDecimal.valueOf(999))
                .available(true)
                .build();
    }

    @Test
    void getAllShouldReturnProducts() {
        when(productRepository.findAll()).thenReturn(List.of(sample));

        var res = productService.getAll();

        assertThat(res.getProducts()).hasSize(1);
        assertThat(res.getProducts().get(0).getName()).isEqualTo("Test Product");
    }

    @Test
    void getByIdShouldReturnProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sample));

        var res = productService.getById(1L);

        assertThat(res.getProduct()).isNotNull();
        assertThat(res.getProduct().getPrice()).isEqualByComparingTo("999");
    }

    @Test
    void getByIdShouldReturnErrorIfNotFound() {
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        var res = productService.getById(2L);

        assertThat(res.getBaseResponse().getCode()).isEqualTo("ERROR");
    }
}
