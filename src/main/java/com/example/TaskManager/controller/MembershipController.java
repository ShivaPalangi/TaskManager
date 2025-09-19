package com.example.TaskManager.controller;

import com.example.TaskManager.dto.MembershipDTO;
import com.example.TaskManager.dto.MembershipRequest;
import com.example.TaskManager.entity.User;
import com.example.TaskManager.service.MembershipService;
import com.example.TaskManager.service.PermissionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/companies/{companyId}/teams/{teamId}/members")
@RequiredArgsConstructor
public class MembershipController {
    private final PermissionService permissionService;
    private final MembershipService membershipService;

    @PostMapping("{memberId}/add-admin")
    public ResponseEntity<?> addTeamAdmin(
            @Min(1) @PathVariable("companyId") Long companyId,
            @Min(1) @PathVariable("teamId") Long teamId,
            @Min(1) @PathVariable("memberId") Long memberId,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.canManageCompany(user, companyId))
            throw new AccessDeniedException("You are not the owner of this company");

        Map<String, Object> newMembership = membershipService.addAdmin(teamId, memberId);
        return ResponseEntity.ok(newMembership);
    }




    @PostMapping
    public ResponseEntity<?> addTeamMember(
            @Min(1) @PathVariable("companyId") Long companyId,
            @Min(1) @PathVariable("teamId") Long teamId,
            @Valid @RequestBody MembershipRequest membershipRequest,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.canManageTeam(user, teamId, companyId))
            throw new AccessDeniedException("You are not allowed to manage this team");

        membershipRequest.setTeamId(teamId);
        Map<String, Object> newMembership = membershipService.addMember(membershipRequest);

        if (newMembership.get("message").equals("Membership created successfully")) {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand( ((MembershipDTO) newMembership.get("data")).getId())
                    .toUri();

            return ResponseEntity.created(location).body(newMembership);
        }
        return ResponseEntity.ok(newMembership);
    }




    @DeleteMapping("{memberId}")
    public ResponseEntity<?> deleteTeamMember(
            @Min(1) @PathVariable("companyId") Long companyId,
            @Min(1) @PathVariable("teamId") Long teamId,
            @Min(1) @PathVariable("memberId") Long memberId,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.canManageTeam(user, teamId, companyId))
            throw new AccessDeniedException("You can't delete any member of this team");

        String result = membershipService.deleteMember(teamId, memberId, user);

        String redirectUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("api/v1/company/{companyId}/team/{teamId}/members")
                .buildAndExpand(companyId, teamId)
                .toUriString();

        Map<String, Object> response = Map.of(
                "redirect", true,
                "redirectUrl", redirectUrl,
                "message", result
        );
        return ResponseEntity.ok(response);
    }




    @GetMapping("{memberId}")
    public ResponseEntity<MembershipDTO> getTeamMember(
            @Min(1) @PathVariable("companyId") Long companyId,
            @Min(1) @PathVariable("teamId") Long teamId,
            @Min(1) @PathVariable("memberId") Long memberId,
            @AuthenticationPrincipal User user
    ) throws AccessDeniedException {

        if ( !permissionService.isMemberOfTeam(user, teamId, companyId))
            throw new AccessDeniedException("You are not the member of this team");

        MembershipDTO membershipDTO = membershipService.getMembership(teamId, memberId, companyId);
        return ResponseEntity.ok(membershipDTO);
    }




    @GetMapping("search")
    public ResponseEntity<List<MembershipDTO>> searchTeamMembers(
            @RequestParam String title,
            @Min(1) @PathVariable("companyId") Long companyId,
            @Min(1) @PathVariable("teamId") Long teamId,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if (!permissionService.isMemberOfTeam(user, teamId, companyId))
            throw new AccessDeniedException("You are not the member of this team");

        List<MembershipDTO> members = membershipService.searchTeamMembers(teamId, title);
        return ResponseEntity.ok(members);
    }




    @GetMapping
    public ResponseEntity<List<MembershipDTO>> getTeamMembers(
            @Min(1) @PathVariable("companyId") Long companyId,
            @Min(1) @PathVariable("teamId") Long teamId,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if (!permissionService.isMemberOfTeam(user, teamId, companyId))
            throw new AccessDeniedException("You are not the member of this team");

        List<MembershipDTO> members = membershipService.getTeamMembers(teamId);
        return ResponseEntity.ok(members);
    }
}