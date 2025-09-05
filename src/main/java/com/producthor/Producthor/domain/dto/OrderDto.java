package com.producthor.Producthor.domain.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderDto {
    private List<OrderItemDto> items;
    private Long shippingDataId;
    private ShippingDataDto shippingData;
}
