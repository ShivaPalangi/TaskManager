package com.example.TaskManager.service;

import com.example.TaskManager.dto.TeamDTO;

public interface TeamService {
    TeamDTO updateTeam(TeamDTO teamDTO);

    TeamDTO addTeam(TeamDTO teamDTO, Long teamId);

    TeamDTO getTeam(Long teamId);
}