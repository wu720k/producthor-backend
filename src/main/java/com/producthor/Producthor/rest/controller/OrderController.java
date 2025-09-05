package com.producthor.Producthor.rest.controller;

import com.producthor.Producthor.domain.dto.OrderDto;
import com.producthor.Producthor.rest.response.OrderResponse;
import com.producthor.Producthor.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/public/create")
    public OrderResponse create(@RequestBody OrderDto dto) {
        return orderService.create(dto);
    }
}
