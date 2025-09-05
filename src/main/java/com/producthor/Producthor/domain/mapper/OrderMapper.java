package com.producthor.Producthor.domain.mapper;

import com.producthor.Producthor.domain.dao.OrderDao;
import com.producthor.Producthor.domain.dao.OrderItemDao;
import com.producthor.Producthor.domain.model.Order;

import java.util.stream.Collectors;

public final class OrderMapper {
    private OrderMapper(){}

    public static OrderDao toDao(Order order) {
        if (order == null) return null;
        return OrderDao.builder()
                .id(order.getId())
                .items(order.getItems().stream()
                        .map(oi -> OrderItemDao.builder()
                                .id(oi.getId())
                                .product(ProductMapper.toDao(oi.getProduct()))
                                .quantity(oi.getQuantity())
                                .unitPriceAtPurchase(oi.getUnitPriceAtPurchase())
                                .build())
                        .collect(Collectors.toList()))
                .totalGross(order.getTotalGross())
                .shippingData(ShippingDataMapper.toDao(order.getShippingData()))
                .createdAt(order.getCreatedAt())
                .username(order.getUser() != null ? (order.getUser().getName().isBlank() ? order.getUser().getUsername() : order.getUser().getName()) : null)
                .build();
    }
}
