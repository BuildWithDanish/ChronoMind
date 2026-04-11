package com.danish.journalApp.dto;

import com.danish.journalApp.enums.JournalType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JournalEntryResponse {
    private String id;
    private String title;
    private String content;
    private LocalDateTime date;
    private JournalType journalType;
}