package com.atbm.appvppbe.controller;

import com.atbm.appvppbe.dto.request.*;
import com.atbm.appvppbe.service.SignatureSer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/signature")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class SignatureCon {
    private final SignatureSer ser;

    @PostMapping("/tool")
    public boolean signByTool(@RequestBody SignByToolReq req) throws Exception {
        return ser.signByTool(req);
    }

    @PostMapping("/orderText")
    public String handleOrderText(@RequestBody CheckSignatureReq req) {
        return ser.handleOrderText(req);
    }

    @PostMapping("/verify")
    public boolean verify(@RequestBody VerifySignReq req) throws Exception {
        return ser.verify(req);
    }

    @PostMapping("/checkFile")
    public boolean checkSignatureFile(@RequestBody CheckSignatureFileReq req) {
        return ser.checkSignatureFile(req);
    }

    @PostMapping("/check")
    public boolean checkSignature(@RequestBody CheckSignatureReq req) throws Exception {
        return ser.checkSignature(req);
    }

    @PostMapping("/sign")
    public boolean saveSignature(@RequestBody OrderReq req) throws Exception {
        return ser.saveSignature(req);
    }
}
