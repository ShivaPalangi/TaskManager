package com.example.TaskManager.service;

import com.example.TaskManager.dto.MembershipDTO;
import com.example.TaskManager.dto.MembershipRequest;
import com.example.TaskManager.entity.Membership;
import com.example.TaskManager.entity.Task;
import com.example.TaskManager.entity.Team;
import com.example.TaskManager.entity.User;
import com.example.TaskManager.enums.MembershipRoles;
import com.example.TaskManager.enums.TaskStatusTypes;
import com.example.TaskManager.exception.ResourceNotFoundException;
import com.example.TaskManager.mapper.MembershipMapper;
import com.example.TaskManager.repository.MembershipRepository;
import com.example.TaskManager.repository.TeamRepository;
import com.example.TaskManager.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MembershipService {
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final MembershipMapper membershipMapper;


    @Transactional
    public Membership createMembership(Team team, User user, MembershipRoles role) {
        Membership membership = new Membership();
        membership.setTeam(team);
        membership.setEmployee(user);
        membership.setRole(role);
        membershipRepository.save(membership);
        return membership;
    }



    @Transactional
    public Map<String, Object> addAdmin(Long teamId, Long memberId) {
        Membership membership = membershipRepository.findByIdAndTeamId(memberId, teamId).orElseThrow(
                () -> new ResourceNotFoundException("Member with id %d is not the member of this team".formatted(memberId)));
        if (membership.getRole() == MembershipRoles.MEMBER) {
            membership.setRole(MembershipRoles.ADMIN);
            membershipRepository.save(membership);
            return Map.of("message", "Membership promoted to admin.", "data", membershipMapper.mapToMembershipDTO(membership));
        }
        return Map.of("message", "Membership is already Admin", "data", membershipMapper.mapToMembershipDTO(membership));
    }



    @Transactional
    public Map<String, Object> addMember(MembershipRequest membershipRequest) {
        User user = userRepository.findByEmailAddress(membershipRequest.getUsername()).orElseThrow(
                () -> new ResourceNotFoundException("User with email address %s not found".formatted(membershipRequest.getUsername())));
        Team team = teamRepository.findById(membershipRequest.getTeamId()).orElseThrow(
                () -> new ResourceNotFoundException("Team with id %d not found".formatted(membershipRequest.getTeamId())));

        if ( !membershipRepository.existsByEmployeeIdAndTeamId(user.getId(), team.getId()))
            return Map.of("message", "Membership created successfully",
                    "data", membershipMapper.mapToMembershipDTO(createMembership(team, user, MembershipRoles.MEMBER)));
        return Map.of("message", "this user is already the member of this team",
                "data", membershipMapper.mapToMembershipDTO(membershipRepository.findByEmployeeAndTeam(user, team).get()));
    }



    @Transactional
    public String deleteMember(Long teamId, Long memberId, User user) {
        Membership memberToDelete = membershipRepository.findByIdAndTeamId(memberId, teamId).orElseThrow(
                () -> new ResourceNotFoundException("Member with id %d is not the member of this team".formatted(memberId)));
        Membership membership = membershipRepository.findByEmployeeAndTeamId(user, teamId).orElseThrow(
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

        List<Task> filteredAssignedTasks = membership.getAssignedTasks().stream()
                .filter(task -> task.getTaskStatus() != TaskStatusTypes.DELETED)
                .collect(Collectors.toList());
        List<Task> filteredCreatedTasks = membership.getCreatedTasks().stream()
                .filter(task -> TaskService.NOT_COMPLETED_STATUSES.contains(task.getTaskStatus()) )
                .sorted(Comparator.comparing(Task::getStartTime).reversed())
                .collect(Collectors.toList());
        membership.setAssignedTasks(filteredAssignedTasks);
        membership.setCreatedTasks(filteredCreatedTasks);
        return membershipMapper.mapToMembershipDTO(membership);
    }



    public List<MembershipDTO> getTeamMembers(Long teamId) {
        List<Membership> memberships = membershipRepository.findAllByTeamId(teamId);
        return memberships.stream().map(membershipMapper::mapToMembershipDTO).collect(Collectors.toList());
    }



    public List<MembershipDTO> searchTeamMembers(Long teamId, String title) {
        List<Membership> memberships = membershipRepository.findByTeamIdAndEmployeeNameContaining(teamId, title);
        return memberships.stream().map(membershipMapper::mapToMembershipDTO).collect(Collectors.toList());
    }
}
