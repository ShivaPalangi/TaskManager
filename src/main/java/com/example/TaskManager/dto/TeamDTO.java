package com.example.TaskManager.dto;

import com.example.TaskManager.validation.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
public class TeamDTO {
    private Long id;
    @NotBlank(message = "Name is required", groups = ValidationGroups.Create.class)
    private String name;
    private Long companyId;
    private String description;
    private List<MembershipDTO> memberships;
}