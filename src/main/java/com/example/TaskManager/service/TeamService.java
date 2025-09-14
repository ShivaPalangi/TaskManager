package com.example.TaskManager.service;

import com.example.TaskManager.dto.TeamDTO;
import com.example.TaskManager.entity.Company;
import com.example.TaskManager.entity.Team;
import com.example.TaskManager.entity.User;
import com.example.TaskManager.enums.MembershipRoles;
import com.example.TaskManager.exception.ResourceNotFoundException;
import com.example.TaskManager.mapper.TeamMapper;
import com.example.TaskManager.repository.CompanyRepository;
import com.example.TaskManager.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TeamService  {
    private final TeamRepository teamRepository;
    private final CompanyRepository companyRepository;
    private final MembershipService membershipService;



    public TeamDTO addTeam(TeamDTO teamDTO, Long companyId, User user) {
        if ( !companyRepository.existsById(companyId))
            throw new ResourceNotFoundException("Company with id %d not found".formatted(companyId));
        Team team = TeamMapper.mapToTeamEntity(teamDTO);
        Team savedTeam = teamRepository.save(team);
        membershipService.createMembership(team, user, MembershipRoles.OWNER);
        return TeamMapper.mapToTeamDTO(savedTeam);
    }


    public TeamDTO getTeam(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new ResourceNotFoundException("Team with id %d not found".formatted(teamId)));
        return TeamMapper.mapToTeamDTO(team);
    }



    public TeamDTO updateTeam(TeamDTO teamDTO) {
        Team teamToUpdate = teamRepository.findByIdAndCompanyId(teamDTO.getId(), teamDTO.getCompanyId()).orElseThrow(
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