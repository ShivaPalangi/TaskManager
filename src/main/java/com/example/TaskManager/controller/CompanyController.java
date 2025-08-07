package com.example.TaskManager.controller;

import com.example.TaskManager.dto.CompanyDTO;
import com.example.TaskManager.service.CompanyService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("company")
@AllArgsConstructor
public class CompanyController {
    private CompanyService companyService;


    @PostMapping("addCompany")
    public ResponseEntity<CompanyDTO> addCompany(@Valid @RequestBody CompanyDTO companyDTO){
        CompanyDTO createdCompany = companyService.addCompany(companyDTO);
        return new ResponseEntity<>(createdCompany, HttpStatus.CREATED);
    }

    @PatchMapping("updateCompany/{id}")
    public ResponseEntity<CompanyDTO> updateCompany(@PathVariable Long id, @Valid @RequestBody CompanyDTO companyDTO){
        companyDTO.setId(id);
        CompanyDTO updatedCompany = companyService.updateCompany(companyDTO);
        return new ResponseEntity<>(updatedCompany, HttpStatus.OK);
    }


    @GetMapping("{id}")
    public ResponseEntity<CompanyDTO> getCompanyDetails(@PathVariable Long id){
        CompanyDTO companyDTO = companyService.getCompany(id);
        return new ResponseEntity<>(companyDTO, HttpStatus.OK);
    }
}