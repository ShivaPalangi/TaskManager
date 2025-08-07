package com.example.TaskManager.mapper;

import com.example.TaskManager.dto.EmployeeDTO;
import com.example.TaskManager.entity.Employee;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class EmployeeMapper {

    public static EmployeeDTO mapToEmployeeDTO(Employee employee){
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setFirstName(employee.getFirstName());
        employeeDTO.setLastName(employee.getLastName());
        employeeDTO.setPhoneNumber(employee.getPhoneNumber());
        employeeDTO.setEmailAddress(employee.getEmailAddress());
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        if (employee.getDateOfBirth() != null)
            employeeDTO.setDateOfBirth(employee.getDateOfBirth().format(formatter));
        if (employee.getCompany() != null)
            employeeDTO.setCompany(CompanyMapper.mapToCompanyDTO(employee.getCompany()));
        if ( employee.getMemberships() != null && !employee.getMemberships().isEmpty())
            employeeDTO.setMemberships(employee.getMemberships().stream().map(MembershipMapper::mapToMembershipDTO).collect(Collectors.toList()));
        return employeeDTO;
    }


    public static Employee mapToEmployeeEntity(EmployeeDTO employeeDTO){
        Employee employee = new Employee();
        employee.setId(employeeDTO.getId());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setPhoneNumber(employeeDTO.getPhoneNumber());
        employee.setEmailAddress(employeeDTO.getEmailAddress());
        employee.setDateOfBirth(LocalDate.parse(employeeDTO.getDateOfBirth()));
        if (employeeDTO.getCompany() != null)
            employee.setCompany(CompanyMapper.mapToCompanyEntity(employeeDTO.getCompany()));
        if ( employeeDTO.getMemberships() != null && !employeeDTO.getMemberships().isEmpty())
            employee.setMemberships(employeeDTO.getMemberships().stream().map(MembershipMapper::mapToMembershipEntity).collect(Collectors.toList()));
        return employee;
    }
}