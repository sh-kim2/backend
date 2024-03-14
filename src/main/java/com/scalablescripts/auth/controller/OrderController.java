package com.scalablescripts.auth.controller;


import com.scalablescripts.auth.data.Order;
import com.scalablescripts.auth.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
class OrderController {

    private final OrderService orderService;
    private final Integer refreshTokenValidity;

    @Autowired
    OrderController(OrderService orderService, @Value("${application.security.refresh-token-validity}") Integer refreshTokenValidity) {
        this.orderService = orderService;
        this.refreshTokenValidity = refreshTokenValidity;
    }

    record OrderResponse(List<Order> orders, HttpStatus httpStatus) {}

    @GetMapping("/orders")
    public OrderResponse getOrder(
            @CookieValue(value = "refresh_token", required = false) String token
    ) {
//        if (!jwtService.isValid(token)) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//        }

        List<Order> orders = orderService.getOrder(token);

        return new OrderResponse(orders, HttpStatus.OK);
    }



    record PushOrderResponse(HttpStatus httpStatus) {}
    record PushOrderRequest(String name, String address, String payment, String cardNumber, String items) {}

    @Transactional
    @PostMapping("/orders")
    public PushOrderResponse pushOrder(
            @RequestBody PushOrderRequest pushOrderRequest,
            @CookieValue(value = "refresh_token", required = false) String token
    ) {
//        if (!jwtService.isValid(token)) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//        }

        orderService.pushOrders(
                pushOrderRequest.name(),
                pushOrderRequest.address(),
                pushOrderRequest.payment(),
                pushOrderRequest.cardNumber(),
                pushOrderRequest.items(),
                token);

        return new PushOrderResponse(HttpStatus.OK);
    }


}
