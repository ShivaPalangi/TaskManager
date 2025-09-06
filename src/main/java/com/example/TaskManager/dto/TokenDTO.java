package com.example.TaskManager.dto;

import com.example.TaskManager.enums.TokenType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TokenDTO {
    private Long id;
    @NotBlank(message = "Token is required")
    private String token;
    @NotNull(message = "Token Type is required")
    private TokenType tokenType;
    @NotNull
    private boolean revoked;
    @NotNull
    private boolean expired;
    @NotNull(message = "User Id is required")
    private Long userId;
}
