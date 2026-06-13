package com.atbm.appvppbe.service;

import com.atbm.appvppbe.dto.entity.Order;
import com.atbm.appvppbe.dto.entity.OrderItem;
import com.atbm.appvppbe.repository.OrderItemRep;
import com.atbm.appvppbe.repository.OrderRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderSer {
    private final OrderRep rep;
    private final OrderItemRep orderItemRep;

    public List<Order> showOrderUser(long userId) {
        return rep.findByUserId(userId);
    }

    public List<OrderItem> showOrderItemUser(long orderId) {
        return orderItemRep.findByOrderId(orderId);
    }
}
