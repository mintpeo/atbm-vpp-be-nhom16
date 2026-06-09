package com.atbm.appvppbe.service;

import com.atbm.appvppbe.AlgorithmSignature.DSA.DSA;
import com.atbm.appvppbe.dto.entity.Order;
import com.atbm.appvppbe.dto.request.OrderReq;
import com.atbm.appvppbe.repository.OrderRep;
import com.atbm.appvppbe.repository.SignatureRep;
import com.atbm.appvppbe.dto.entity.Signature;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.security.KeyPair;

@Service
@RequiredArgsConstructor
public class SignatureSer {
    private final SignatureRep rep;
    private final OrderRep orderRep;
    private final OrderSer orderSer;
    private final ObjectMapper objectMapper;

    // 1 user 1 cap public, private
    public void saveSignature(OrderReq req) throws Exception {
        // signature success => save database
        // Object => Json Text (Dang sai)
        String orderText = objectMapper.writeValueAsString(req);

        // Create KeyPair
        DSA dsa = new DSA();
        KeyPair keyPair = dsa.createSignature();
        String signature = dsa.process(orderText, keyPair); // Signature

        // Export Key
        String exportPublicKey = dsa.exportKey(keyPair, true);
        String exportPrivateKey = dsa.exportKey(keyPair, false);

        // Save on database
        // Order
        Order order = orderSer.saveOrder(req);
        // Signature
        Signature saveSignature = new Signature();
        saveSignature.setOrder(order);
        saveSignature.setSignature(signature);
        saveSignature.setPublicKey(exportPublicKey);
        rep.save(saveSignature);
    }
}
