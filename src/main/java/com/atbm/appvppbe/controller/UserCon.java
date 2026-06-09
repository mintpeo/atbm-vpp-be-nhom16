package com.atbm.appvppbe.controller;

import com.atbm.appvppbe.dto.entity.User;
import com.atbm.appvppbe.dto.request.LoginUserReq;
import com.atbm.appvppbe.dto.request.SignUserReq;
import com.atbm.appvppbe.service.UserSer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class UserCon {
    private final UserSer ser;

    @PostMapping("/sign")
    public User signUser(@RequestBody SignUserReq req) throws Exception {
        return ser.signUser(req);
    }

    @PostMapping("/login")
    public User loginUser(@RequestBody LoginUserReq req) {
        return ser.loginUser(req);
    }
}
