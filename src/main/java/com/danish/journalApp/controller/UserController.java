package com.danish.journalApp.controller;

import com.danish.journalApp.dto.Mapper;
import com.danish.journalApp.dto.UpdateUserRequest;
import com.danish.journalApp.dto.UserResponse;
import com.danish.journalApp.entity.User;
import com.danish.journalApp.services.UserService;
import com.danish.journalApp.services.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WeatherService weatherService;

    @PutMapping
    public ResponseEntity<UserResponse> updateUser(@RequestBody UpdateUserRequest request) {
        User user = Mapper.toUpdateUser(request);
        User updated = userService.updateUser(user);
        return new ResponseEntity<>(Mapper.toUserResponse(updated), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.deleteByUsername(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<String> greeting() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        double feelsLike = weatherService.getWeather("Lucknow").main.feelsLike - 273;
        return new ResponseEntity<>(
                "Hi " + authentication.getName() + " temp feels like " + feelsLike + "C",
                HttpStatus.OK);
    }
}