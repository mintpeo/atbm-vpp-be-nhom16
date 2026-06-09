package com.atbm.appvppbe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSer {
    private final MailSender mailSender;

    public void sendMail(String email, String sub, String content) {
        // Set up send mail
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(sub);
        message.setText(content);
        mailSender.send(message);
    }
}
