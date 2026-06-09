package com.atbm.appvppbe.service;

import com.atbm.appvppbe.dto.entity.Order;
import com.atbm.appvppbe.dto.entity.User;
import com.atbm.appvppbe.dto.request.OrderReq;
import com.atbm.appvppbe.repository.OrderRep;
import com.atbm.appvppbe.repository.UserRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderSer {
    private final OrderRep rep;
    private final UserRep userRep;

    public Order saveOrder(OrderReq req) {
        // Check User
        User user = userRep.findById(req.getUserId()).orElse(null);

        Order saveOrder = new Order();
        saveOrder.setUser(user);
        rep.save(saveOrder);
        return saveOrder;
    }
}
