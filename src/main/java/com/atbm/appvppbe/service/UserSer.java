package com.atbm.appvppbe.service;

import com.atbm.appvppbe.AlgorithmSignature.DSA.DSA;
import com.atbm.appvppbe.dto.entity.User;
import com.atbm.appvppbe.dto.request.LoginUserReq;
import com.atbm.appvppbe.dto.request.SendMailReq;
import com.atbm.appvppbe.dto.request.SignUserReq;
import com.atbm.appvppbe.dto.response.SignUserRes;
import com.atbm.appvppbe.repository.UserRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class UserSer {
    private final UserRep rep;
    private final MailSer mailSer;

    // Create new user with new key
    public SignUserRes createUserWithKey(LoginUserReq req) throws Exception {
        User user = loginUser(req);
        if (user == null) return null;

        // DSA
        DSA dsa = new DSA();
        KeyPair keyPair = dsa.createSignature();
        String newPubKey = dsa.exportKey(keyPair, true);
        String newPriKey = dsa.exportKey(keyPair, false);

        // Create new user
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setName(user.getName());
        newUser.setPublicKey(newPubKey);

        User saveUser = rep.save(newUser);
        return new SignUserRes(saveUser, newPriKey);
    }

    public SignUserRes signUser(SignUserReq req) throws Exception {
        // DSA
        DSA dsa = new DSA();
        KeyPair keyPair = dsa.createSignature();
        String exportPublicKey = dsa.exportKey(keyPair, true);
        String exportPrivateKey = dsa.exportKey(keyPair, false);

        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword()); // Chua Encrypt
        user.setPublicKey(exportPublicKey);

        User saveUser = rep.save(user);

        return new SignUserRes(saveUser, exportPrivateKey);
    }

    public void sendPrivateMail(SendMailReq req) {
        // Send Private Key To Mail
        String sub = "Welcome To VPP Shop";
        String content = "Your Private Key: " + req.getPrivateKey();
        mailSer.sendMail(req.getEmail(), sub, content);
    }

    public User loginUser(LoginUserReq req) {
        User user = rep.findFirstByEmailOrderByIdDesc(req.getEmail()).orElse(null);
        if (user == null) return null;
        if (user.getPassword().equals(req.getPassword())) return user;
        return null; // wrong pass
    }
}
