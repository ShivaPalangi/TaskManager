package com.example.TaskManager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class EmployeeDTO {
    private Long id;

    @NotNull(message = "نام نمی تواند خالی باشد")
    private String firstName;

    @NotNull(message = "نام خانوادگی نمی تواند خالی باشد")
    private String lastName;

    @NotNull(message = "شماره تلفن نمی تواند خالی باشد")
    private String phoneNumber;

    @Email(message = "فرمت ایمیل نادرست است")
    private String emailAddress;

    @NotNull(message = "تاریخ تولد نمی تواند خالی باشد")
    private String dateOfBirth;

    private CompanyDTO company;
    private List<MembershipDTO> memberships;
}