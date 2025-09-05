package com.producthor.Producthor.domain.dao;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class OrderItemDao {
    Long id;
    //OrderDao order;
    ProductDao product;
    Integer quantity;
    BigDecimal unitPriceAtPurchase;
}
