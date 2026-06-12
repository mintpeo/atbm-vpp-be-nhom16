package com.atbm.appvppbe.service;

import com.atbm.appvppbe.AlgorithmSignature.DSA.DSA;
import com.atbm.appvppbe.dto.entity.Order;
import com.atbm.appvppbe.dto.entity.OrderItem;
import com.atbm.appvppbe.dto.entity.User;
import com.atbm.appvppbe.dto.request.OrderItemReq;
import com.atbm.appvppbe.dto.request.OrderReq;
import com.atbm.appvppbe.dto.request.SignReq;
import com.atbm.appvppbe.dto.request.VerifySignReq;
import com.atbm.appvppbe.repository.OrderItemRep;
import com.atbm.appvppbe.repository.OrderRep;
import com.atbm.appvppbe.repository.SignatureRep;
import com.atbm.appvppbe.dto.entity.Signature;
import com.atbm.appvppbe.repository.UserRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SignatureSer {
    private final SignatureRep rep;
    private final OrderRep orderRep;
    private final OrderItemRep orderItemRep;
    private final UserRep userRep;
    private final ObjectMapper objectMapper;

    // Verify
    public boolean verify(VerifySignReq req) throws Exception {
        Order order = orderRep.findById(req.getOrderId()).orElse(null);
        if (order == null) return false;

        List<OrderItem> orderItem = orderItemRep.findByOrderId(order.getId());
        List<OrderItemReq> orderItemReqs = new ArrayList<>();
        for (OrderItem o : orderItem) {
            OrderItemReq orderItemReq = new OrderItemReq();
            orderItemReq.setProductId(o.getProductId());
            orderItemReq.setType(o.getType());
            orderItemReq.setPrice(o.getPrice());
            orderItemReq.setQuantity(o.getQuantity());

            orderItemReqs.add(orderItemReq);
        }

        User user = userRep.findById(order.getUser().getId()).orElse(null);
        if (user == null) return false;

        Signature signature = rep.findByOrderId(order.getId()).orElse(null);
        if (signature == null) return false;

        SignReq signReq = new SignReq();
        signReq.setOrderId(order.getId());
        signReq.setUserId(order.getUser().getId());
        signReq.setTotalPrice(order.getTotalPrice());
        signReq.setItems(orderItemReqs);

        String data = objectMapper.writeValueAsString(signReq);
        DSA dsa = new DSA();
        return dsa.verify(user.getPublicKey(), data, signature.getSignature());
    }

    // Signature
    public void saveSignature(OrderReq req) throws Exception {
        // Check User
        User user = userRep.findById(req.getUserId()).orElse(null);

        // signature success => save database
        // Save on database
        // Order
        Order saveOrder = new Order();
        saveOrder.setUser(user);
        saveOrder.setTotalPrice(req.getTotalPrice());
        Order order = orderRep.save(saveOrder);

        // Order Item
        for (OrderItemReq orderItemReq : req.getItems()) {
            OrderItem saveOrderItem = new OrderItem();
            saveOrderItem.setOrder(order);
            saveOrderItem.setProductId(orderItemReq.getProductId());
            saveOrderItem.setPrice(orderItemReq.getPrice());
            saveOrderItem.setType(orderItemReq.getType());
            saveOrderItem.setQuantity(orderItemReq.getQuantity());

            orderItemRep.save(saveOrderItem);
        }

        SignReq signReq = new SignReq();
        signReq.setOrderId(order.getId());
        signReq.setUserId(order.getUser().getId());
        signReq.setTotalPrice(order.getTotalPrice());
        signReq.setItems(req.getItems());

        // Object => Json Text
        String orderText = objectMapper.writeValueAsString(signReq);

        // Signature
        DSA dsa = new DSA();
        String signature = dsa.sign(orderText, req.getPrivateKey());

        Signature saveSignature = new Signature();
        saveSignature.setOrder(order);
        saveSignature.setSignature(signature);
        rep.save(saveSignature);
    }
}
