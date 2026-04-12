package com.danish.chronoMind.controller;

import com.danish.chronoMind.dto.Mapper;
import com.danish.chronoMind.dto.SignupRequest;
import com.danish.chronoMind.dto.UserResponse;
import com.danish.chronoMind.entity.User;
import com.danish.chronoMind.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Admin", description = "Admin only operations — ROLE_ADMIN required")
@SecurityRequirement(name = "Bearer Auth") // JWT lock poore controller pe
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // getAllUsers endpoint document kiya
    @Operation(summary = "Get All User", description = "Get all user register in system")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users list returned"),
            @ApiResponse(responseCode = "403", description = "Forbidden — ADMIN role required")
    })
    @GetMapping("/all-user")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers()
                .stream()
                .map(Mapper::toUserResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // createAdmin endpoint document kiya
    @Operation(summary = "Create Admin")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Admin created successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden — ADMIN role required")
    })
    @PostMapping("/create-admin")
    public ResponseEntity<UserResponse> createAdmin(@RequestBody SignupRequest request) {
        User user = Mapper.toUser(request);
        user.setRoles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userService.save(user);
        return new ResponseEntity<>(Mapper.toUserResponse(saved), HttpStatus.CREATED);
    }
}