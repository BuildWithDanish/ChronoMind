package com.danish.journalApp.controller;

import com.danish.journalApp.dto.LoginRequest;
import com.danish.journalApp.dto.Mapper;
import com.danish.journalApp.dto.SignupRequest;
import com.danish.journalApp.dto.UserResponse;
import com.danish.journalApp.entity.User;
import com.danish.journalApp.services.UserDetailServiceImpl;
import com.danish.journalApp.services.UserService;
import com.danish.journalApp.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailServiceImpl userDetailsService;

    @GetMapping
    public String checkHealth() {
        return "OK";
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@RequestBody SignupRequest request) {
        User user = Mapper.toUser(request);
        user.setRoles(Arrays.asList("ROLE_USER"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userService.save(user);
        return new ResponseEntity<>(Mapper.toUserResponse(saved), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Login failed for user: {}", request.getUsername(), e);
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }
}