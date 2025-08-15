package com.example.TaskManager.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;
import java.util.List;

@Data
public class EmployeeDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    @Email(message = "فرمت ایمیل نادرست است")
    private String emailAddress;

    private String dateOfBirth;
    private Long companyId;
    private List<MembershipDTO> memberships;
}