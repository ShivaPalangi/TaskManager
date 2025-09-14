package com.example.TaskManager.service;

import com.example.TaskManager.dto.MembershipDTO;
import com.example.TaskManager.entity.Membership;
import com.example.TaskManager.entity.Team;
import com.example.TaskManager.entity.User;
import com.example.TaskManager.enums.MembershipRoles;
import com.example.TaskManager.exception.ResourceNotFoundException;
import com.example.TaskManager.mapper.MembershipMapper;
import com.example.TaskManager.repository.MembershipRepository;
import com.example.TaskManager.repository.TeamRepository;
import com.example.TaskManager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MembershipService {
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    public Membership createMembership(Team team, User user, MembershipRoles role) {
        Membership membership = new Membership();
        membership.setTeam(team);
        membership.setEmployee(user);
        membership.setRole(role);
        membershipRepository.save(membership);
        return membership;
    }

    public MembershipDTO addAdmin(Long teamId, Long memberId) {
        Membership membership = membershipRepository.findByIdAndTeamId(memberId, teamId).orElseThrow(
                () -> new ResourceNotFoundException("Member with id %d is not the member of this team".formatted(memberId)));
        if (membership.getRole() == MembershipRoles.MEMBER) {
            membership.setRole(MembershipRoles.ADMIN);
            membershipRepository.save(membership);
        }
        return MembershipMapper.mapToMembershipDTO(membership);
    }

    public MembershipDTO addMember(Long teamId, String emailAddress) {
        User user = userRepository.findByEmailAddress(emailAddress).orElseThrow(
                () -> new ResourceNotFoundException("User with email address %s not found".formatted(emailAddress)));
        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new ResourceNotFoundException("Team with id %d not found".formatted(teamId)));
        return MembershipMapper.mapToMembershipDTO(createMembership(team, user, MembershipRoles.MEMBER));
    }


    public String deleteMember(Long teamId, Long memberId, User user) {
        Membership memberToDelete = membershipRepository.findByIdAndTeamId(memberId, teamId).orElseThrow(
                () -> new ResourceNotFoundException("Member with id %d is not the member of this team".formatted(memberId)));
        Membership membership = membershipRepository.findByEmployee(user).orElseThrow(
                () -> new ResourceNotFoundException("Member with id %d is not the member of this team".formatted(memberId)));
        if ( !(memberToDelete.getRole() == MembershipRoles.OWNER))
            if ( (membership.getRole() == MembershipRoles.OWNER)
               || (membership.getRole() == MembershipRoles.ADMIN && memberToDelete.getRole() == MembershipRoles.MEMBER)) {
                membershipRepository.delete(memberToDelete);
                return "Member successfully deleted from team";
            }
        return "You can't delete this member";
    }


    public MembershipDTO getMembership(Long teamId, Long memberId, Long companyId) {
        teamRepository.findByIdAndCompanyId(teamId, companyId).orElseThrow(
                () -> new ResourceNotFoundException("Company with id %d haven't any team with id %d".formatted(companyId, teamId)));
        Membership membership = membershipRepository.findByIdAndTeamId(memberId, teamId).orElseThrow(
                () -> new ResourceNotFoundException("Member with id %d is not the member of this team".formatted(memberId)));
        return MembershipMapper.mapToMembershipDTO(membership);
    }

}
