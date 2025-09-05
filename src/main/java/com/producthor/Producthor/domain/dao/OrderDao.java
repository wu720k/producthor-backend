package com.producthor.Producthor.domain.dao;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Value
@Builder
public class OrderDao {
    Long id;
    //UserDao user;
    String username;
    List<OrderItemDao> items;
    BigDecimal totalGross;
    ShippingDataDao shippingData;
    OffsetDateTime createdAt;
}
