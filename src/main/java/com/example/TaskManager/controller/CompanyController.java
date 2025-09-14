package com.example.TaskManager.controller;

import com.example.TaskManager.dto.CompanyDTO;
import com.example.TaskManager.entity.User;
import com.example.TaskManager.service.CompanyService;
import com.example.TaskManager.service.PermissionService;
import com.example.TaskManager.validation.ValidationGroups;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("api/v1/company")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    private final PermissionService permissionService;


    @PostMapping("add-company")
    public ResponseEntity<CompanyDTO> addCompany(
            @Validated(ValidationGroups.Create.class) @RequestBody CompanyDTO companyDTO,
            @AuthenticationPrincipal User user){
        CompanyDTO createdCompany = companyService.addCompany(companyDTO, user);
        return new ResponseEntity<>(createdCompany, HttpStatus.CREATED);
    }

    @PatchMapping("{id}/update")
    public ResponseEntity<CompanyDTO> updateCompany(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody CompanyDTO companyDTO,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.canManageCompany(user, id))
            throw new AccessDeniedException("Only owner can update company");

        companyDTO.setId(id);
        CompanyDTO updatedCompany = companyService.updateCompany(companyDTO);
        return new ResponseEntity<>(updatedCompany, HttpStatus.OK);
    }


    @GetMapping("{id}")
    public ResponseEntity<CompanyDTO> getCompanyDetails(@PathVariable @Min(1) Long id, @AuthenticationPrincipal User user) throws AccessDeniedException {
        if ( !(permissionService.canManageCompany(user, id) || permissionService.isMemberOfCompany(user, id)) )
            throw new AccessDeniedException("You are not the member of this company");

        CompanyDTO companyDTO = companyService.getCompany(id);
        return new ResponseEntity<>(companyDTO, HttpStatus.OK);
    }
}