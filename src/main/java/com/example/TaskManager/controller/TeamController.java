package com.example.TaskManager.controller;

import com.example.TaskManager.dto.TeamDTO;
import com.example.TaskManager.entity.User;
import com.example.TaskManager.service.PermissionService;
import com.example.TaskManager.service.TeamService;
import com.example.TaskManager.validation.ValidationGroups;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/companies/{companyId}/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;
    private final PermissionService permissionService;


    @PostMapping
    public ResponseEntity<?> addTeam(
            @Validated(ValidationGroups.Create.class) @RequestBody TeamDTO teamDTO,
            @PathVariable("companyId") @Min(1) Long companyId ,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.canManageCompany(user, companyId))
            throw new AccessDeniedException("You are not allowed to add team to this company.");

        teamDTO.setCompanyId(companyId);
        TeamDTO createdTeam = teamService.addTeam(teamDTO, user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdTeam.getId())
                .toUri();

        return ResponseEntity.created(location).body(
                Map.of("message", "Team created successfully", "data", createdTeam));
    }



    @PatchMapping("/{teamId}")
    public ResponseEntity<?> updateTeam(
            @Valid @RequestBody TeamDTO teamDTO,
            @PathVariable("companyId") @Min(1) Long companyId,
            @PathVariable("teamId") @Min(1) Long teamId,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if  ( !permissionService.canManageTeam(user, teamId))
            throw new AccessDeniedException("You are not allowed to update this team");

        teamDTO.setId(teamId);
        teamDTO.setCompanyId(companyId);
        TeamDTO updatedTeam = teamService.updateTeam(teamDTO);
        return ResponseEntity.ok(
                Map.of("message", "Team updated successfully", "data", updatedTeam));
    }



    @GetMapping("/{teamId}")
    public  ResponseEntity<TeamDTO> getTeam(
            @PathVariable("companyId") @Min(1) Long companyId,
            @PathVariable("teamId") @Min(1) Long teamId,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.isMemberOfTeam(user, teamId, companyId))
            throw new AccessDeniedException("You are not the member of this team");

        TeamDTO teamDTO = teamService.getTeam(teamId);
        return ResponseEntity.ok(teamDTO);
    }



    @DeleteMapping("/{teamId}")
    public ResponseEntity<?> deleteTeam(
            @PathVariable("companyId") @Min(1) Long companyId,
            @PathVariable("teamId") @Min(1) Long teamId,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.canManageCompany(user, companyId))
            throw new AccessDeniedException("You are not allowed delete this team!");

        teamService.deleteTeam(teamId);

        String redirectUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/companies/{CompanyId}/teams")
                .buildAndExpand(companyId)
                .toUriString();

        Map<String, Object> response = Map.of(
                "redirect", true,
                "redirectUrl", redirectUrl,
                "message", "Team with id %d deleted successfully".formatted(teamId)
        );
        return ResponseEntity.ok(response);
    }



    @GetMapping
    public ResponseEntity<List<TeamDTO>> getTeams(
            @PathVariable("companyId") @Min(1) Long companyId,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.isMemberOfCompany(user, companyId))
            throw new AccessDeniedException("You are not the member of this company");
        List<TeamDTO> teams = teamService.getTeams(companyId);
        return ResponseEntity.ok(teams);
    }



    @GetMapping("/search")
    public ResponseEntity<List<TeamDTO>> searchTeams(
            @PathVariable("companyId") @Min(1) Long companyId,
            @RequestParam String title,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.isMemberOfCompany(user, companyId))
            throw new AccessDeniedException("You are not the member of this company");
        List<TeamDTO> teams = teamService.searchTeams(companyId, title);
        return ResponseEntity.ok(teams);
    }
}