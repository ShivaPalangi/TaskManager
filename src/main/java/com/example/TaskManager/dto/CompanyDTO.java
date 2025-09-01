package com.example.TaskManager.dto;

import com.example.TaskManager.validation.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
public class CompanyDTO {
    private Long id;

    @NotBlank(message = "name is required", groups = ValidationGroups.Create.class)
    @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters")
    private String name;

    private String description;
    private List<TeamDTO> teams;
    private List<UserDTO> employees;
}