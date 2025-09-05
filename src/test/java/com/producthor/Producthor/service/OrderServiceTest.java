package com.producthor.Producthor.service;

import com.producthor.Producthor.domain.dto.OrderDto;
import com.producthor.Producthor.domain.dto.OrderItemDto;
import com.producthor.Producthor.domain.dto.ShippingDataDto;
import com.producthor.Producthor.domain.model.*;
import com.producthor.Producthor.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class OrderServiceTest {

    @Autowired private OrderService orderService;
    @Autowired private ProductRepository productRepository;
    @Autowired private ShippingDataRepository shippingDataRepository;

    private Product prod1;
    private Product prod2;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        shippingDataRepository.deleteAll();

        prod1 = productRepository.save(Product.builder()
                .name("A")
                .price(new BigDecimal("1000"))
                .available(true)
                .build());

        prod2 = productRepository.save(Product.builder()
                .name("B")
                .price(new BigDecimal("2500"))
                .available(true)
                .build());
    }

    @Test
    void create_with_shippingDataId_calculates_total_and_items() {
        var ship = shippingDataRepository.save(ShippingData.builder()
                .postalCode("4400").city("Nyíregyháza").street("Kakas").houseNumber("5").build());

        OrderDto dto = new OrderDto();
        dto.setItems(List.of(
                new OrderItemDto(prod1.getId(), 2),
                new OrderItemDto(prod2.getId(), 1)
        ));
        dto.setShippingDataId(ship.getId());

        var res = orderService.create(dto);

        assertThat(res.getBaseResponse().getCode()).isEqualTo("OK");
        assertThat(res.getOrder()).isNotNull();
        assertThat(res.getOrder().getItems()).hasSize(2);
        assertThat(res.getOrder().getTotalGross()).isEqualByComparingTo("4500"); // 2*1000 + 1*2500
        assertThat(res.getOrder().getShippingData().getCity()).isEqualTo("Nyíregyháza");
    }

    @Test
    void create_with_new_shippingData_builds_shipping_and_total() {
        OrderDto dto = new OrderDto();
        dto.setItems(List.of(new OrderItemDto(prod1.getId(), 3)));
        dto.setShippingData(new ShippingDataDto("4400","Nyh","Utca","10.",""));

        var res = orderService.create(dto);

        assertThat(res.getBaseResponse().getCode()).isEqualTo("OK");
        assertThat(res.getOrder().getTotalGross()).isEqualByComparingTo("3000");
        assertThat(res.getOrder().getShippingData().getHouseNumber()).isEqualTo("10.");
    }

    @Test
    void create_fails_when_product_not_found() {
        OrderDto dto = new OrderDto();
        dto.setItems(List.of(new OrderItemDto(999999L, 1)));
        dto.setShippingData(new ShippingDataDto("4400","Nyh","U","1.",""));

        var res = orderService.create(dto);

        assertThat(res.getBaseResponse().getCode()).isEqualTo("ERROR");
        assertThat(res.getOrder()).isNull();
    }
}
