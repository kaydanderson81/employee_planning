package com.employee_planning.service.employeeProject;

import com.employee_planning.model.Employee;
import com.employee_planning.model.EmployeeProject;
import com.employee_planning.repository.EmployeeProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeProjectServiceImpl implements EmployeeProjectService {

    @Autowired
    private EmployeeProjectRepository employeeProjectRepository;


    @Override
    public List<EmployeeProject> getAllEmployeeProjects() {
        return employeeProjectRepository.findAll();
    }

    @Override
    public EmployeeProject getEmployeeProjectById(Long id) {
        Optional<EmployeeProject> optional = employeeProjectRepository.findById(id);
        EmployeeProject employeeProject;
        if (optional.isPresent()) {
            employeeProject = optional.get();
        } else {
            throw new RuntimeException("Employee Project not found for id :: " + id);
        }
        return employeeProject;
    }

    @Override
    public void saveEmployeeProject(EmployeeProject employeeProject) {
        this.employeeProjectRepository.save(employeeProject);
    }

    @Override
    public EmployeeProject setEmployeeProjectAttributes(Employee updateEmployee, EmployeeProject employeeProject,
                                                        double employeeBookedMonths) {
        EmployeeProject newEmployeeProject = new EmployeeProject();
        newEmployeeProject.setEmployee(updateEmployee);
        newEmployeeProject.setProject(employeeProject.getProject());
        newEmployeeProject.setEmployeeProjectName(employeeProject.getProject().getName());
        newEmployeeProject.setEmployeeProjectStartDate(employeeProject.getEmployeeProjectStartDate());
        newEmployeeProject.setEmployeeProjectEndDate(employeeProject.getEmployeeProjectEndDate());
        newEmployeeProject.setConfirmBooking(employeeProject.getConfirmBooking());
        newEmployeeProject.setPercentage(employeeProject.getPercentage());
        newEmployeeProject.setEmployeeBookedMonths(employeeBookedMonths);
        double employeeBookedMonthsPercentage = getEmployeeBookedMonthsByPercentage(employeeProject.getPercentage(),
                newEmployeeProject.getEmployeeBookedMonths());
        if (employeeProject.getPercentage() != 100) {
            newEmployeeProject.setEmployeeBookedMonths(employeeBookedMonthsPercentage);

        }
        return newEmployeeProject;
    }

    @Override
    public EmployeeProject setUpdatedEmployeeProjectAttributes(Long id, EmployeeProject employeeProjects, double employeeBookedMonths) {
        EmployeeProject updateEmployeeProject = getEmployeeProjectById(id);
        updateEmployeeProject.setEmployeeProjectStartDate(employeeProjects.getEmployeeProjectStartDate());
        updateEmployeeProject.setEmployeeProjectEndDate(employeeProjects.getEmployeeProjectEndDate());
        updateEmployeeProject.setConfirmBooking(employeeProjects.getConfirmBooking());
        updateEmployeeProject.setEmployeeBookedMonths(employeeBookedMonths);
        updateEmployeeProject.setPercentage(employeeProjects.getPercentage());
        if (updateEmployeeProject.getPercentage() != 100) {
            updateEmployeeProject.setEmployeeBookedMonths(
                    getEmployeeBookedMonthsByPercentage(updateEmployeeProject.getPercentage(),
                            updateEmployeeProject.getEmployeeBookedMonths()));

        }
        return updateEmployeeProject;
    }


    @Override
    public List<EmployeeProject> getAllEmployeeProjectsByProjectId(Long id) {
        return employeeProjectRepository.getAllEmployeeProjectsByProjectId(id);
    }

    @Override
    public void deleteEmployeeProjectById(Long id) {
        this.employeeProjectRepository.deleteById(id);
    }

    //Charts

    @Override
    public List<String> findAllStartAndEndDatesByYear() {
        Set<String> startDates = employeeProjectRepository.findAllEmployeeProjectStartDates();
        Set<String> endDates = employeeProjectRepository.findAllEmployeeProjectEndDates();

        Set<Integer> years = new HashSet<>();
        for (String date : startDates) {
            for (String end : endDates) {
                String startYear = date.split("-")[0];
                String endYear = end.split("-")[0];
                years.add(Integer.valueOf(String.valueOf(startYear)));
                years.add(Integer.valueOf(String.valueOf(endYear)));

            }
        }
        List<Integer> sortedYears = new ArrayList<>(years);
        Collections.sort(sortedYears);
        return sortedYears.stream().map(Object::toString).collect(Collectors.toList());
    }

    @Override
    public Double getEmployeeBookedMonthsByPercentage(int percentage, double bookedMonths) {
        return (bookedMonths * percentage) / 100;
    }

}
