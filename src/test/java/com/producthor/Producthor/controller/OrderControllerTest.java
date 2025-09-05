package com.producthor.Producthor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.producthor.Producthor.domain.dto.OrderDto;
import com.producthor.Producthor.domain.dto.OrderItemDto;
import com.producthor.Producthor.domain.dto.ShippingDataDto;
import com.producthor.Producthor.domain.model.Product;
import com.producthor.Producthor.domain.model.ShippingData;
import com.producthor.Producthor.repository.ProductRepository;
import com.producthor.Producthor.repository.ShippingDataRepository;
import com.producthor.Producthor.rest.response.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class OrderControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper om;
    @Autowired private ProductRepository productRepository;
    @Autowired private ShippingDataRepository shippingDataRepository;

    private Product p1, p2;
    private ShippingData sd;

    @BeforeEach
    void initData() {
        productRepository.deleteAll();
        shippingDataRepository.deleteAll();

        p1 = productRepository.save(Product.builder().name("P1").price(new BigDecimal("1000")).available(true).build());
        p2 = productRepository.save(Product.builder().name("P2").price(new BigDecimal("2500")).available(true).build());
        sd = shippingDataRepository.save(ShippingData.builder()
                .postalCode("4400").city("Nyíregyháza").street("Kakas").houseNumber("5").build());
    }

    @Test
    void create_with_shippingDataId_returns_OrderResponse_and_total() throws Exception {
        OrderDto dto = new OrderDto();
        dto.setItems(List.of(new OrderItemDto(p1.getId(), 2), new OrderItemDto(p2.getId(), 1)));
        dto.setShippingDataId(sd.getId());

        var resp = mvc.perform(post("/api/order/public/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        OrderResponse body = om.readValue(resp.getContentAsByteArray(), OrderResponse.class);

        assertThat(body.getBaseResponse().getCode()).isEqualTo("OK");
        assertThat(body.getOrder()).isNotNull();
        assertThat(body.getOrder().getItems()).hasSize(2);
        assertThat(body.getOrder().getTotalGross()).isEqualByComparingTo("4500");
        assertThat(body.getOrder().getShippingData().getCity()).isEqualTo("Nyíregyháza");
    }

    @Test
    void create_with_inline_shippingData_also_works() throws Exception {
        OrderDto dto = new OrderDto();
        dto.setItems(List.of(new OrderItemDto(p1.getId(), 3)));
        dto.setShippingData(new ShippingDataDto(
                "1111","Bp","Fő","1.",""
        ));

        var resp = mvc.perform(post("/api/order/public/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        OrderResponse body = om.readValue(resp.getContentAsByteArray(), OrderResponse.class);
        assertThat(body.getBaseResponse().getCode()).isEqualTo("OK");
        assertThat(body.getOrder().getTotalGross()).isEqualByComparingTo("3000");
        assertThat(body.getOrder().getShippingData().getCity()).isEqualTo("Bp");
    }
}
