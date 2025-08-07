package com.example.TaskManager.service.impl;

import com.example.TaskManager.dto.TeamDTO;
import com.example.TaskManager.entity.Company;
import com.example.TaskManager.entity.Team;
import com.example.TaskManager.mapper.TeamMapper;
import com.example.TaskManager.repository.CompanyRepository;
import com.example.TaskManager.repository.TeamRepository;
import com.example.TaskManager.service.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TeamServiceImpl implements TeamService {
    private TeamRepository teamRepository;
    private CompanyRepository companyRepository;



    @Override
    public TeamDTO addTeam(TeamDTO teamDTO, Long id) {
        Optional<Company> companyOptional = companyRepository.findById(id);
        Company company = companyOptional.get();
        Team team = TeamMapper.mapToTeamEntity(teamDTO);
        team.setCompany(company);
        Team savedTeam = teamRepository.save(team);
        return TeamMapper.mapToTeamDTO(savedTeam);
    }


    @Override
    public TeamDTO updateTeam(TeamDTO teamDTO) {
        Optional<Team> teamOptional = teamRepository.findById(teamDTO.getId());
        Team teamToUpdate = teamOptional.get();
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