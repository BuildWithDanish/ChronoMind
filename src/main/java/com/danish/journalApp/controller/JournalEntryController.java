package com.danish.journalApp.controller;

import com.danish.journalApp.dto.JournalEntryRequest;
import com.danish.journalApp.dto.JournalEntryResponse;
import com.danish.journalApp.dto.Mapper;
import com.danish.journalApp.entity.JournalEntry;
import com.danish.journalApp.entity.User;
import com.danish.journalApp.services.JournalEntryService;
import com.danish.journalApp.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping
    public ResponseEntity<List<JournalEntryResponse>> getJournalEntriesOfUsers() {
        User user = userService.findByUsername(getAuth().getName());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<JournalEntry> entries = user.getJournalEntries();
        if (entries != null && !entries.isEmpty()) {
            List<JournalEntryResponse> response = entries.stream()
                    .map(Mapper::toJournalEntryResponse)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<JournalEntryResponse> getJournalEntryById(@PathVariable ObjectId id) {
        User user = userService.findByUsername(getAuth().getName());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        JournalEntry entry = user.getJournalEntries().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (entry != null) {
            return new ResponseEntity<>(Mapper.toJournalEntryResponse(entry), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<JournalEntryResponse> createJournalEntry(
            @RequestBody JournalEntryRequest request) {
        try {
            JournalEntry entry = Mapper.toJournalEntry(request);
            journalEntryService.saveEntry(entry, getAuth().getName());
            return new ResponseEntity<>(Mapper.toJournalEntryResponse(entry), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<JournalEntryResponse> updateJournalEntry(
            @RequestBody JournalEntryRequest request, @PathVariable ObjectId id) {
        User user = userService.findByUsername(getAuth().getName());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        JournalEntry old = user.getJournalEntries().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (old != null) {
            old.setTitle(request.getTitle() != null && !request.getTitle().isEmpty()
                    ? request.getTitle() : old.getTitle());
            old.setContent(request.getContent() != null && !request.getContent().isEmpty()
                    ? request.getContent() : old.getContent());
            old.setJournalType(request.getJournalType() != null
                    ? request.getJournalType() : old.getJournalType());
            journalEntryService.update(id, old);
            return new ResponseEntity<>(Mapper.toJournalEntryResponse(old), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteJournalEntry(@PathVariable ObjectId id) {
        if (journalEntryService.delete(id, getAuth().getName())) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}