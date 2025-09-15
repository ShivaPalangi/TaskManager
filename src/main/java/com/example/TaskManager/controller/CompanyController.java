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
import java.util.List;

@RestController
@RequestMapping("api/v1/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    private final PermissionService permissionService;


    @PostMapping
    public ResponseEntity<CompanyDTO> addCompany(
            @Validated(ValidationGroups.Create.class) @RequestBody CompanyDTO companyDTO,
            @AuthenticationPrincipal User user){
        CompanyDTO createdCompany = companyService.addCompany(companyDTO, user);
        return new ResponseEntity<>(createdCompany, HttpStatus.CREATED);
    }

    @PatchMapping("{id}")
    public ResponseEntity<CompanyDTO> updateCompany(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody CompanyDTO companyDTO,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.canManageCompany(user, id))
            throw new AccessDeniedException("Only company owner can update company.");

        companyDTO.setId(id);
        CompanyDTO updatedCompany = companyService.updateCompany(companyDTO);
        return new ResponseEntity<>(updatedCompany, HttpStatus.OK);
    }


    @GetMapping("{id}")
    public ResponseEntity<CompanyDTO> getCompanyDetails(
            @PathVariable @Min(1) Long id,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !(permissionService.canManageCompany(user, id) || permissionService.isMemberOfCompany(user, id)) )
            throw new AccessDeniedException("You are not the member of this company.");

        CompanyDTO companyDTO = companyService.getCompany(id);
        return new ResponseEntity<>(companyDTO, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delectCompany(
            @PathVariable @Min(1) Long id,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.canManageCompany(user, id))
            throw new AccessDeniedException("Only company owner can delete this company.");

        companyService.deleteCompany(id);
        return new ResponseEntity<>("Company successfully deleted.", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CompanyDTO>> getAllCompanies(@AuthenticationPrincipal User user) {
        List<CompanyDTO> companyDTOList = companyService.getAllCompanies(user);
        return new ResponseEntity<>(companyDTOList, HttpStatus.OK);
    }


    @GetMapping("search")
    public ResponseEntity<List<CompanyDTO>> searchCompany(
            @RequestParam String title,
            @AuthenticationPrincipal User user){
        List<CompanyDTO> companyDTOList = companyService.searchCompanies(user, title);
        return new ResponseEntity<>(companyDTOList, HttpStatus.OK);
    }
}