package com.example.TaskManager.dto;

import lombok.Data;
import java.util.List;

@Data
public class TeamDTO {
    private Long id;
    private String name;
    private Long companyId;
    private String description;
    private List<MembershipDTO> memberships;
}