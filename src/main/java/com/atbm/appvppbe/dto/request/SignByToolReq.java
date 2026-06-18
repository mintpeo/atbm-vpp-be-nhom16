package com.atbm.appvppbe.dto.request;

import lombok.Data;

@Data
public class SignByToolReq {
    private long userId;
    private long orderId;
    private String signText;
    private String orderText;
}
