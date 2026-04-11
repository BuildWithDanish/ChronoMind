package com.danish.journalApp.dto;

import com.danish.journalApp.entity.JournalEntry;
import com.danish.journalApp.entity.User;

public class Mapper {

    public static User toUpdateUser(UpdateUserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setWeeklyReport(request.isWeeklyReport());
        return user;
    }

    public static UserResponse toUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setWeeklyReport(user.isWeeklyReport());
        return response;
    }

    public static JournalEntryResponse toJournalEntryResponse(JournalEntry entry) {
        JournalEntryResponse response = new JournalEntryResponse();
        response.setId(entry.getId().toHexString());
        response.setTitle(entry.getTitle());
        response.setContent(entry.getContent());
        response.setDate(entry.getDate());
        response.setJournalType(entry.getJournalType());
        return response;
    }

    public static JournalEntry toJournalEntry(JournalEntryRequest request) {
        JournalEntry entry = new JournalEntry();
        entry.setTitle(request.getTitle());
        entry.setContent(request.getContent());
        entry.setJournalType(request.getJournalType());
        return entry;
    }

    public static User toUser(SignupRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setWeeklyReport(request.isWeeklyReport());
        return user;
    }
}