package com.example.TaskManager.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class TeamDTO {
    private Long id;

    @NotNull(message = "اسم تیم نمی تواند خالی باشد")
    private String name;

    @NotNull(message = "آی دی شرکت نمی تواند خالی باشد")
    private CompanyDTO company;

    private String description;
    private List<MembershipDTO> memberships;
}