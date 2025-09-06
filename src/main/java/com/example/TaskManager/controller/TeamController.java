package com.example.TaskManager.controller;

import com.example.TaskManager.dto.TeamDTO;
import com.example.TaskManager.service.CompanyService;
import com.example.TaskManager.service.TeamService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("company")
@RequiredArgsConstructor
public class TeamController {
    private TeamService teamService;
    private CompanyService companyService;

    @PostMapping("{id}/add-team")
    public ResponseEntity<TeamDTO> addTeam(@Valid @RequestBody TeamDTO teamDTO, @PathVariable @Min(1) Long id){
        TeamDTO createdTeam = teamService.addTeam(teamDTO, id);
        return new ResponseEntity<>(createdTeam, HttpStatus.CREATED);
    }

    @PatchMapping("{companyId}/team/{teamId}/update")
    public ResponseEntity<TeamDTO> updateTeam(@Valid @RequestBody TeamDTO teamDTO, @PathVariable("companyId") @Min(1) Long companyId, @PathVariable("teamId") @Min(1) Long teamId){
        teamDTO.setId(teamId);
        TeamDTO updatedTeam = teamService.updateTeam(teamDTO);
        return new ResponseEntity<>(updatedTeam, HttpStatus.OK);
    }

    @GetMapping("{companyId}/team/{teamId}")
    public  ResponseEntity<TeamDTO> getTeam(@PathVariable("companyId") @Min(1) Long companyId, @PathVariable("teamId") @Min(1) Long teamId){
        TeamDTO teamDTO = teamService.getTeam(teamId);
        return new ResponseEntity<>(teamDTO, HttpStatus.OK);
    }
}