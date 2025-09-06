package com.example.TaskManager.service;

import com.example.TaskManager.dto.TeamDTO;
import com.example.TaskManager.entity.Company;
import com.example.TaskManager.entity.Team;
import com.example.TaskManager.exception.ResourceNotFoundException;
import com.example.TaskManager.mapper.TeamMapper;
import com.example.TaskManager.repository.CompanyRepository;
import com.example.TaskManager.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TeamService  {
    private TeamRepository teamRepository;
    private CompanyRepository companyRepository;



    public TeamDTO addTeam(TeamDTO teamDTO, Long id) {
        Company company = companyRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Company with id %d not found".formatted(id)));
        Team team = TeamMapper.mapToTeamEntity(teamDTO);
        team.setCompany(company);
        Team savedTeam = teamRepository.save(team);
        return TeamMapper.mapToTeamDTO(savedTeam);
    }


    public TeamDTO getTeam(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new ResourceNotFoundException("Team with id %d not found".formatted(teamId)));
        return TeamMapper.mapToTeamDTO(team);
    }



    public TeamDTO updateTeam(TeamDTO teamDTO) {
        Team teamToUpdate = teamRepository.findById(teamDTO.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Team with id %d not found".formatted(teamDTO.getId())));
        UpdateTeamEntityFromDTO(teamToUpdate, teamDTO);
        Team savedTeam = teamRepository.save(teamToUpdate);
        return TeamMapper.mapToTeamDTO(savedTeam);
    }

    private void UpdateTeamEntityFromDTO(Team teamToUpdate, TeamDTO teamDTO) {
        if (teamDTO.getName() != null)
            teamToUpdate.setName(teamDTO.getName());
        if (teamDTO.getDescription() != null)
            teamToUpdate.setDescription(teamDTO.getDescription());
    }
}