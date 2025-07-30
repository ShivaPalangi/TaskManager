package com.example.TaskManager.dto;

import lombok.Data;
import java.util.List;

@Data
public class EmployeeDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String emailAddress;
    private String dateOfBirth;
    private CompanyDTO company;
    private List<MembershipDTO> memberships;
}