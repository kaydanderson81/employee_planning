package com.employee_planning.project;

import com.employee_planning.model.EmployeeProject;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectWarning {

    public static String validateEmployeeProject(EmployeeProject employeeProjects) {
        String error = "";
        if (ProjectWarning.ifProjectStartDateIsNotSetCorrectly(employeeProjects.getEmployeeProjectStartDate())) {
            error += "Employee project start date day must be the 1st or 15th. Please check dates entered.";
        }

        if (ProjectWarning.ifProjectEndDateIsNotSetCorrectly(employeeProjects.getEmployeeProjectEndDate())) {
            error += "Employee project end date day must be the 14th or the last day of the month. Please check dates entered.";
        }

        return error;
    }

    public static String validateListOfEmployeeProjects(List<EmployeeProject> employeeProjects) {
        String error = "";
        String message = loopEmployeeProjectsAndPrint(employeeProjects);
        if (!employeeProjects.isEmpty()) {
            error += "There are still " + employeeProjects.size() + " Employees booked to project " +
                    employeeProjects.get(0).getProject().getName() + ": " + message;
        }
        return error;
    }

    public static boolean ifProjectStartDateIsNotSetCorrectly(String employeeProjectStartDates) {
        if (employeeProjectStartDates.isEmpty()) {
            return false;
        }
        String subStart = employeeProjectStartDates.substring(employeeProjectStartDates.length() - 2);
        return !subStart.equals("01") && !subStart.equals("15");
    }

    public static boolean ifProjectEndDateIsNotSetCorrectly(String employeeProjectEndDates) {
        if (employeeProjectEndDates.isEmpty()) {
            return false;
        }
            LocalDate end = LocalDate.parse(employeeProjectEndDates);
            YearMonth month = YearMonth.from(end);
        return !end.equals(month.atDay(14)) && !end.equals(month.atEndOfMonth());
    }

    public static String loopEmployeeProjectsAndPrint(List<EmployeeProject> employeeProjects) {
        return employeeProjects.stream().map(
                e -> e.getEmployee().getName()).collect(Collectors.joining(", "));
    }

}
