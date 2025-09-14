package com.example.TaskManager.controller;

import com.example.TaskManager.dto.TeamDTO;
import com.example.TaskManager.entity.User;
import com.example.TaskManager.service.PermissionService;
import com.example.TaskManager.service.TeamService;
import com.example.TaskManager.validation.ValidationGroups;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("api/v1/company/{companyId}")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;
    private final PermissionService permissionService;


    @PostMapping("add-team")
    public ResponseEntity<TeamDTO> addTeam(
            @Validated(ValidationGroups.Create.class) @RequestBody TeamDTO teamDTO,
            @PathVariable("companyId") @Min(1) Long companyId,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.canManageCompany(user, companyId))
            throw new AccessDeniedException("You are not allowed to add this team");

        teamDTO.setCompanyId(companyId);
        TeamDTO createdTeam = teamService.addTeam(teamDTO, companyId, user);
        return new ResponseEntity<>(createdTeam, HttpStatus.CREATED);
    }



    @PatchMapping("team/{teamId}/update")
    public ResponseEntity<TeamDTO> updateTeam(
            @Valid @RequestBody TeamDTO teamDTO,
            @PathVariable("companyId") @Min(1) Long companyId,
            @PathVariable("teamId") @Min(1) Long teamId,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if  ( !permissionService.canManageTeam(user, teamId))
            throw new AccessDeniedException("You are not allowed to update this team");

        teamDTO.setId(teamId);
        teamDTO.setCompanyId(companyId);
        TeamDTO updatedTeam = teamService.updateTeam(teamDTO);
        return new ResponseEntity<>(updatedTeam, HttpStatus.OK);
    }



    @GetMapping("team/{teamId}")
    public  ResponseEntity<TeamDTO> getTeam(
            @PathVariable("companyId") @Min(1) Long companyId,
            @PathVariable("teamId") @Min(1) Long teamId,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.isMemberOfTeam(user, teamId))
            throw new AccessDeniedException("You are not the member of this team");

        TeamDTO teamDTO = teamService.getTeam(teamId);
        return new ResponseEntity<>(teamDTO, HttpStatus.OK);
    }
}