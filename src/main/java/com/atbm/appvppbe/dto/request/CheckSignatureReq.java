package com.atbm.appvppbe.dto.request;

import lombok.Data;

@Data
public class CheckSignatureReq {
    private long userId;
    private long orderId;
    private String privateKey;
}
