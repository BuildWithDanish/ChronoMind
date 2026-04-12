package com.danish.chronoMind.scheduler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserSchedulerTests {

    @Autowired
    UserScheduler userScheduler;

    @Test
    public void fetchUsersAndSendSaMailTests() {
        userScheduler.fetchUsersAndSendSaMail();
    }
}
