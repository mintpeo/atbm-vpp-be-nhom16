package com.atbm.appvppbe.controller;

import com.atbm.appvppbe.dto.entity.User;
import com.atbm.appvppbe.dto.request.LoginUserReq;
import com.atbm.appvppbe.dto.request.SendMailReq;
import com.atbm.appvppbe.dto.request.SignUserReq;
import com.atbm.appvppbe.dto.response.SignUserRes;
import com.atbm.appvppbe.service.UserSer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "${app.frontend.url}")
@RequiredArgsConstructor
public class UserCon {
    private final UserSer ser;

    @PostMapping("/create")
    public SignUserRes create(@RequestBody LoginUserReq req) throws Exception {
        return ser.createUserWithKey(req);
    }

    @PostMapping("/sign")
    public SignUserRes signUser(@RequestBody SignUserReq req) throws Exception {
        return ser.signUser(req);
    }

    @PostMapping("/send")
    public void sendPrivateMail(@RequestBody SendMailReq req) {
        ser.sendPrivateMail(req);
    }

    @PostMapping("/login")
    public User loginUser(@RequestBody LoginUserReq req) {
        return ser.loginUser(req);
    }
}
