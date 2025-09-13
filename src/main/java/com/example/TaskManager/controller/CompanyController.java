package com.example.TaskManager.controller;

import com.example.TaskManager.dto.CompanyDTO;
import com.example.TaskManager.service.CompanyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;


    @PostMapping("add-company")
    public ResponseEntity<CompanyDTO> addCompany(@Valid @RequestBody CompanyDTO companyDTO){
        CompanyDTO createdCompany = companyService.addCompany(companyDTO);
        return new ResponseEntity<>(createdCompany, HttpStatus.CREATED);
    }

    @PatchMapping("company/{id}/update")
    public ResponseEntity<CompanyDTO> updateCompany(@PathVariable @Min(1) Long id, @Valid @RequestBody CompanyDTO companyDTO){
        companyDTO.setId(id);
        CompanyDTO updatedCompany = companyService.updateCompany(companyDTO);
        return new ResponseEntity<>(updatedCompany, HttpStatus.OK);
    }


    @GetMapping("company/{id}")
    public ResponseEntity<CompanyDTO> getCompanyDetails(@PathVariable @Min(1) Long id){
        CompanyDTO companyDTO = companyService.getCompany(id);
        return new ResponseEntity<>(companyDTO, HttpStatus.OK);
    }
}