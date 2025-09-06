package com.example.TaskManager.mapper;

import com.example.TaskManager.dto.TokenDTO;
import com.example.TaskManager.entity.Token;
import com.example.TaskManager.entity.User;
import com.example.TaskManager.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class TokenMapper {
    private static UserRepository userRepository;

    public static TokenDTO mapToTokenDTO(Token token) {
        if (token == null) return null;

        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setId(token.getId());
        tokenDTO.setToken(token.getToken());
        tokenDTO.setTokenType(token.getTokenType());
        tokenDTO.setRevoked(token.isRevoked());
        tokenDTO.setExpired(token.isExpired());
        if (token.getUser() != null)
            tokenDTO.setUserId(token.getUser().getId());
        return tokenDTO;
    }

    public static Token mapToTokenEntity(TokenDTO tokenDTO) {
        if (tokenDTO == null) return null;

        Token token = new Token();
        token.setId(tokenDTO.getId());
        token.setToken(tokenDTO.getToken());
        token.setTokenType(tokenDTO.getTokenType());
        token.setRevoked(tokenDTO.isRevoked());
        token.setExpired(tokenDTO.isExpired());
        if (token.getUser() != null){
            Optional<User> optionalUser = userRepository.findById(token.getUser().getId());
            token.setUser(optionalUser.get());
        }
        return token;
    }
}