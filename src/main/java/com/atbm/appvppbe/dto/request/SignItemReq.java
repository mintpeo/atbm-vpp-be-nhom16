package com.atbm.appvppbe.dto.request;

import lombok.Data;

@Data
public class SignItemReq {
    private long productId;
    private int price;
    private int quantity;
    private String type;
}
