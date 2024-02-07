package com.employee_planning.service.employeeProject;

import com.employee_planning.model.Employee;
import com.employee_planning.model.EmployeeProject;

import java.util.List;

public interface EmployeeProjectService {



    List<EmployeeProject> getAllEmployeeProjects();

    EmployeeProject getEmployeeProjectById(Long id);

    void saveEmployeeProject(EmployeeProject employeeProject);

    EmployeeProject setEmployeeProjectAttributes(Employee employee, EmployeeProject employeeProject,
                                                 double employeeBookedMonths);

    EmployeeProject setUpdatedEmployeeProjectAttributes(Long id, EmployeeProject employeeProject, double employeeBookedMonths);

    List<EmployeeProject> getAllEmployeeProjectsByProjectId(Long id);

    void deleteEmployeeProjectById(Long id);

    List<String> findAllStartAndEndDatesByYear();

    Double getEmployeeBookedMonthsByPercentage(int percentage, double bookedMonths);

}
