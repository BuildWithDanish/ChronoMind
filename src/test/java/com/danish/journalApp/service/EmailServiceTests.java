package com.danish.journalApp.service;

import com.danish.journalApp.services.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTests {

    @Autowired
    private EmailService emailService;

    @Test
    public void sendEmailTests() {
        emailService.sendMail("buildwithdanishofficial@gmail.com", "checking mail sender", "hahahahahahahhahahhahhahah");
    }
}
