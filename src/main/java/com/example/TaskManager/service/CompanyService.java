package com.example.TaskManager.service;

import com.example.TaskManager.dto.CompanyDTO;
import com.example.TaskManager.entity.Company;
import com.example.TaskManager.exception.ResourceNotFoundException;
import com.example.TaskManager.mapper.CompanyMapper;
import com.example.TaskManager.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CompanyService {

    private CompanyRepository companyRepository;


    public CompanyDTO addCompany(CompanyDTO companyDTO) {
        Company company = CompanyMapper.mapToCompanyEntity(companyDTO);
        company = companyRepository.save(company);
        return CompanyMapper.mapToCompanyDTO(company);
    }


    public CompanyDTO updateCompany(CompanyDTO companyDTO) {
        Company companyToUpdate = companyRepository.findById(companyDTO.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Company", "Id", companyDTO.getId()));
        updateCompanyEntityFromDTO(companyToUpdate, companyDTO);
        Company savedCompany = companyRepository.save(companyToUpdate);
        return CompanyMapper.mapToCompanyDTO(savedCompany);
    }


    public CompanyDTO getCompany(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Company", "Id", id));
        return CompanyMapper.mapToCompanyDTO(company);
    }

    private void updateCompanyEntityFromDTO(Company companyToUpdate, CompanyDTO companyDTO) {
        if (companyDTO.getName() != null)
            companyToUpdate.setName(companyDTO.getName());
        if (companyDTO.getDescription() != null)
            companyToUpdate.setDescription(companyDTO.getDescription());
    }

}