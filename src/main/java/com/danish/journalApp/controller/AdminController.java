package com.danish.journalApp.controller;

import com.danish.journalApp.cache.AppCache;
import com.danish.journalApp.dto.Mapper;
import com.danish.journalApp.dto.SignupRequest;
import com.danish.journalApp.dto.UserResponse;
import com.danish.journalApp.entity.User;
import com.danish.journalApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AppCache appCache;

    @GetMapping("/all-user")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers()
                .stream()
                .map(Mapper::toUserResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/create-admin")
    public ResponseEntity<UserResponse> createAdmin(@RequestBody SignupRequest request) {
        User user = Mapper.toUser(request);
        user.setRoles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userService.save(user);
        return new ResponseEntity<>(Mapper.toUserResponse(saved), HttpStatus.CREATED);
    }

    @GetMapping("/clear-app-cache")
    public ResponseEntity<?> clearCache() {
        appCache.init();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}