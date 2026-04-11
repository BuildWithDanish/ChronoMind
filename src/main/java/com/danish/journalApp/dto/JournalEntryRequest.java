package com.danish.journalApp.dto;

import com.danish.journalApp.enums.JournalType;
import lombok.Data;

@Data
public class JournalEntryRequest {
    private String title;
    private String content;
    private JournalType journalType;
}