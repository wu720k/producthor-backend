package com.producthor.Producthor.domain.dto;

import lombok.Data;

@Data
public class UserDto {
    private String name;
    private ShippingDataDto shippingData;
}