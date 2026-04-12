package com.danish.chronoMind.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SignupRequest {
    @Schema(example = "Danish")
    private String username;

    @Schema(example = "StrongPassword@123")
    private String password;

    @Schema(example = "danish@gmail.com")
    private String email;

    @Schema(example = "true", description = "Enable weekly productivity report emails")
    private boolean weeklyReport;
}