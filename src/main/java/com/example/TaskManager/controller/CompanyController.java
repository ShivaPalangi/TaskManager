package com.example.TaskManager.controller;

import com.example.TaskManager.dto.CompanyDTO;
import com.example.TaskManager.entity.User;
import com.example.TaskManager.service.CompanyService;
import com.example.TaskManager.service.PermissionService;
import com.example.TaskManager.validation.ValidationGroups;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    private final PermissionService permissionService;


    @PostMapping
    public ResponseEntity<?> addCompany(
            @Validated(ValidationGroups.Create.class) @RequestBody CompanyDTO companyDTO,
            @AuthenticationPrincipal User user){
        CompanyDTO createdCompany = companyService.addCompany(companyDTO, user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdCompany.getId())
                .toUri();

        return ResponseEntity.created(location).body(
                Map.of("message", "Company created successfully", "data", createdCompany));
    }




    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCompany(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody CompanyDTO companyDTO,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.canManageCompany(user, id))
            throw new AccessDeniedException("Only company owner can update company.");

        companyDTO.setId(id);
        CompanyDTO updatedCompany = companyService.updateCompany(companyDTO);

        return ResponseEntity.ok(
                Map.of("message", "Company updated successfully", "data", updatedCompany));
    }




    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getCompanyDetails(
            @PathVariable @Min(1) Long id,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !(permissionService.canManageCompany(user, id) || permissionService.isMemberOfCompany(user, id)) )
            throw new AccessDeniedException("You are not the member of this company.");

        CompanyDTO companyDTO = companyService.getCompany(id);
        return ResponseEntity.ok(companyDTO);
    }




    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompany(
            @PathVariable @Min(1) Long id,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.canManageCompany(user, id))
            throw new AccessDeniedException("Only company owner can delete this company.");

        companyService.deleteCompany(id);

        String redirectUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath() // base url
                .path("/api/v1/companies")
                .build()
                .toUriString();

        Map<String, Object> response = Map.of(
                "redirect", true,
                "redirectUrl", redirectUrl,
                "message", "Company with id %d deleted successfully".formatted(id)
        );

        return ResponseEntity.ok(response);
    }




    @GetMapping
    public ResponseEntity<List<CompanyDTO>> getAllCompanies(@AuthenticationPrincipal User user) {
        List<CompanyDTO> companyDTOList = companyService.getAllCompanies(user);
        return ResponseEntity.ok(companyDTOList);
    }




    @GetMapping("/search")
    public ResponseEntity<List<CompanyDTO>> searchCompany(
            @RequestParam String title,
            @AuthenticationPrincipal User user){
        List<CompanyDTO> companyDTOList = companyService.searchCompanies(user, title);
        return ResponseEntity.ok(companyDTOList);
    }
}