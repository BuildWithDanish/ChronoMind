package com.danish.chronoMind.dto;

import com.danish.chronoMind.enums.JournalType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JournalEntryResponse {
    @Schema(example = "661f8a2c1234567890abcdef")
    private String id;

    @Schema(example = "Daily Work Summary")
    private String title;

    @Schema(example = "Completed backend APIs and fixed authentication bugs")
    private String content;

    @Schema(example = "2026-04-12T10:15:30")
    private LocalDateTime date;

    @Schema(example = "PRODUCTIVITY")
    private JournalType journalType;
}