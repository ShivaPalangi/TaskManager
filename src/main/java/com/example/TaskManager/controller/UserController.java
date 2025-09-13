package com.example.TaskManager.controller;

import com.example.TaskManager.dto.ChangePasswordRequest;
import com.example.TaskManager.dto.UserDTO;
import com.example.TaskManager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<UserDTO> changePassword(@Valid @RequestBody ChangePasswordRequest request, Principal connectedUser) {
        UserDTO user = userService.changePassword(request, connectedUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @PatchMapping("update-user-info")
    public ResponseEntity<UserDTO> updateUserInfo(@Valid @RequestBody UserDTO userDTO, Principal connectedUser) {
        UserDTO updatedUser = userService.updateUser(userDTO, connectedUser);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}
