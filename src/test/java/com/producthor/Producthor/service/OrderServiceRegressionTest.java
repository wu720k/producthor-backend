package com.producthor.Producthor.service;

import com.producthor.Producthor.domain.dto.OrderDto;
import com.producthor.Producthor.domain.dto.OrderItemDto;
import com.producthor.Producthor.domain.model.Product;
import com.producthor.Producthor.domain.model.ShippingData;
import com.producthor.Producthor.repository.ProductRepository;
import com.producthor.Producthor.repository.ShippingDataRepository;
import com.producthor.Producthor.rest.response.OrderResponse;
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
class OrderServiceRegressionTest {

    @Autowired private OrderService orderService;
    @Autowired private ProductRepository productRepository;
    @Autowired private ShippingDataRepository shippingDataRepository;

    private Product prod;
    private ShippingData sharedSd;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        shippingDataRepository.deleteAll();

        prod = productRepository.save(Product.builder().name("P").price(new BigDecimal("100")).available(true).build());
        sharedSd = shippingDataRepository.save(ShippingData.builder()
                .postalCode("4400").city("Nyh").street("U").houseNumber("1").build());
    }

    @Test
    void multiple_orders_can_reference_same_shippingDataId() {
        OrderDto a = new OrderDto();
        a.setItems(List.of(new OrderItemDto(prod.getId(), 1)));
        a.setShippingDataId(sharedSd.getId());

        OrderDto b = new OrderDto();
        b.setItems(List.of(new OrderItemDto(prod.getId(), 2)));
        b.setShippingDataId(sharedSd.getId());

        OrderResponse ra = orderService.create(a);
        OrderResponse rb = orderService.create(b);

        assertThat(ra.getBaseResponse().getCode()).isEqualTo("OK");
        assertThat(rb.getBaseResponse().getCode()).isEqualTo("OK");
        assertThat(ra.getOrder().getId()).isNotNull();
        assertThat(rb.getOrder().getId()).isNotNull();
        assertThat(ra.getOrder().getShippingData().getId()).isEqualTo(sharedSd.getId());
        assertThat(rb.getOrder().getShippingData().getId()).isEqualTo(sharedSd.getId());
    }
}
