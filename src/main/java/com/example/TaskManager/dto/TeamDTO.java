package com.example.TaskManager.dto;

import lombok.Data;
import java.util.List;

@Data
public class TeamDTO {
    private Long id;
    private String name;
    private String description;
    private CompanyDTO company;
    private List<MembershipDTO> memberships;
}