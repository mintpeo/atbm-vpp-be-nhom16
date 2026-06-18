package com.atbm.appvppbe.service;

import com.atbm.appvppbe.AlgorithmSignature.DSA.DSA;
import com.atbm.appvppbe.dto.entity.Order;
import com.atbm.appvppbe.dto.entity.OrderItem;
import com.atbm.appvppbe.dto.entity.User;
import com.atbm.appvppbe.dto.request.*;
import com.atbm.appvppbe.repository.OrderItemRep;
import com.atbm.appvppbe.repository.OrderRep;
import com.atbm.appvppbe.repository.SignatureRep;
import com.atbm.appvppbe.dto.entity.Signature;
import com.atbm.appvppbe.repository.UserRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
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

    // Verify (khong su dung)
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
        PublicKey publicKey = dsa.importPublicKey(user.getPublicKey());
        return dsa.verify(publicKey, data, signature.getSignature());
    }

    // Signature Again With File (khong su dung)
    public boolean checkSignatureFile(CheckSignatureFileReq req) {
        // Check User
        User user = userRep.findById(req.getUserId()).orElse(null);
        if (user == null) return false;

        // Order
        Order order = orderRep.findById(req.getOrderId()).orElse(null);
        if (order == null) return false;

        // Order Item
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

        try {
            FileInputStream keyFis = new FileInputStream(req.getFile());
            byte[] encKey = new byte[keyFis.available()];
            keyFis.read(encKey);
            keyFis.close();

            PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(encKey);
            KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
            PrivateKey privateKey = keyFactory.generatePrivate(privateSpec);

            // Data to signature
            SignReq signReq = new SignReq();
            signReq.setOrderId(req.getOrderId());
            signReq.setUserId(req.getUserId());
            signReq.setTotalPrice(order.getTotalPrice());
            signReq.setItems(orderItemReqs);

            // Object => Json Text
            String orderText = objectMapper.writeValueAsString(signReq);

            // Handle Signature
            DSA dsa = new DSA();
            PublicKey publicKey = dsa.importPublicKey(user.getPublicKey());
            String signature = dsa.sign(orderText, privateKey);

            // Verify
            boolean verify = dsa.verify(publicKey, orderText, signature);
            if (verify) {
                // Verify => True (Order)
                order.setVerify(true);
                orderRep.save(order);

                // Save
                Signature saveSignature = new Signature();
                saveSignature.setOrder(order);
                saveSignature.setSignature(signature);
                rep.save(saveSignature);
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    // Signature By Tool
    public boolean signByTool(SignByToolReq req) throws Exception {
        // Check User
        User user = userRep.findById(req.getUserId()).orElse(null);
        if (user == null) return false;

        // Order
        Order order = orderRep.findById(req.getOrderId()).orElse(null);
        if (order == null) return false;

        // Verify
        DSA dsa = new DSA();
        PublicKey publicKey = dsa.importPublicKey(user.getPublicKey());
        boolean verify = dsa.verify(publicKey, req.getOrderText(), req.getSignText());
        if (verify) {
            // Verify => True (Order)
            order.setVerify(true);
            orderRep.save(order);

            // Save
            Signature saveSignature = new Signature();
            saveSignature.setOrder(order);
            saveSignature.setSignature(req.getSignText());
            rep.save(saveSignature);
            return true;
        }
        return false;
    }

    public String handleOrderText(CheckSignatureReq req) {
        // Check User
        User user = userRep.findById(req.getUserId()).orElse(null);
        if (user == null) return null;

        // Order
        Order order = orderRep.findById(req.getOrderId()).orElse(null);
        if (order == null) return null;

        // Order Item
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

        // Data to signature
        SignReq signReq = new SignReq();
        signReq.setOrderId(req.getOrderId());
        signReq.setUserId(req.getUserId());
        signReq.setTotalPrice(order.getTotalPrice());
        signReq.setItems(orderItemReqs);

        // Object => Json Text
        return objectMapper.writeValueAsString(signReq);
    }

    // Signature Again
    public boolean checkSignature(CheckSignatureReq req) throws Exception {
        // Check User
        User user = userRep.findById(req.getUserId()).orElse(null);
        if (user == null) return false;

        // Order
        Order order = orderRep.findById(req.getOrderId()).orElse(null);
        if (order == null) return false;

        // Order Item
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

        // Data to signature
        SignReq signReq = new SignReq();
        signReq.setOrderId(req.getOrderId());
        signReq.setUserId(req.getUserId());
        signReq.setTotalPrice(order.getTotalPrice());
        signReq.setItems(orderItemReqs);

        // Object => Json Text
        String orderText = objectMapper.writeValueAsString(signReq);

        // Handle Signature
        return handleSignature(req.getPrivateKey(), user.getPublicKey(), orderText, order);
    }

    // Signature
    public boolean saveSignature(OrderReq req) throws Exception {
        // Check User
        User user = userRep.findById(req.getUserId()).orElse(null);
        if (user == null) return false;

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

        // Data to signature
        SignReq signReq = new SignReq();
        signReq.setOrderId(order.getId());
        signReq.setUserId(req.getUserId());
        signReq.setTotalPrice(req.getTotalPrice());
        signReq.setItems(req.getItems());

        // Object => Json Text
        String orderText = objectMapper.writeValueAsString(signReq);

        // Handle Signature
        return handleSignature(req.getPrivateKey(), user.getPublicKey(), orderText, order);
    }

    // Handle Signature
    private boolean handleSignature(String privateKeyReq, String publicKeyUser, String orderText, Order order) throws Exception {
        // Handle Signature
        DSA dsa = new DSA();
        PrivateKey privateKey = dsa.importPrivateKey(privateKeyReq);
        PublicKey publicKey = dsa.importPublicKey(publicKeyUser);
        String signature = dsa.sign(orderText, privateKey);

        // Verify
        boolean verify = dsa.verify(publicKey, orderText, signature);
        if (verify) {
            // Verify => True (Order)
            order.setVerify(true);
            orderRep.save(order);

            // Save
            Signature saveSignature = new Signature();
            saveSignature.setOrder(order);
            saveSignature.setSignature(signature);
            rep.save(saveSignature);
            return true;
        }
        return false;
    }
}
