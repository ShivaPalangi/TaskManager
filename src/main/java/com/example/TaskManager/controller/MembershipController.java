package com.example.TaskManager.controller;

import com.example.TaskManager.dto.MembershipDTO;
import com.example.TaskManager.entity.User;
import com.example.TaskManager.service.MembershipService;
import com.example.TaskManager.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("api/v1/company/{companyId}/team/{teamId}/members")
@RequiredArgsConstructor
public class MembershipController {
    private final PermissionService permissionService;
    private final MembershipService membershipService;

    @PostMapping("{memberId}/add-admin")
    public ResponseEntity<MembershipDTO> addTeamAdmin(
            @PathVariable("companyId") Long companyId,
            @PathVariable("teamId") Long teamId,
            @PathVariable("memberId") Long memberId,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.canManageCompany(user, companyId))
            throw new AccessDeniedException("You are not the owner of this company");

        MembershipDTO newMembership = membershipService.addAdmin(teamId, memberId);
        return new ResponseEntity<>(newMembership,HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<MembershipDTO> addTeamMember(
            @PathVariable("companyId") Long companyId,
            @PathVariable("teamId") Long teamId,
            @RequestBody String emailAddress,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.canManageTeam(user, teamId))
            throw new AccessDeniedException("You are not allowed to manage this team");

        MembershipDTO newMembership = membershipService.addMember(teamId, emailAddress);
        return new ResponseEntity<>(newMembership,HttpStatus.CREATED);
    }


    @DeleteMapping("{memberId}")
    public ResponseEntity<String> deleteTeamMember(
            @PathVariable("companyId") Long companyId,
            @PathVariable("teamId") Long teamId,
            @PathVariable("memberId") Long memberId,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if (permissionService.canManageTeam(user, teamId))
            throw new AccessDeniedException("You can't delete any member of this team");

        String response = membershipService.deleteMember(teamId, memberId, user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("{memberId}")
    public ResponseEntity<MembershipDTO> getTeamMember(
            @PathVariable("companyId") Long companyId,
            @PathVariable("teamId") Long teamId,
            @PathVariable("memberId") Long memberId,
            @AuthenticationPrincipal User user
    ) throws AccessDeniedException {

        if (permissionService.isMemberOfTeam(user, teamId))
            throw new AccessDeniedException("You are not the member of this team");

        MembershipDTO membershipDTO = membershipService.getMembership(teamId, memberId, companyId);
        return new ResponseEntity<>(membershipDTO, HttpStatus.OK);
    }
}