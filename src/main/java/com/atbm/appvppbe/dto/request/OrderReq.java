package com.atbm.appvppbe.dto.request;

import lombok.Data;

@Data
public class OrderReq {
    private long userId;
    private int totalPrice;
}
