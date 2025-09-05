package com.producthor.Producthor.domain.dao;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class UserDao {
    Long id;
    String username;
    String name;
    ShippingDataDao shippingData;
    List<OrderDao> orderHistory;
}
