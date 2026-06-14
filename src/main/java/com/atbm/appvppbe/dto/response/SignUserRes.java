package com.atbm.appvppbe.dto.response;

import com.atbm.appvppbe.dto.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUserRes {
    private User user;
    private String privateKey;
}
