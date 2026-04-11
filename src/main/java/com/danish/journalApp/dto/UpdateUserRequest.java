package com.danish.journalApp.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String username;
    private String password;
    private String email;
    private boolean weeklyReport;
}