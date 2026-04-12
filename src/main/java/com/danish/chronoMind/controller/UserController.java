package com.danish.chronoMind.controller;

import com.danish.chronoMind.dto.Mapper;
import com.danish.chronoMind.dto.UpdateUserRequest;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "User Management", description = "Manage your Profile — JWT token required") // Swagger UI group
@SecurityRequirement(name = "Bearer Auth") // is poore controller pe lock icon aayega Swagger UI mein
public class UserController {

    @Autowired
    private UserService userService;

    // updateUser endpoint document kiya
    @Operation(summary = "Update Profile", description = "Update Username, password, email or weeklyReport")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized — JWT token required"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping
    public ResponseEntity<UserResponse> updateUser(@RequestBody UpdateUserRequest request) {
        User user = Mapper.toUpdateUser(request);
        User updated = userService.updateUser(user);
        if (updated == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(Mapper.toUserResponse(updated), HttpStatus.OK);
    }

    // deleteUser endpoint document kiya
    @Operation(summary = "Delete Account", description = "Your account and all journal Entries will permanently get deleted")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Account deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized — JWT token required")
    })
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.deleteByUsername(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}