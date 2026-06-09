package com.atbm.appvppbe.dto.request;

import lombok.Data;

@Data
public class LoginUserReq {
    private String email;
    private String password;
}
