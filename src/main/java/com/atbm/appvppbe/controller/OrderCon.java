package com.atbm.appvppbe.controller;

import com.atbm.appvppbe.dto.entity.Order;
import com.atbm.appvppbe.dto.entity.OrderItem;
import com.atbm.appvppbe.service.OrderSer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class OrderCon {
    private final OrderSer ser;

    @PostMapping("/show")
    public List<Order> showOrderUser(@RequestBody long userId) {
        return ser.showOrderUser(userId);
    }

    @GetMapping("/detail")
    public List<OrderItem> showOrderItem(@RequestParam long orderId) {
        return ser.showOrderItemUser(orderId);
    }
}
