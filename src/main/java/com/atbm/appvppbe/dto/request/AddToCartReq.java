package com.atbm.appvppbe.dto.request;

import lombok.Data;

@Data
public class AddToCartReq {
    private long userId;
    private long productId;
    private String type;
    private int quantity;
    private String image;
}
