package com.producthor.Producthor.domain.dao;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CategoryDao {
    Long id;
    String name;
    String description;
}