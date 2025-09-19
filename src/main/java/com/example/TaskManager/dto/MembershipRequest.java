package com.example.TaskManager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MembershipRequest {
    @NotBlank(message = "username is required")
    private String username;
    private Long teamId;
}
