package com.danish.chronoMind.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @Schema(example = "Danish")
    private String username;

    @Schema(example = "NewStrongPassword@123")
    private String password;

    @Schema(example = "danish_updated@gmail.com")
    private String email;

    @Schema(example = "true")
    private boolean weeklyReport;
}