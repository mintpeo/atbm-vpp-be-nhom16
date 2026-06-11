package com.atbm.appvppbe.controller;

import com.atbm.appvppbe.dto.request.OrderReq;
import com.atbm.appvppbe.dto.request.VerifySignReq;
import com.atbm.appvppbe.service.SignatureSer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/signature")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class SignatureCon {
    private final SignatureSer ser;

    @PostMapping("/verify")
    public boolean verify(@RequestBody VerifySignReq req) throws Exception {
        return ser.verify(req);
    }

    @PostMapping("/sign")
    public void saveSignature(@RequestBody OrderReq req) throws Exception {
        ser.saveSignature(req);
    }
}
