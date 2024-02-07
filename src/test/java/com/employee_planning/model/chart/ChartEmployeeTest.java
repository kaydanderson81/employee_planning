package com.employee_planning.model.chart;

import com.employee_planning.EmployeeProjectTestBase;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChartEmployeeTest extends EmployeeProjectTestBase {

    @Test
    void testNoArgConstructor() {
        ChartEmployee chartEmployee = new ChartEmployee();
        assertNotNull(chartEmployee);
    }

    @Test
    void getId() {
        long id = 1L;
        long retrievedId = chartEmployee1.getId();
        assertEquals(id, retrievedId);
    }

    @Test
    void setId() {
        long setId = 11L;
        this.chartEmployee1.setId(setId);
        assertEquals(setId, this.chartEmployee1.getId());
    }

    @Test
    void getName() {
        String name = "Chart Employee 1";
        String retrievedName = this.chartEmployee1.getName();
        assertNotNull(retrievedName);
        assertEquals(name, retrievedName);
    }

    @Test
    void setName() {
        String setName = "Test Chart Employee";
        this.chartEmployee1.setName(setName);
        assertNotNull(this.chartEmployee1.getName());
        assertEquals(setName, this.chartEmployee1.getName());
    }

    @Test
    void getChartEmployeeProjects() {
        List<ChartEmployeeProject> chartEmployeeProjectList = this.chartEmployeeProjects;
        List<ChartEmployeeProject> retrievedChartEmployeeProjects = this.chartEmployee1.getChartEmployeeProjects();
        assertNotNull(retrievedChartEmployeeProjects);
        assertEquals(chartEmployeeProjectList, retrievedChartEmployeeProjects);
    }

    @Test
    void setChartEmployeeProjects() {
        List<ChartEmployeeProject> employeeProjectList = new ArrayList<>();
        employeeProjectList.add(chartEmployeeProject2);
        employeeProjectList.add(chartEmployeeProjectToAdd);
        this.chartEmployee1.setChartEmployeeProjects(employeeProjectList);
        assertNotNull(this.chartEmployee1.getChartEmployeeProjects());
        assertEquals(employeeProjectList, this.chartEmployee1.getChartEmployeeProjects());
    }

    @Test
    void testToString() {


        String expectedToString = "ChartEmployee{" +
                "id=1, name='Chart Employee 1', " +
                "chartEmployeeProjects=[ChartEmployeeProject{id=3, projectId=3, projectName='Test Project 1', " +
                "employeeBookedMonths=12.0, confirmBooking=true, startDate='2023-01-01', percentage=50}, " +
                "ChartEmployeeProject{id=5, projectId=5, projectName='Test Project 3', employeeBookedMonths=12.0, " +
                "confirmBooking=true, startDate='2023-05-01', percentage=50}]}";

        assertEquals(expectedToString, this.chartEmployee1.toString());
    }
}