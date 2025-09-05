package com.producthor.Producthor.domain.dao;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ShippingDataDao {
    Long id;
    String postalCode;
    String city;
    String street;
    String houseNumber;
    String additionalInfo;
}
