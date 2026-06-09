package com.atbm.appvppbe.service;

import com.atbm.appvppbe.AlgorithmSignature.DSA.DSA;
import com.atbm.appvppbe.dto.entity.User;
import com.atbm.appvppbe.dto.request.LoginUserReq;
import com.atbm.appvppbe.dto.request.SignUserReq;
import com.atbm.appvppbe.repository.UserRep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.KeyPair;

@Service
@RequiredArgsConstructor
public class UserSer {
    private final UserRep rep;
    private final MailSer mailSer;

    public User signUser(SignUserReq req) throws Exception {
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
        if (saveUser.getId() != 0) {
            // Send Private Key To Mail
            String email = req.getEmail();
            String sub = "Welcome To My Shop";
            String content = "Your Private Key: " + exportPrivateKey;
            System.out.println(exportPrivateKey);
//            mailSer.sendMail(email, sub, content);
        }

        return saveUser;
    }

    public User loginUser(LoginUserReq req) {
        User user = rep.findByEmail(req.getEmail()).orElse(null);
        if (user == null) return null;
        if (user.getPassword().equals(req.getPassword())) return user;
        return null; // wrong pass
    }
}
