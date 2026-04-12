package com.danish.chronoMind.services;

import com.danish.chronoMind.entity.JournalEntry;
import com.danish.chronoMind.entity.User;
import com.danish.chronoMind.repository.JournalEntryRepository;
import com.danish.chronoMind.repository.UserRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class JournalEntryService {

    @Autowired
    JournalEntryRepository journalEntryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(JournalEntryService.class);

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String username) {
        try {
            User user = userRepository.findByUsername(username);
            journalEntry.setDate(LocalDateTime.now());
            journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(journalEntry);
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public Optional<JournalEntry> findById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean delete(ObjectId id, String username) {
        boolean removed = false;
        try {
            User user = userRepository.findByUsername(username);
            removed = user.getJournalEntries().removeIf(journalEntry -> journalEntry.getId().equals(id));
            if (removed) {
                userService.save(user);
                journalEntryRepository.deleteById(id);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return removed;
    }

    public void update(ObjectId id, JournalEntry newEntry) {
        newEntry.setDate(LocalDateTime.now());
        journalEntryRepository.save(newEntry);
    }
}
