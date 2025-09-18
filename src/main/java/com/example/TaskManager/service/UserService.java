package com.example.TaskManager.service;

import com.example.TaskManager.dto.ChangePasswordRequest;
import com.example.TaskManager.dto.UserDTO;
import com.example.TaskManager.entity.User;
import com.example.TaskManager.mapper.UserMapper;
import com.example.TaskManager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public UserDTO changePassword(ChangePasswordRequest request, User user) {

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return userMapper.mapToEmployeeDTO(user);
    }


    public UserDTO updateUser(UserDTO userDTO, User user) {
        updateUserEntityFromDTO(user, userDTO);
        User savedUser = userRepository.save(user);
        return userMapper.mapToEmployeeDTO(savedUser);
    }

    private void updateUserEntityFromDTO(User user, UserDTO userDTO) {
        if (userDTO.getFirstName() != null)
            user.setFirstName(userDTO.getFirstName());
        if (userDTO.getLastName() != null)
            user.setLastName(userDTO.getLastName());
        if (userDTO.getDateOfBirth() != null)
            user.setDateOfBirth(LocalDate.parse(userDTO.getDateOfBirth()));
        if (userDTO.getEmailAddress() != null && userRepository.existsByEmailAddress(userDTO.getEmailAddress()))
            user.setEmailAddress(userDTO.getEmailAddress());
    }
}