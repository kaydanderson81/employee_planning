package com.employee_planning.project;

import com.employee_planning.EmployeeProjectTestBase;
import com.employee_planning.model.EmployeeProject;
import com.employee_planning.repository.EmployeeProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProjectWarningTest extends EmployeeProjectTestBase {

    @Autowired
    private EmployeeProjectRepository employeeProjectRepository;

    @Test
    void validateEmployeeProject_Success() {
        String expectedWarning = "";
        String retrievedWarning = ProjectWarning.validateEmployeeProject(this.employeeProject3);
        assertNotNull(retrievedWarning);
        assertEquals(expectedWarning, retrievedWarning);
    }

    @Test
    void validateEmployeeProject_IncorrectStartDate() {
        String expectedWarning = "Employee project start date day must be the 1st or 15th. Please check dates entered.";
        this.employeeProject3.setEmployeeProjectStartDate("2023-01-02");
        String retrievedWarning = ProjectWarning.validateEmployeeProject(this.employeeProject3);
        assertNotNull(retrievedWarning);
        assertEquals(expectedWarning, retrievedWarning);
    }

    @Test
    void validateEmployeeProject_IncorrectEndDate() {
        String expectedWarning = "Employee project end date day must be the 14th or the last day of the month. Please check dates entered.";
        this.employeeProject3.setEmployeeProjectEndDate("2023-01-12");
        String retrievedWarning = ProjectWarning.validateEmployeeProject(this.employeeProject3);
        assertNotNull(retrievedWarning);
        assertEquals(expectedWarning, retrievedWarning);
    }

    @Test
    void validateListOfEmployeeProjects_Success() {
        String expectedWarning = "";
        List<EmployeeProject> emptyList = new ArrayList<>();
        String retrievedWarning = ProjectWarning.validateListOfEmployeeProjects(emptyList);
        assertNotNull(retrievedWarning);
        assertEquals(expectedWarning, retrievedWarning);
    }

    @Test
    void validateListOfEmployeeProjects_Failed_EmployeeProjectsStillExistForThatProject() {
        String expectedError = "There are still 5 Employees booked to project Test Project 1: Test Employee 1, " +
                "Test Employee 2, Test Employee 2, Test Employee 1, Test Employee 1";
        String actualError = ProjectWarning.validateListOfEmployeeProjects(employeeProjects);
        assertNotNull(actualError);
        assertEquals(expectedError, actualError);
    }

    @Test
    void ifProjectStartDateIsNotSetCorrectly_Success() {
        boolean setIncorrectly = false;
        boolean retrievedWarning = ProjectWarning.ifProjectStartDateIsNotSetCorrectly("");
        assertEquals(setIncorrectly, retrievedWarning);
    }

    @Test
    void ifProjectStartDateIsNotSetCorrectly_Fail() {
        boolean setIncorrectly = true;
        boolean retrievedWarning = ProjectWarning.ifProjectStartDateIsNotSetCorrectly("2022-01-02");
        assertEquals(setIncorrectly, retrievedWarning);
    }

    @Test
    void ifProjectEndDateIsNotSetCorrectly_Success() {
        boolean setIncorrectly = false;
        boolean retrievedWarning = ProjectWarning.ifProjectEndDateIsNotSetCorrectly("");
        assertEquals(setIncorrectly, retrievedWarning);
    }

    @Test
    void ifProjectEndDateIsNotSetCorrectly_Fail() {
        boolean setIncorrectly = true;
        boolean retrievedWarning = ProjectWarning.ifProjectEndDateIsNotSetCorrectly("2022-12-30");
        assertEquals(setIncorrectly, retrievedWarning);
    }

    @Test
    void loopEmployeeProjectsAndPrint() {
        String expectedPrint = "Test Employee 1, Test Employee 2, Test Employee 2, Test Employee 1, Test Employee 1";
        String retrievedPrint = ProjectWarning.loopEmployeeProjectsAndPrint(this.employeeProjects);
        assertNotNull(retrievedPrint);
        assertEquals(expectedPrint, retrievedPrint);
    }
}