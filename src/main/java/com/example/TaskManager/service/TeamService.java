package com.example.TaskManager.service;

import com.example.TaskManager.dto.TeamDTO;
import com.example.TaskManager.entity.Team;
import com.example.TaskManager.entity.User;
import com.example.TaskManager.enums.MembershipRoles;
import com.example.TaskManager.exception.ResourceNotFoundException;
import com.example.TaskManager.mapper.TeamMapper;
import com.example.TaskManager.repository.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TeamService  {
    private final TeamRepository teamRepository;
    private final MembershipService membershipService;
    private final TeamMapper teamMapper;



    @Transactional
    public TeamDTO addTeam(TeamDTO teamDTO, User user) {
        Team team = teamMapper.mapToTeamEntity(teamDTO);
        Team savedTeam = teamRepository.save(team);
        membershipService.createMembership(team, user, MembershipRoles.OWNER);
        return teamMapper.mapToTeamDTO(savedTeam);
    }



    public TeamDTO getTeam(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new ResourceNotFoundException("Team with id %d not found".formatted(teamId)));
        return teamMapper.mapToTeamDTO(team);
    }




    @Transactional
    public TeamDTO updateTeam(TeamDTO teamDTO) {
        Team teamToUpdate = teamRepository.findByIdAndCompanyId(teamDTO.getId(), teamDTO.getCompanyId()).orElseThrow(
                () -> new ResourceNotFoundException("Team with id %d not found".formatted(teamDTO.getId())));
        UpdateTeamEntityFromDTO(teamToUpdate, teamDTO);
        Team savedTeam = teamRepository.save(teamToUpdate);
        return teamMapper.mapToTeamDTO(savedTeam);
    }

    private void UpdateTeamEntityFromDTO(Team teamToUpdate, TeamDTO teamDTO) {
        if (teamDTO.getName() != null)
            teamToUpdate.setName(teamDTO.getName());
        if (teamDTO.getDescription() != null)
            teamToUpdate.setDescription(teamDTO.getDescription());
    }




    public void deleteTeam(Long teamId) {
        teamRepository.deleteById(teamId);
    }




    public List<TeamDTO> getTeams(Long companyId) {
        List<Team> teams = teamRepository.findAllByCompanyId(companyId);
        return teams
                .stream()
                .sorted(Comparator.comparing(Team::getName))
                .map(teamMapper::mapToTeamDTO)
                .collect(Collectors.toList());
    }



    public List<TeamDTO> searchTeams(Long companyId, String name) {
        List<Team> teams = teamRepository.findAllByCompanyIdAndNameContainingIgnoreCase(companyId, name);
        return teams
                .stream()
                .sorted(Comparator.comparing(Team::getName))
                .map(teamMapper::mapToTeamDTO)
                .collect(Collectors.toList());
    }
}