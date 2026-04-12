package com.danish.chronoMind.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class User {

    @Id
    private ObjectId id;
    @NonNull
    @Indexed(unique = true)
    private String username;
    @NonNull
    private String password;
    private String email;
    private boolean weeklyReport;
    private List<JournalEntry> journalEntries = new ArrayList<>();
    private List<String> roles = new ArrayList<>();

}
