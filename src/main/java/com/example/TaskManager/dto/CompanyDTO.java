package com.example.TaskManager.dto;

import lombok.Data;
import java.util.List;

@Data
public class CompanyDTO {
    private Long id;
    private String name;
    private String description;
    private List<TeamDTO> teams;
    private List<EmployeeDTO> employees;
}