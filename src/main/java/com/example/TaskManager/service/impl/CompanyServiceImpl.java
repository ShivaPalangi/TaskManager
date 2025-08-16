package com.example.TaskManager.service.impl;

import com.example.TaskManager.dto.CompanyDTO;
import com.example.TaskManager.entity.Company;
import com.example.TaskManager.mapper.CompanyMapper;
import com.example.TaskManager.repository.CompanyRepository;
import com.example.TaskManager.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private CompanyRepository companyRepository;

    @Override
    public CompanyDTO addCompany(CompanyDTO companyDTO) {
        Company company = CompanyMapper.mapToCompanyEntity(companyDTO);
        company = companyRepository.save(company);
        return CompanyMapper.mapToCompanyDTO(company);
    }

    @Override
    public CompanyDTO updateCompany(CompanyDTO companyDTO) {
        Optional<Company> companyOptional = companyRepository.findById(companyDTO.getId());
        Company companyToUpdate = companyOptional.get();
        updateCompanyEntityFromDTO(companyToUpdate, companyDTO);
        Company savedCompany = companyRepository.save(companyToUpdate);
        return CompanyMapper.mapToCompanyDTO(savedCompany);
    }

    @Override
    public CompanyDTO getCompany(Long id) {
        Optional<Company> companyOptional = companyRepository.findById(id);
        return CompanyMapper.mapToCompanyDTO(companyOptional.get());
    }

    private void updateCompanyEntityFromDTO(Company companyToUpdate, CompanyDTO companyDTO) {
        if (companyDTO.getName() != null)
            companyToUpdate.setName(companyDTO.getName());
        if (companyDTO.getDescription() != null)
            companyToUpdate.setDescription(companyDTO.getDescription());
    }
}