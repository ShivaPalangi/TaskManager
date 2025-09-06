package com.example.TaskManager.mapper;

import com.example.TaskManager.dto.UserDTO;
import com.example.TaskManager.entity.Company;
import com.example.TaskManager.entity.User;
import com.example.TaskManager.repository.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class UserMapper {
    private static CompanyRepository companyRepository;

    public static UserDTO mapToEmployeeDTO(User user){
        if (user == null) return null;

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmailAddress(user.getEmailAddress());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole());
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        if (user.getDateOfBirth() != null)
            userDTO.setDateOfBirth(user.getDateOfBirth().format(formatter));
        if (user.getCompany() != null)
            userDTO.setCompanyId(user.getCompany().getId());
        if (user.getTokens() != null && !user.getTokens().isEmpty())
            userDTO.setTokens(user.getTokens().stream().map(TokenMapper::mapToTokenDTO).collect(Collectors.toList()));
        if ( user.getMemberships() != null && !user.getMemberships().isEmpty())
            userDTO.setMemberships(user.getMemberships().stream().map(MembershipMapper::mapToMembershipDTO).collect(Collectors.toList()));
        return userDTO;
    }


    public static User mapToEmployeeEntity(UserDTO userDTO){
        if (userDTO == null) return null;

        User user = new User();
        user.setId(userDTO.getId());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmailAddress(userDTO.getEmailAddress());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        user.setDateOfBirth(LocalDate.parse(userDTO.getDateOfBirth()));
        if (userDTO.getCompanyId() != null){
            Optional<Company> company = companyRepository.findById(userDTO.getCompanyId());
            user.setCompany(company.get());
        }
        if (userDTO.getTokens() != null && !userDTO.getTokens().isEmpty())
            user.setTokens(userDTO.getTokens().stream().map(TokenMapper::mapToTokenEntity).collect(Collectors.toList()));
        if ( userDTO.getMemberships() != null && !userDTO.getMemberships().isEmpty())
            user.setMemberships(userDTO.getMemberships().stream().map(MembershipMapper::mapToMembershipEntity).collect(Collectors.toList()));
        return user;
    }
}