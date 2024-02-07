package com.employee_planning.service.chart;

import com.employee_planning.model.Employee;
import com.employee_planning.model.EmployeeProject;
import com.employee_planning.model.Project;
import com.employee_planning.model.chart.ChartEmployee;
import com.employee_planning.model.chart.ChartEmployeeProject;
import com.employee_planning.model.chart.ChartYear;
import com.employee_planning.repository.EmployeeProjectRepository;
import com.employee_planning.repository.EmployeeRepository;
import com.employee_planning.repository.ProjectRepository;
import com.employee_planning.service.employee.EmployeeService;
import com.employee_planning.service.employeeProject.EmployeeProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

@Service
public class ChartServiceImpl implements ChartService {

    @Autowired
    private EmployeeProjectRepository employeeProjectRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeProjectService employeeProjectService;

    @Override
    public List<ChartEmployee> getAllEmployeesByEmployeeProjectStartDate(ChartYear year) {
        LocalDate yearObjStart = LocalDate.parse(year.getYear() + "-01-01");
        LocalDate yearObjEnd = LocalDate.parse(year.getYear() + "-12-31");
        List<Employee> employees = removeAllEmployeeProjectsOutsideTheGivenYear(year);
        employees.sort(Comparator.comparing(Employee::getLastName));
        for (Employee employee : employees) {
            assert employee.getEmployeeProjects() != null;
            for (EmployeeProject employeeProject : employee.getEmployeeProjects()) {
                LocalDate startDate = LocalDate.parse(employeeProject.getEmployeeProjectStartDate());
                LocalDate endDate = LocalDate.parse(employeeProject.getEmployeeProjectEndDate());
                if ((startDate.isAfter(yearObjStart) || (startDate.isEqual(yearObjStart))) &&
                        (endDate.isBefore(yearObjEnd) || endDate.isEqual(yearObjEnd))) {
                    employeeProject.setEmployeeBookedMonths(employeeService.getEmployeeBookedMonths(employeeProject.getEmployeeProjectStartDate(), employeeProject.getEmployeeProjectEndDate()));
                }
                if (startDate.isBefore(yearObjStart) && endDate.isAfter(yearObjEnd)) {
                    employeeProject.setEmployeeProjectStartDate(String.valueOf(yearObjStart));
                    employeeProject.setEmployeeProjectEndDate(String.valueOf(yearObjEnd));
                    employeeProject.setEmployeeBookedMonths(employeeService.getEmployeeBookedMonths(String.valueOf(yearObjStart), String.valueOf(yearObjEnd)));
                }
                if ((startDate.isAfter(yearObjStart) || startDate.isEqual(yearObjStart)) &&
                        (endDate.isAfter(yearObjEnd) || endDate.isEqual(yearObjEnd))) {
                    employeeProject.setEmployeeProjectEndDate(String.valueOf(yearObjEnd));
                    employeeProject.setEmployeeBookedMonths(employeeService.getEmployeeBookedMonths(employeeProject.getEmployeeProjectStartDate(), String.valueOf(yearObjEnd)));
                }
                if ((startDate.isBefore(yearObjStart) || startDate.isEqual(yearObjStart)) &&
                        (endDate.isBefore(yearObjEnd) || endDate.isEqual(yearObjEnd))) {
                    employeeProject.setEmployeeProjectStartDate(String.valueOf(yearObjStart));
                    employeeProject.setEmployeeBookedMonths(employeeService.getEmployeeBookedMonths(String.valueOf(yearObjStart), employeeProject.getEmployeeProjectEndDate()));
                }
            }
            List<EmployeeProject> employeeProjects = employee.getEmployeeProjects();
            setEmployeeProjectMonthsByPercentage(employeeProjects);
            employeeProjects.sort(Comparator.comparing(EmployeeProject::getEmployeeProjectStartDate));
        }
        List<ChartEmployee> chartEmployees = findAllChartEmployeesFromFindAllEmployees(employees);
        employees.sort(Comparator.comparing(Employee::getLastName));

        return chartEmployees;
    }

    @Override
    public List<ChartEmployee> findAllChartEmployeesFromFindAllEmployees(List<Employee> employees) {
        List<ChartEmployee> chartEmployees = new ArrayList<>();
        for (Employee employee : employees) {
            ChartEmployee chartEmployee = new ChartEmployee();
            chartEmployee.setId(employee.getId());
            chartEmployee.setName(employee.getName());
            List<ChartEmployeeProject> chartEmployeeProjects = new ArrayList<>();
            long index = 1L;
            assert employee.getEmployeeProjects() != null;
            for (EmployeeProject employeeProject : employee.getEmployeeProjects()) {
                ChartEmployeeProject chartEmployeeProject = new ChartEmployeeProject();
                chartEmployeeProject.setId(index);
                chartEmployeeProject.setProjectId(employeeProject.getProject().getId());
                chartEmployeeProject.setProjectName(employeeProject.getEmployeeProjectName());
                chartEmployeeProject.setEmployeeBookedMonths(employeeProject.getEmployeeBookedMonths());
                chartEmployeeProject.setConfirmBooking(employeeProject.getConfirmBooking());
                chartEmployeeProject.setStartDate(employeeProject.getEmployeeProjectStartDate());
                chartEmployeeProject.setPercentage(employeeProject.getPercentage());
                chartEmployeeProjects.add(chartEmployeeProject);
                index += 1;
            }
            chartEmployee.setChartEmployeeProjects(chartEmployeeProjects);
            chartEmployees.add(chartEmployee);
        }
        return chartEmployees;
    }

    @Override
    public List<Employee> removeAllEmployeeProjectsOutsideTheGivenYear(ChartYear year) {
        LocalDate yearObjStart = LocalDate.parse(year.getYear() + "-01-01");
        LocalDate yearObjEnd = LocalDate.parse(year.getYear() + "-12-31");
        List<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees) {
            assert employee.getEmployeeProjects() != null;
            Iterator<EmployeeProject> iter = employee.getEmployeeProjects().iterator();
            while (iter.hasNext()) {
                EmployeeProject employeeProject = iter.next();
                LocalDate startDate = LocalDate.parse(employeeProject.getEmployeeProjectStartDate());
                LocalDate endDate = LocalDate.parse(employeeProject.getEmployeeProjectEndDate());
                if ((startDate.isBefore(yearObjStart) && endDate.isBefore(yearObjStart)) ||
                        (startDate.isAfter(yearObjEnd) && endDate.isAfter(yearObjEnd))) {
                    iter.remove();
                }

            }
        }
        employees.removeIf(empl -> {
            assert empl.getEmployeeProjects() != null;
            return empl.getEmployeeProjects().isEmpty();
        });
        return employees;
    }

    @Override
    public List<Project> removeAllProjectsOutsideTheGivenYear(ChartYear year) {
        LocalDate yearObjStart = LocalDate.parse(year.getYear() + "-01-01");
        LocalDate yearObjEnd = LocalDate.parse(year.getYear() + "-12-31");
        List<Project> projects = projectRepository.findAll();

        projects.removeIf(project -> {
            LocalDate startDate = LocalDate.parse(project.getStartDate());
            LocalDate endDate = LocalDate.parse(project.getEndDate());

            if ((startDate.isBefore(yearObjStart) && endDate.isBefore(yearObjStart))
                    || (startDate.isAfter(yearObjEnd) && endDate.isAfter(yearObjEnd))) {
                return true;
            }

            if (startDate.isBefore(yearObjStart)) {
                startDate = yearObjStart;
            }

            if (endDate.isAfter(yearObjEnd)) {
                endDate = yearObjEnd;
            }

            long months = ChronoUnit.MONTHS.between(startDate, endDate.plusDays(1));
            project.setProjectLengthInMonths(months);

            return false;
        });
        return projects;
    }


    @Override
    public List<EmployeeProject> setEmployeeProjectMonthsByPercentage(List<EmployeeProject> employeeProjects) {
        for (EmployeeProject employeeProject : employeeProjects) {
            if (employeeProject.getPercentage() != 100 && employeeProject.getPercentage() != 0) {
                double bookedMonthsPercentage = employeeProjectService.getEmployeeBookedMonthsByPercentage(employeeProject.getPercentage(), employeeProject.getEmployeeBookedMonths());
                employeeProject.setEmployeeBookedMonths(bookedMonthsPercentage);
            }
        }
        return employeeProjects;
    }
}
