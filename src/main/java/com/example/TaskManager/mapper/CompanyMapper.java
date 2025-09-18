package com.example.TaskManager.mapper;

import com.example.TaskManager.dto.CompanyDTO;
import com.example.TaskManager.entity.Company;
import com.example.TaskManager.exception.ResourceNotFoundException;
import com.example.TaskManager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class CompanyMapper {
    private final UserRepository userRepository;
    private final TeamMapper teamMapper;

    public CompanyDTO mapToCompanyDTO(Company company){
        if (company == null) return null;

        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(company.getId());
        companyDTO.setName(company.getName());
        companyDTO.setDescription(company.getDescription());
        if ( company.getTeams()!= null && !company.getTeams().isEmpty() )
            companyDTO.setTeams(company.getTeams().stream().map(teamMapper::mapToTeamDTO).collect(Collectors.toList()));
        if ( company.getOwner() != null )
            companyDTO.setOwnerId(company.getOwner().getId());
        return companyDTO;
    }


    public  Company mapToCompanyEntity(CompanyDTO companyDTO){
        if (companyDTO == null) return null;

        Company company = new Company();
        company.setId(companyDTO.getId());
        company.setName(companyDTO.getName());
        company.setDescription(companyDTO.getDescription());
        if ( company.getTeams() != null && !company.getTeams().isEmpty() )
            company.setTeams(companyDTO.getTeams().stream().map(teamMapper::mapToTeamEntity).collect(Collectors.toList()));
        if ( companyDTO.getOwnerId() != null )
            company.setOwner(userRepository.findById(companyDTO.getOwnerId()).orElseThrow(
                    () -> new ResourceNotFoundException("Owner not found with id " + companyDTO.getOwnerId())));
        return company;
    }
}