package com.example.TaskManager.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class CompanyDTO {
    private Long id;

    @NotNull(message = "اسم شرکت نمی تواند خالی باشد")
    private String name;

    private String description;
    private List<TeamDTO> teams;
    private List<EmployeeDTO> employees;
}