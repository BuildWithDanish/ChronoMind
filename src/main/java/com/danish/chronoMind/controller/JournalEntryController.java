package com.danish.chronoMind.controller;

import com.danish.chronoMind.dto.JournalEntryRequest;
import com.danish.chronoMind.dto.JournalEntryResponse;
import com.danish.chronoMind.dto.Mapper;
import com.danish.chronoMind.entity.JournalEntry;
import com.danish.chronoMind.entity.User;
import com.danish.chronoMind.services.JournalEntryService;
import com.danish.chronoMind.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Journal Entries", description = "create, read, update and delete Journal entries— JWT token required") // Swagger Ui Group
@SecurityRequirement(name = "Bearer Auth") // poore controller pe JWT lock
public class JournalEntryController {

    @Autowired
    JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    // getAll endpoint document kiya
    @Operation(summary = "Get All Entries", description = "Returns All Journal Entries of LoggedIn User")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Entries successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Koi entry nahi mili"),
            @ApiResponse(responseCode = "401", description = "Unauthorized — JWT token required")
    })
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

    // getById endpoint document kiya
    @Operation(summary = "Get Journal By ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Entry found"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Entry nahi mili"),
            @ApiResponse(responseCode = "401", description = "Unauthorized — JWT token required")
    })
    @GetMapping("/id/{id}")
    public ResponseEntity<JournalEntryResponse> getJournalEntryById(@PathVariable String id) {
        ObjectId objectId;
        try {
            objectId = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userService.findByUsername(getAuth().getName());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        JournalEntry entry = user.getJournalEntries().stream()
                .filter(e -> e.getId().equals(objectId))
                .findFirst()
                .orElse(null);
        if (entry != null) {
            return new ResponseEntity<>(Mapper.toJournalEntryResponse(entry), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // create endpoint document kiya
    @Operation(summary = "Create a new Journal")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Entry successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized — JWT token required")
    })
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

    // update endpoint document kiya
    @Operation(summary = "Update journal entries")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Entry updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Entry not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized — JWT token required")
    })
    @PutMapping("/id/{id}")
    public ResponseEntity<JournalEntryResponse> updateJournalEntry(@RequestBody JournalEntryRequest request, @PathVariable String id) {
        ObjectId objectId;
        try {
            objectId = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userService.findByUsername(getAuth().getName());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        JournalEntry old = user.getJournalEntries().stream()
                .filter(e -> e.getId().equals(objectId))
                .findFirst()
                .orElse(null);
        if (old != null) {
            old.setTitle(request.getTitle() != null && !request.getTitle().isEmpty()
                    ? request.getTitle() : old.getTitle());
            old.setContent(request.getContent() != null && !request.getContent().isEmpty()
                    ? request.getContent() : old.getContent());
            old.setJournalType(request.getJournalType() != null
                    ? request.getJournalType() : old.getJournalType());
            journalEntryService.update(objectId, old);
            return new ResponseEntity<>(Mapper.toJournalEntryResponse(old), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // delete endpoint document kiya
    @Operation(summary = "Delete Journal by Id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Entry deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Entry nahi mili"),
            @ApiResponse(responseCode = "401", description = "Unauthorized — JWT token required")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteJournalEntry(@PathVariable String id) {
        ObjectId objectId;
        try {
            objectId = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (journalEntryService.delete(objectId, getAuth().getName())) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}