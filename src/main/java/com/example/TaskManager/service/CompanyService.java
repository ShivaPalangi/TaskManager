package com.example.TaskManager.service;

import com.example.TaskManager.dto.CompanyDTO;
import com.example.TaskManager.entity.Company;
import com.example.TaskManager.entity.User;
import com.example.TaskManager.exception.ResourceNotFoundException;
import com.example.TaskManager.mapper.CompanyMapper;
import com.example.TaskManager.repository.CompanyRepository;
import com.example.TaskManager.repository.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;


    public CompanyDTO addCompany(CompanyDTO companyDTO, User user) {
        Company company = CompanyMapper.mapToCompanyEntity(companyDTO);
        company.setOwner(user);
        company = companyRepository.save(company);
        return CompanyMapper.mapToCompanyDTO(company);
    }


    public CompanyDTO updateCompany(CompanyDTO companyDTO) {
        Company companyToUpdate = companyRepository.findById(companyDTO.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Company with id %d not found".formatted(companyDTO.getId())));
        updateCompanyEntityFromDTO(companyToUpdate, companyDTO);
        Company savedCompany = companyRepository.save(companyToUpdate);
        return CompanyMapper.mapToCompanyDTO(savedCompany);
    }


    public CompanyDTO getCompany(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Company with id %d not found".formatted(id)));
        return CompanyMapper.mapToCompanyDTO(company);
    }

    private void updateCompanyEntityFromDTO(Company companyToUpdate, CompanyDTO companyDTO) {
        if (companyDTO.getName() != null)
            companyToUpdate.setName(companyDTO.getName());
        if (companyDTO.getDescription() != null)
            companyToUpdate.setDescription(companyDTO.getDescription());
    }

    public void deleteCompany(Long id) {
        if (companyRepository.existsById(id))
            companyRepository.deleteById(id);
        else
            throw new ResourceNotFoundException("Company with id %d not found".formatted(id));
    }

    public List<CompanyDTO> getAllCompanies(User user) {
        List<Company> companies = companyRepository.findCompaniesByUser(user);
        return companies.stream().map(CompanyMapper::mapToCompanyDTO).collect(Collectors.toList());
    }

    public List<CompanyDTO> searchCompanies(User user, String title) {
        List<Company> companies = companyRepository.findCompaniesByUserAndNameContaining(user, title);
        return companies.stream().map(CompanyMapper::mapToCompanyDTO).collect(Collectors.toList());
    }
}