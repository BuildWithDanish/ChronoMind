package com.danish.journalApp.services;

import com.danish.journalApp.entity.User;
import com.danish.journalApp.repository.JournalEntryRepository;
import com.danish.journalApp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    public JournalEntryRepository journalEntryRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByUsername(authentication.getName());
        if (currentUser != null) {
            currentUser.setUsername(user.getUsername());
            currentUser.setPassword(passwordEncoder.encode(user.getPassword()));
            currentUser.setEmail(user.getEmail());
            currentUser.setWeeklyReport(user.isWeeklyReport());
            return userRepository.save(currentUser);
        }
        return null;
    }

    public User save(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            log.error("Failed to save user: {}", user.getUsername(), e);
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> saveAdmin(User user) {
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public void deleteByUsername(String username) {
        User user = userRepository.findByUsername(username);
        user.getJournalEntries().forEach(journalEntry -> {
            journalEntryRepository.deleteById(journalEntry.getId());
        });
        userRepository.deleteByUsername(username);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
