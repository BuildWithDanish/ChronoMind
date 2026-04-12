package com.danish.chronoMind.dto;

import com.danish.chronoMind.enums.JournalType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class JournalEntryRequest {
    @Schema(example = "Daily Work Summary")
    private String title;

    @Schema(example = "Completed backend APIs and improved system performance")
    private String content;

    @Schema(example = "PRODUCTIVITY", description = "Type of journal entry: PRODUCTIVITY or CASUAL")
    private JournalType journalType;
}