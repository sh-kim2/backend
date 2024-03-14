package com.scalablescripts.auth.controller;


import com.scalablescripts.auth.data.Item;
import com.scalablescripts.auth.service.AuthService;
import com.scalablescripts.auth.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
class ItemController {

    private final ItemService itemService;

    private final Integer refreshTokenValidity;

    @Autowired
    public ItemController(ItemService itemService, @Value("${application.security.refresh-token-validity}") Integer refreshTokenValidity) {
        this.itemService = itemService;
        this.refreshTokenValidity = refreshTokenValidity;
    }

    @GetMapping("/items")
    public List<Item> getItems() {
        var items = itemService.items();

        return items;
    }

}
