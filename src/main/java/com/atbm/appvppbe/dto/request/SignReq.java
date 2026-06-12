package com.atbm.appvppbe.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class SignReq {
    private long orderId;
    private long userId;
    private int totalPrice;
    private List<OrderItemReq> items;
}
