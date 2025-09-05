package com.producthor.Producthor.service;

import com.producthor.Producthor.domain.dto.OrderDto;
import com.producthor.Producthor.domain.dto.OrderItemDto;
import com.producthor.Producthor.domain.mapper.OrderMapper;
import com.producthor.Producthor.domain.mapper.ShippingDataMapper;
import com.producthor.Producthor.domain.model.Order;
import com.producthor.Producthor.domain.model.OrderItem;
import com.producthor.Producthor.domain.model.Product;
import com.producthor.Producthor.domain.model.ShippingData;
import com.producthor.Producthor.domain.model.User;
import com.producthor.Producthor.repository.*;
import com.producthor.Producthor.rest.response.BaseResponse;
import com.producthor.Producthor.rest.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ShippingDataRepository shippingDataRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderResponse create(OrderDto dto) {
        if (dto == null || dto.getItems() == null || dto.getItems().isEmpty()) {
            return OrderResponse.builder()
                    .baseResponse(new BaseResponse("ERROR", "Order items are required"))
                    .build();
        }

        User user = resolveAuthenticatedUserOrNull();

        ShippingData shippingData = null;
        if (dto.getShippingDataId() != null) {
            shippingData = shippingDataRepository.findById(dto.getShippingDataId()).orElse(null);
            if (shippingData == null) {
                return OrderResponse.builder()
                        .baseResponse(new BaseResponse("ERROR", "ShippingData not found"))
                        .build();
            }
        } else if (dto.getShippingData() != null) {
            ShippingData newEntity = ShippingDataMapper.toEntity(dto.getShippingData());
            shippingData = shippingDataRepository.save(newEntity);
        } else {
            return OrderResponse.builder()
                    .baseResponse(new BaseResponse("ERROR", "ShippingData is required"))
                    .build();
        }

        Order order = Order.builder()
                .user(user)
                .shippingData(shippingData)
                .totalGross(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;
        for (OrderItemDto itemDto : dto.getItems()) {
            if (itemDto.getProductId() == null || itemDto.getQuantity() == null || itemDto.getQuantity() < 1) {
                return OrderResponse.builder()
                        .baseResponse(new BaseResponse("ERROR", "Invalid item (productId/quantity)"))
                        .build();
            }

            Product product = productRepository.findById(itemDto.getProductId()).orElse(null);
            if (product == null) {
                return OrderResponse.builder()
                        .baseResponse(new BaseResponse("ERROR", "Product not found: " + itemDto.getProductId()))
                        .build();
            }

            BigDecimal unitPrice = product.getPrice();
            OrderItem item = OrderItem.builder()
                    .product(product)
                    .quantity(itemDto.getQuantity())
                    .unitPriceAtPurchase(unitPrice)
                    .build();

            order.addItem(item);
            total = total.add(unitPrice.multiply(BigDecimal.valueOf(itemDto.getQuantity())));
        }

        order.setTotalGross(total);
        Order saved = orderRepository.save(order);

        return OrderResponse.builder()
                .order(OrderMapper.toDao(saved))
                .baseResponse(new BaseResponse("OK", ""))
                .build();
    }


    private User resolveAuthenticatedUserOrNull() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return null;
        Object principal = auth.getPrincipal();
        if (principal == null || "anonymousUser".equals(principal)) return null;

        String username = auth.getName();
        return userRepository.findByUsername(username).orElse(null);
    }
}
