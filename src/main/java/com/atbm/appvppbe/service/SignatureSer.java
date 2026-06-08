package com.atbm.appvppbe.service;

import com.atbm.appvppbe.dto.request.OrderReq;
import com.atbm.appvppbe.repository.OrderRep;
import com.atbm.appvppbe.repository.SignatureRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignatureSer {
    private SignatureRep rep;
    private OrderRep orderRep;

    public void saveSignature(OrderReq req) {
        // signature success => save database

    }
}
