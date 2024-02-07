package com.employee_planning.service.chart;

import com.employee_planning.model.Employee;
import com.employee_planning.model.EmployeeProject;
import com.employee_planning.model.Project;
import com.employee_planning.model.chart.ChartEmployee;
import com.employee_planning.model.chart.ChartYear;

import java.util.List;

public interface ChartService {

    List<ChartEmployee> findAllChartEmployeesFromFindAllEmployees(List<Employee> employees);

    List<ChartEmployee> getAllEmployeesByEmployeeProjectStartDate(ChartYear year);

    List<Employee> removeAllEmployeeProjectsOutsideTheGivenYear(ChartYear year);
    List<Project> removeAllProjectsOutsideTheGivenYear(ChartYear year);

    List<EmployeeProject> setEmployeeProjectMonthsByPercentage(List<EmployeeProject> employeeProjects);
}
