package com.manage.userManagement.controller;

import com.manage.userManagement.dto.request.EmailSenderRequest;
import com.manage.userManagement.service.MailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailSenderController {
    @Autowired
    MailSenderService mailSenderService;

    @PostMapping("/email/send")
    public ResponseEntity<String> emailSender(@RequestBody EmailSenderRequest emailSenderRequest){
        return ResponseEntity.ok(mailSenderService.sendNewMail(emailSenderRequest));
    }
}
