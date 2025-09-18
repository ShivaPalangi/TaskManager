package com.example.TaskManager.mapper;

import com.example.TaskManager.dto.TeamDTO;
import com.example.TaskManager.entity.Team;
import com.example.TaskManager.exception.ResourceNotFoundException;
import com.example.TaskManager.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class TeamMapper {
    private final CompanyRepository companyRepository;
    private final MembershipMapper membershipMapper;

    public TeamDTO mapToTeamDTO(Team team){
        if (team == null) return null;

        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setId(team.getId());
        teamDTO.setName(team.getName());
        teamDTO.setDescription(team.getDescription());
        if ( team.getCompany() != null )
            teamDTO.setCompanyId(team.getCompany().getId());
        if ( team.getMemberships() != null && !team.getMemberships().isEmpty() )
            teamDTO.setMemberships(team.getMemberships().stream().map(membershipMapper::mapToMembershipDTO).collect(Collectors.toList()));
        return teamDTO;
    }


    public Team mapToTeamEntity(TeamDTO teamDTO){
        if (teamDTO == null) return null;

        Team team = new Team();
        team.setId(teamDTO.getId());
        team.setName(teamDTO.getName());
        team.setDescription(teamDTO.getDescription());
        if (teamDTO.getCompanyId() != null)
            team.setCompany(companyRepository.findById(teamDTO.getCompanyId()).orElseThrow(
                    () -> new ResourceNotFoundException("Company not found with id " + teamDTO.getCompanyId())));
        if ( teamDTO.getMemberships() != null && !teamDTO.getMemberships().isEmpty() )
            team.setMemberships(teamDTO.getMemberships().stream().map(membershipMapper::mapToMembershipEntity).collect(Collectors.toList()));
        return team;
    }
}