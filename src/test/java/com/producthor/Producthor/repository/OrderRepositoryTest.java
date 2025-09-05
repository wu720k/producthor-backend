package com.producthor.Producthor.repository;

import com.producthor.Producthor.domain.model.Order;
import com.producthor.Producthor.domain.model.ShippingData;
import com.producthor.Producthor.domain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShippingDataRepository shippingDataRepository;

    @Test
    void saveAndFindOrder() {
        User user = userRepository.save(User.builder()
                .username("testuser")
                .password("pass")
                .build());

        ShippingData ship = shippingDataRepository.save(ShippingData.builder()
                .city("Budapest")
                .postalCode("1111")
                .street("Fo utca")
                .houseNumber("1")
                .build());

        Order order = Order.builder()
                .user(user)
                .shippingData(ship)
                .totalGross(BigDecimal.valueOf(12345))
                .createdAt(OffsetDateTime.now())
                .build();

        order = orderRepository.save(order);

        var found = orderRepository.findById(order.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getUser().getUsername()).isEqualTo("testuser");
        assertThat(found.getShippingData().getCity()).isEqualTo("Budapest");
        assertThat(found.getTotalGross()).isEqualByComparingTo(BigDecimal.valueOf(12345));
    }
}
