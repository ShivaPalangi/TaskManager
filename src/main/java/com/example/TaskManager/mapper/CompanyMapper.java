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
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(company.getId());
        companyDTO.setName(company.getName());
        companyDTO.setDescription(company.getDescription());
        if ( ! company.getTeams().isEmpty())
            companyDTO.setTeams(company.getTeams().stream().map(TeamMapper::mapToTeamDTO).collect(Collectors.toList()));
        if ( ! company.getEmployees().isEmpty())
            companyDTO.setEmployees(company.getEmployees().stream().map(EmployeeMapper::mapToEmployeeDTO).collect(Collectors.toList()));
        return companyDTO;
    }


    public static Company mapToCompanyEntity(CompanyDTO companyDTO){
        Company company = new Company();
        company.setId(companyDTO.getId());
        company.setName(companyDTO.getName());
        company.setDescription(companyDTO.getDescription());
        if ( ! companyDTO.getTeams().isEmpty())
            company.setTeams(companyDTO.getTeams().stream().map(TeamMapper::mapToTeamEntity).collect(Collectors.toList()));
        if ( ! companyDTO.getEmployees().isEmpty())
            company.setEmployees(companyDTO.getEmployees().stream().map(EmployeeMapper::mapToEmployeeEntity).collect(Collectors.toList()));
        return company;
    }
}