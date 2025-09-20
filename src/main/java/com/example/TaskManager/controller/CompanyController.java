package com.example.TaskManager.controller;

import com.example.TaskManager.dto.CompanyDTO;
import com.example.TaskManager.entity.User;
import com.example.TaskManager.service.CompanyService;
import com.example.TaskManager.service.PermissionService;
import com.example.TaskManager.validation.ValidationGroups;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Company Management", description = "APIs for managing company")
@SecurityRequirement(name = "bearerAuth")
public class CompanyController {
    private final CompanyService companyService;
    private final PermissionService permissionService;


    @Operation(
            summary = "create new company",
            description = "create new company requires authentication"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "company created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request - Validation failed"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            )
    })
    @PostMapping
    public ResponseEntity<?> addCompany(
            @Parameter(description = "Company data to create", required = true)
            @Validated(ValidationGroups.Create.class) @RequestBody CompanyDTO companyDTO,

            @Parameter(description = "Authenticated user", hidden = true)
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




    @Operation(
            summary = "Update an existing company",
            description = "Only company owner can update company details."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Company updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request - validation failed"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - only company owner can update"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Company not found"
            )
    })
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCompany(
            @Parameter(description = "Company id",  required = true)
            @PathVariable @Min(1) Long id,

            @Parameter(description = "Company data to update",  required = true)
            @Valid @RequestBody CompanyDTO companyDTO,

            @Parameter(description = "Authenticated user", hidden = true)
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.canManageCompany(user, id))
            throw new AccessDeniedException("Only company owner can update company.");

        companyDTO.setId(id);
        CompanyDTO updatedCompany = companyService.updateCompany(companyDTO);

        return ResponseEntity.ok(
                Map.of("message", "Company updated successfully", "data", updatedCompany));
    }




    @Operation(
            summary = "Get existing company detail",
            description = "Only company member can access to company details."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "company details"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request - validation failed"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - only company members can access company detail"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Company not found"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getCompanyDetails(
            @Parameter(description = "Company id", required = true)
            @PathVariable @Min(1) Long id,

            @Parameter(description = "Authenticated user",  hidden = true)
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !(permissionService.canManageCompany(user, id) || permissionService.isMemberOfCompany(user, id)) )
            throw new AccessDeniedException("You are not the member of this company.");

        CompanyDTO companyDTO = companyService.getCompany(id);
        return ResponseEntity.ok(companyDTO);
    }




    @Operation(
            summary = "Delete existing company",
            description = "Only company owner can delete company."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "company deleted"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request - validation failed"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - only company owner can delete company"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Company not found"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompany(
            @Parameter(description = "Company id",  required = true)
            @PathVariable @Min(1) Long id,

            @Parameter(description = "Authenticated user",   hidden = true)
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.canManageCompany(user, id))
            throw new AccessDeniedException("Only company owner can delete this company.");

        companyService.deleteCompany(id);

        String redirectUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
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




    @Operation(
            summary = "Get all user companies",
            description = "the list of your companies"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of companies"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            )
    })
    @GetMapping
    public ResponseEntity<List<CompanyDTO>> getAllCompanies(
            @Parameter(description = "Authenticated user", hidden = true)
            @AuthenticationPrincipal User user) {
        List<CompanyDTO> companyDTOList = companyService.getAllCompanies(user);
        return ResponseEntity.ok(companyDTOList);
    }




    @Operation(
            summary = "Search compies",
            description = "List of your companies containing specific title"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of companies"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request - validation failed"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            )
    })
    @GetMapping("/search")
    public ResponseEntity<List<CompanyDTO>> searchCompany(
            @Parameter(description = "the name of companies you looking for",  required = true)
            @RequestParam String title,

            @Parameter(description = "Authenticated user", hidden = true)
            @AuthenticationPrincipal User user){
        List<CompanyDTO> companyDTOList = companyService.searchCompanies(user, title);
        return ResponseEntity.ok(companyDTOList);
    }
}