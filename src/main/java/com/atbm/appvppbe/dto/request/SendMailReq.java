package com.atbm.appvppbe.dto.request;

import lombok.Data;

@Data
public class SendMailReq {
    private String email;
    private String privateKey;
}
