package com.danish.journalApp.dto;

import lombok.Data;

@Data
public class UserResponse {
    private String username;
    private String email;
    private boolean weeklyReport;
}