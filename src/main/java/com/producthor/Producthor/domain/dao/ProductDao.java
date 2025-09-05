package com.producthor.Producthor.domain.dao;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Map;

@Value
@Builder
public class ProductDao {
    Long id;
    String name;
    String description;
    BigDecimal price;
    String imageUrl;
    Map<String, String> specifications;
    Boolean available;
    Long categoryId;
}
