package com.atbm.appvppbe.dto.request;

import lombok.Data;

@Data
public class OrderItemReq {
    private String image;
    private long productId;
    private int quantity;
    private String type;
}
