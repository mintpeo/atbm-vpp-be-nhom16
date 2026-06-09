package com.atbm.appvppbe.dto.request;

import lombok.Data;

@Data
public class SignUserReq {
    private String email;
    private String password;
}
