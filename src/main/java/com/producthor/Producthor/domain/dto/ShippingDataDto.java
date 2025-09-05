package com.producthor.Producthor.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShippingDataDto {
    private String postalCode;
    private String city;
    private String street;
    private String houseNumber;
    private String additionalInfo;
}