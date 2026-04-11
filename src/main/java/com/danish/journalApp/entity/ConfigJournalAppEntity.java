package com.danish.journalApp.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "config_journal_app")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ConfigJournalAppEntity {

    private String key;
    private String value;
}
