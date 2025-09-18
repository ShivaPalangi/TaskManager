package com.example.TaskManager.mapper;

import com.example.TaskManager.dto.UserDTO;
import com.example.TaskManager.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class UserMapper {
    private final CompanyMapper companyMapper;
    private final TokenMapper tokenMapper;
    private final MembershipMapper membershipMapper;
    private final TaskCategoryMapper taskCategoryMapper;

    public UserDTO mapToEmployeeDTO(User user){
        if (user == null) return null;

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmailAddress(user.getEmailAddress());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole());
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        if ( user.getDateOfBirth() != null )
            userDTO.setDateOfBirth(user.getDateOfBirth().format(formatter));
        if ( user.getCompanies() != null && !user.getCompanies().isEmpty() )
            userDTO.setCompanies(user.getCompanies().stream().map(companyMapper::mapToCompanyDTO).collect(Collectors.toList()));
        if ( user.getTokens() != null && !user.getTokens().isEmpty() )
            userDTO.setTokens(user.getTokens().stream().map(tokenMapper::mapToTokenDTO).collect(Collectors.toList()));
        if ( user.getMemberships() != null && !user.getMemberships().isEmpty() )
            userDTO.setMemberships(user.getMemberships().stream().map(membershipMapper::mapToMembershipDTO).collect(Collectors.toList()));
        if ( user.getTaskCategories() != null && !user.getTaskCategories().isEmpty() )
            userDTO.setTaskCategories(user.getTaskCategories().stream().map(taskCategoryMapper::mapToTaskCategoryDTO).collect(Collectors.toList()));
        return userDTO;
    }


    public User mapToEmployeeEntity(UserDTO userDTO){
        if (userDTO == null) return null;

        User user = new User();
        user.setId(userDTO.getId());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmailAddress(userDTO.getEmailAddress());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        user.setDateOfBirth(LocalDate.parse(userDTO.getDateOfBirth()));
        if ( userDTO.getCompanies() != null && !userDTO.getCompanies().isEmpty() )
            user.setCompanies(userDTO.getCompanies().stream().map(companyMapper::mapToCompanyEntity).collect(Collectors.toList()));
        if ( userDTO.getTokens() != null && !userDTO.getTokens().isEmpty() )
            user.setTokens(userDTO.getTokens().stream().map(tokenMapper::mapToTokenEntity).collect(Collectors.toList()));
        if ( userDTO.getMemberships() != null && !userDTO.getMemberships().isEmpty() )
            user.setMemberships(userDTO.getMemberships().stream().map(membershipMapper::mapToMembershipEntity).collect(Collectors.toList()));
        if ( userDTO.getTaskCategories() != null && !userDTO.getTaskCategories().isEmpty() )
            user.setTaskCategories(userDTO.getTaskCategories().stream().map(taskCategoryMapper::mapToTaskCategoryEntity).collect(Collectors.toList()));
        return user;
    }
}