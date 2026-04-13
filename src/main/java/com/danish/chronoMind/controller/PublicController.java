package com.danish.chronoMind.controller;

import com.danish.chronoMind.dto.LoginRequest;
import com.danish.chronoMind.dto.Mapper;
import com.danish.chronoMind.dto.SignupRequest;
import com.danish.chronoMind.dto.UserResponse;
import com.danish.chronoMind.entity.User;
import com.danish.chronoMind.services.UserDetailServiceImpl;
import com.danish.chronoMind.services.UserService;
import com.danish.chronoMind.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "Signup, login aur health check endpoints - Get JWT Token") // Swagger UI mein group naam "Authentication" dikhega
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

    @Operation(summary = "Health check", description = "Check Server is Running")
    @ApiResponse(responseCode = "200", description = "Server is running")
    @GetMapping
    public String checkHealth() {
        return "OK";
    }

    // signup endpoint document kiya with success aur error response codes
    @Operation(summary = "Registration", description = "Create a new User and then Login to get JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "Username already exists or invalid data")
    })
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@RequestBody SignupRequest request) {
        User user = Mapper.toUser(request);
        user.setRoles(Arrays.asList("USER"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userService.save(user);
        return new ResponseEntity<>(Mapper.toUserResponse(saved), HttpStatus.CREATED);
    }

    // login endpoint document kiya — JWT token milega success pe
    @Operation(summary = "Login", description = "Login and get JWT Token, use this token for further requests")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful — get JWT"),
            @ApiResponse(responseCode = "400", description = "Wrong username or password")
    })
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