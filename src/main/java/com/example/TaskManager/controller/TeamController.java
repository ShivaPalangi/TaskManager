package com.example.TaskManager.controller;

import com.example.TaskManager.dto.TeamDTO;
import com.example.TaskManager.service.TeamService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@AllArgsConstructor
public class TeamController {
    private TeamService teamService;

    @PostMapping("/company/{id}/addTeam")
    public ResponseEntity<TeamDTO> addTeam(@Valid @RequestBody TeamDTO teamDTO, @PathVariable Long id){
        TeamDTO createdTeam = teamService.addTeam(teamDTO, id);
        return new ResponseEntity<>(createdTeam, HttpStatus.CREATED);
    }

    @PatchMapping("updateTeam/{id}")
    public ResponseEntity<TeamDTO> updateTeam(@Valid @RequestBody TeamDTO teamDTO, @PathVariable Long id){
        teamDTO.setId(id);
        TeamDTO updatedTeam = teamService.updateTeam(teamDTO);
        return new ResponseEntity<>(updatedTeam, HttpStatus.OK);
    }

}