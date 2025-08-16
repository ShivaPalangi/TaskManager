package com.example.TaskManager.controller;

import com.example.TaskManager.dto.CompanyDTO;
import com.example.TaskManager.dto.TeamDTO;
import com.example.TaskManager.service.CompanyService;
import com.example.TaskManager.service.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("company")
@AllArgsConstructor
public class TeamController {
    private TeamService teamService;
    private CompanyService companyService;

    @PostMapping("{id}/add-team")
    public ResponseEntity<TeamDTO> addTeam(@RequestBody TeamDTO teamDTO, @PathVariable Long id){
        TeamDTO createdTeam = teamService.addTeam(teamDTO, id);
        return new ResponseEntity<>(createdTeam, HttpStatus.CREATED);
    }

    @PatchMapping("{companyId}/team/{teamId}/update")
    public ResponseEntity<TeamDTO> updateTeam(@RequestBody TeamDTO teamDTO, @PathVariable("companyId") Long companyId, @PathVariable("teamId") Long teamId){
        teamDTO.setId(teamId);
        TeamDTO updatedTeam = teamService.updateTeam(teamDTO);
        return new ResponseEntity<>(updatedTeam, HttpStatus.OK);
    }

    @GetMapping("{companyId}/team/{teamId}")
    public  ResponseEntity<TeamDTO> getTeam(@PathVariable("companyId") Long companyId, @PathVariable("teamId") Long teamId){
        TeamDTO teamDTO = teamService.getTeam(teamId);
        return new ResponseEntity<>(teamDTO, HttpStatus.OK);
    }
}