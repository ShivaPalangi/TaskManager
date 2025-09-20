package com.example.TaskManager.service;

import com.example.TaskManager.dto.CompanyDTO;
import com.example.TaskManager.entity.Company;
import com.example.TaskManager.entity.User;
import com.example.TaskManager.exception.ResourceNotFoundException;
import com.example.TaskManager.mapper.CompanyMapper;
import com.example.TaskManager.repository.CompanyRepository;
import com.example.TaskManager.repository.MembershipRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final MembershipRepository membershipRepository;
    private final CompanyMapper companyMapper;


    @Transactional
    public CompanyDTO addCompany( CompanyDTO companyDTO, User user) {
        Company company = companyMapper.mapToCompanyEntity(companyDTO);
        company.setOwner(user);
        companyRepository.save(company);
        return companyMapper.mapToCompanyDTO(company);
    }




    @Transactional
    public CompanyDTO updateCompany(CompanyDTO companyDTO) {
        Company companyToUpdate = companyRepository.findById(companyDTO.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Company with id %d not found".formatted(companyDTO.getId())));
        updateCompanyEntityFromDTO(companyToUpdate, companyDTO);
        Company savedCompany = companyRepository.save(companyToUpdate);
        return companyMapper.mapToCompanyDTO(savedCompany);
    }

    private void updateCompanyEntityFromDTO(Company companyToUpdate, CompanyDTO companyDTO) {
        if (companyDTO.getName() != null &&  !companyDTO.getName().isBlank()) {}
            companyToUpdate.setName(companyDTO.getName());
        if (companyDTO.getDescription() != null && !companyDTO.getDescription().isBlank())
            companyToUpdate.setDescription(companyDTO.getDescription());
    }




    public CompanyDTO getCompany(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Company with id %d not found".formatted(id)));
        return companyMapper.mapToCompanyDTO(company);
    }




    @Transactional
    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }




    public List<CompanyDTO> getAllCompanies(User user) {
        List<Company> companies =  membershipRepository.findAllByEmployee(user)
                .stream()
                .map(membership -> membership.getTeam().getCompany())
                .collect(Collectors.toList());
        companies.addAll(companyRepository.findAllByOwner(user));
        return companies
                .stream()
                .sorted(Comparator.comparing(Company::getName))
                .map(companyMapper::mapToCompanyDTO)
                .collect(Collectors.toList());
    }




    public List<CompanyDTO> searchCompanies(User user, String title) {
        List<Company> companies = companyRepository.findCompaniesByUserAndNameContaining(user, title);
        companies.addAll(companyRepository.findAllByOwnerAndNameContaining(user, title));
        return companies
                .stream()
                .sorted(Comparator.comparing(Company::getName))
                .map(companyMapper::mapToCompanyDTO)
                .collect(Collectors.toList());
    }
}