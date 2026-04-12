package com.danish.chronoMind.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserResponse {
    @Schema(example = "Danish")
    private String username;

    @Schema(example = "danish@gmail.com")
    private String email;

    @Schema(example = "true")
    private boolean weeklyReport;
}