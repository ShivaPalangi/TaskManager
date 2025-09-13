package com.example.TaskManager.dto;

import com.example.TaskManager.enums.Role;
import com.example.TaskManager.validation.ValidationGroups;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class UserDTO {
    private Long id;

    @NotBlank(message = "First Name is required", groups = ValidationGroups.Create.class)
    private String firstName;

    @NotBlank(message = "Last Name is required", groups = ValidationGroups.Create.class)
    private String lastName;

    @NotBlank(message = "EmailAddress is required", groups = ValidationGroups.Create.class)
    @Email(message = "فرمت ایمیل نادرست است")
    private String emailAddress;

    @NotBlank(message = "Password is required", groups = ValidationGroups.Create.class)
    private String password;

    @NotNull(message = "Role is required", groups = ValidationGroups.Create.class)
    private Role role;

    private String dateOfBirth;
    private List<TokenDTO> tokens;
    private List<CompanyDTO> companies;
    private List<MembershipDTO> memberships;
}