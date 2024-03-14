package com.scalablescripts.auth.controller;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.scalablescripts.auth.data.Cart;
import com.scalablescripts.auth.data.CartRepo;
import com.scalablescripts.auth.data.Item;
import com.scalablescripts.auth.data.ItemRepo;
import com.scalablescripts.auth.service.CartService;
import com.scalablescripts.auth.service.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
class CartController {

    private final CartService cartService;

    private final Integer refreshTokenValidity;

    @Autowired
    public CartController(CartService cartService, @Value("${application.security.refresh-token-validity}") Integer refreshTokenValidity) {
        this.cartService = cartService;
        this.refreshTokenValidity = refreshTokenValidity;
    }

    record CarItemResponse(List<Item> items, HttpStatus httpStatus) {}
    @GetMapping("/cart/items")
    public CarItemResponse getCartItems(@CookieValue(value = "refresh_token", required = false) String token) {

        var items = cartService.cartItems(token); //List<Item>


        return new CarItemResponse(items, HttpStatus.OK);
    }

    record PushCartItemResponse(HttpStatus httpStatus) {}
    record PushCartItemRequest(Long itemId) {}

    @PostMapping("/cart/items/{itemId}")
    public PushCartItemResponse pushCartItem(
            @RequestBody PushCartItemRequest pushCartItemRequest,
            @CookieValue("refresh_token") String refreshToken
    )  {


        cartService.pushCartItem(pushCartItemRequest.itemId, refreshToken);

        return new PushCartItemResponse(HttpStatus.OK);
    }



//    @PostMapping("/cart/items/{itemId}")
//    public ResponseEntity pushCartItem(
//            @PathVariable("itemId") int itemId,
//            @CookieValue(value = "refresh_token", required = false) String token
//    ) {
//        Long id = 10L;
//
//        cartService.pushCartItem(itemId, id);
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    record RemoveCartItemResponse(HttpStatus httpStatus) {}
    record RemoveCartItemRequest(Long itemId) {}

    @DeleteMapping("/cart/items/{itemId}")
    public RemoveCartItemResponse removeCartItem(
            @RequestBody RemoveCartItemRequest removeCartItemRequest,
            @CookieValue("refresh_token") String refreshToken
    ) {

        cartService.removeCartItem(removeCartItemRequest.itemId, refreshToken);

        return new RemoveCartItemResponse(HttpStatus.OK);
    }


}
