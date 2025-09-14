package com.example.TaskManager.controller;

import com.example.TaskManager.dto.ChangePasswordRequest;
import com.example.TaskManager.dto.UserDTO;
import com.example.TaskManager.entity.User;
import com.example.TaskManager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping("change-password")
    public ResponseEntity<UserDTO> changePassword(@Valid @RequestBody ChangePasswordRequest request, @AuthenticationPrincipal User currentUser) {
        UserDTO user = userService.changePassword(request, currentUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @PatchMapping
    public ResponseEntity<UserDTO> updateUserInfo(@Valid @RequestBody UserDTO userDTO, @AuthenticationPrincipal User currentUser) {
        UserDTO updatedUser = userService.updateUser(userDTO, currentUser);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}
