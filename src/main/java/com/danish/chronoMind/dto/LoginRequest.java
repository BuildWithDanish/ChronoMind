package com.danish.chronoMind.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginRequest {
    @Schema(example = "Danish")
    private String username;

    @Schema(example = "StrongPassword@123")
    private String password;
}