package com.example.TaskManager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChangePasswordRequest {
    @NotBlank(message = "Current Password is required")
    private String currentPassword;
    @NotBlank(message = "New Password is required")
    private String newPassword;
    @NotBlank(message = "Confirmation Password is required")
    private String confirmationPassword;
}
