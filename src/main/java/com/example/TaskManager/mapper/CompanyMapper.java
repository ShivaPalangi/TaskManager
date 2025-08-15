package com.example.TaskManager.mapper;

import com.example.TaskManager.dto.CompanyDTO;
import com.example.TaskManager.entity.Company;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class CompanyMapper {

    public static CompanyDTO mapToCompanyDTO(Company company){
        if (company == null) return null;

        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(company.getId());
        companyDTO.setName(company.getName());
        companyDTO.setDescription(company.getDescription());
        if ( company.getTeams()!= null && !company.getTeams().isEmpty())
            companyDTO.setTeams(company.getTeams().stream().map(TeamMapper::mapToTeamDTO).collect(Collectors.toList()));
        if ( company.getEmployees() != null && !company.getEmployees().isEmpty())
            companyDTO.setEmployees(company.getEmployees().stream().map(EmployeeMapper::mapToEmployeeDTO).collect(Collectors.toList()));
        return companyDTO;
    }


    public static Company mapToCompanyEntity(CompanyDTO companyDTO){
        if (companyDTO == null) return null;

        Company company = new Company();
        company.setId(companyDTO.getId());
        company.setName(companyDTO.getName());
        company.setDescription(companyDTO.getDescription());
        if ( company.getTeams() != null && !company.getTeams().isEmpty())
            company.setTeams(companyDTO.getTeams().stream().map(TeamMapper::mapToTeamEntity).collect(Collectors.toList()));
        if ( companyDTO.getEmployees() != null && !companyDTO.getEmployees().isEmpty())
            company.setEmployees(companyDTO.getEmployees().stream().map(EmployeeMapper::mapToEmployeeEntity).collect(Collectors.toList()));
        return company;
    }
}