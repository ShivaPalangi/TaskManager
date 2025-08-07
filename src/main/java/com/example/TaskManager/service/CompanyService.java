package com.example.TaskManager.service;

import com.example.TaskManager.dto.CompanyDTO;

public interface CompanyService {
    CompanyDTO addCompany(CompanyDTO companyDTO);

    CompanyDTO updateCompany(CompanyDTO companyDTO);

    CompanyDTO getCompany(Long id);
}