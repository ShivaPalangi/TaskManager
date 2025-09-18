package com.example.TaskManager.mapper;

import com.example.TaskManager.dto.TokenDTO;
import com.example.TaskManager.entity.Token;
import com.example.TaskManager.exception.ResourceNotFoundException;
import com.example.TaskManager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TokenMapper {
    private final UserRepository userRepository;

    public TokenDTO mapToTokenDTO(Token token) {
        if (token == null) return null;

        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setId(token.getId());
        tokenDTO.setToken(token.getToken());
        tokenDTO.setTokenType(token.getTokenType());
        tokenDTO.setRevoked(token.isRevoked());
        tokenDTO.setExpired(token.isExpired());
        if ( token.getUser() != null )
            tokenDTO.setUserId(token.getUser().getId());
        return tokenDTO;
    }

    public Token mapToTokenEntity(TokenDTO tokenDTO) {
        if (tokenDTO == null) return null;

        Token token = new Token();
        token.setId(tokenDTO.getId());
        token.setToken(tokenDTO.getToken());
        token.setTokenType(tokenDTO.getTokenType());
        token.setRevoked(tokenDTO.isRevoked());
        token.setExpired(tokenDTO.isExpired());
        if ( tokenDTO.getUserId() != null)
            token.setUser(userRepository.findById(tokenDTO.getUserId()).orElseThrow(
                    () -> new ResourceNotFoundException("User not found with id: " + tokenDTO.getUserId())));
        return token;
    }
}