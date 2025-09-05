package com.producthor.Producthor.controller;

import com.producthor.Producthor.domain.dao.CategoryDao;
import com.producthor.Producthor.rest.controller.CategoryController;
import com.producthor.Producthor.rest.response.BaseResponse;
import com.producthor.Producthor.rest.response.CategoriesResponse;
import com.producthor.Producthor.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CategoryController.class)
class CategoryControllerTest {

    @Autowired private MockMvc mvc;
    @MockBean private CategoryService service;

    @Test
    void public_getAll_returns_list() throws Exception {
        var resp = CategoriesResponse.builder()
                .categories(List.of(CategoryDao.builder().id(1L).name("Cat1").build()))
                .baseResponse(new BaseResponse("OK",""))
                .build();
        Mockito.when(service.getAll()).thenReturn(resp);

        mvc.perform(get("/api/category/public/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories[0].name").value("Cat1"));
    }

    @Test
    void admin_delete_returns_ok() throws Exception {
        Mockito.when(service.delete(5L)).thenReturn(new BaseResponse("OK",""));

        // ha a security filterek gondot okoznak: @AutoConfigureMockMvc(addFilters=false) vagy engedélyezz @WithMockUser
        mvc.perform(delete("/api/category/admin/delete/{id}", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("OK"));
    }
}
