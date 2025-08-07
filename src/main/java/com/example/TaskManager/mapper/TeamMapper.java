package com.example.TaskManager.mapper;

import com.example.TaskManager.dto.TeamDTO;
import com.example.TaskManager.entity.Team;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class TeamMapper {

    public static TeamDTO mapToTeamDTO(Team team){
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setId(team.getId());
        teamDTO.setName(team.getName());
        teamDTO.setDescription(team.getDescription());
        if (team.getCompany() != null)
            teamDTO.setCompany(CompanyMapper.mapToCompanyDTO(team.getCompany()));
        if ( team.getMemberships() != null && !team.getMemberships().isEmpty())
            teamDTO.setMemberships(team.getMemberships().stream().map(MembershipMapper::mapToMembershipDTO).collect(Collectors.toList()));
        return teamDTO;
    }


    public static Team mapToTeamEntity(TeamDTO teamDTO){
        Team team = new Team();
        team.setId(teamDTO.getId());
        team.setName(teamDTO.getName());
        team.setDescription(teamDTO.getDescription());
        if (teamDTO.getCompany() != null)
            team.setCompany(CompanyMapper.mapToCompanyEntity(teamDTO.getCompany()));
        if ( teamDTO.getMemberships() != null && !teamDTO.getMemberships().isEmpty())
            team.setMemberships(teamDTO.getMemberships().stream().map(MembershipMapper::mapToMembershipEntity).collect(Collectors.toList()));
        return team;
    }
}