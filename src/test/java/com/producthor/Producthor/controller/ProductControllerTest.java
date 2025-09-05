package com.producthor.Producthor.controller;

import com.producthor.Producthor.domain.dao.ProductDao;
import com.producthor.Producthor.rest.controller.ProductController;
import com.producthor.Producthor.rest.response.BaseResponse;
import com.producthor.Producthor.rest.response.ProductResponse;
import com.producthor.Producthor.rest.response.ProductsResponse;
import com.producthor.Producthor.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    @Autowired private MockMvc mvc;
    @MockBean private ProductService productService;

    @Test
    void getAll_returns_products() throws Exception {
        var p = ProductDao.builder().id(1L).name("P").price(new BigDecimal("100")).available(true).build();
        var resp = ProductsResponse.builder()
                .products(List.of(p))
                .baseResponse(new BaseResponse("OK",""))
                .build();
        Mockito.when(productService.getAll()).thenReturn(resp);

        mvc.perform(get("/api/product/public/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.baseResponse.code").value("OK"))
                .andExpect(jsonPath("$.products[0].name").value("P"));
    }

    @Test
    void getById_returns_one() throws Exception {
        var p = ProductDao.builder().id(42L).name("X").price(new BigDecimal("1")).available(true).build();
        var resp = ProductResponse.builder()
                .product(p)
                .baseResponse(new BaseResponse("OK",""))
                .build();
        Mockito.when(productService.getById(42L)).thenReturn(resp);

        mvc.perform(get("/api/product/public/get").param("id","42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product.id").value(42));
    }
}
