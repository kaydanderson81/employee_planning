package com.employee_planning.model.chart;

import com.employee_planning.EmployeeProjectTestBase;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ChartEmployeeProjectTest extends EmployeeProjectTestBase {

    @Test
    void testNoArgConstructor() {
        ChartEmployeeProject chartEmployeeProject = new ChartEmployeeProject();
        assertNotNull(chartEmployeeProject);
    }

    @Test
    void getId() {
        long id = 3L;
        long retrievedId = this.chartEmployeeProject1.getId();
        assertEquals(id, retrievedId);
    }

    @Test
    void setId() {
        long setId = 1L;
        this.chartEmployeeProject1.setId(setId);
        assertEquals(setId, this.chartEmployeeProject1.getId());
    }

    @Test
    void getProjectId() {
        long projectId = 3L;
        long retrievedProjectId = this.chartEmployeeProject1.getProjectId();
        assertEquals(projectId, retrievedProjectId);
    }

    @Test
    void setProjectId() {
        long setProjectId = 1L;
        this.chartEmployeeProject1.setProjectId(setProjectId);
        assertEquals(setProjectId, this.chartEmployeeProject1.getProjectId());
    }

    @Test
    void getProjectName() {
        String name = "Test Project 1";
        String retrievedName = this.chartEmployeeProject1.getProjectName();
        assertNotNull(retrievedName);
        assertEquals(name, retrievedName);
    }

    @Test
    void setProjectName() {
        String setName = "Set Test";
        this.chartEmployeeProject1.setProjectName(setName);
        assertEquals(setName, this.chartEmployeeProject1.getProjectName());
    }

    @Test
    void getEmployeeBookedMonths() {
        double months = 12;
        double retrievedMonths = this.chartEmployeeProject1.getEmployeeBookedMonths();
        assertEquals(months, retrievedMonths);
    }

    @Test
    void setEmployeeBookedMonths() {
        double setMonths = 6;
        this.chartEmployeeProject1.setEmployeeBookedMonths(setMonths);
        assertEquals(setMonths, this.chartEmployeeProject1.getEmployeeBookedMonths());
    }

    @Test
    void isConfirmBooking() {
        boolean confirm = true;
        boolean retrievedConfirm = this.chartEmployeeProject1.isConfirmBooking();
        assertEquals(confirm, retrievedConfirm);
    }

    @Test
    void setConfirmBooking() {
        boolean setConfirm = false;
        this.chartEmployeeProject1.setConfirmBooking(setConfirm);
        assertEquals(setConfirm, this.chartEmployeeProject1.isConfirmBooking());
    }

    @Test
    void getStartDate() {
        String startDate = "2023-01-01";
        String retrievedDate = this.chartEmployeeProject1.getStartDate();
        assertNotNull(retrievedDate);
        assertEquals(startDate, retrievedDate);
    }

    @Test
    void setStartDate() {
        String setStartDate = "2022-02-01";
        this.chartEmployeeProject1.setStartDate(setStartDate);
        assertEquals(setStartDate, this.chartEmployeeProject1.getStartDate());
    }

    @Test
    void getPercentage() {
        int percentage = 50;
        int retrievedPercentage = this.chartEmployeeProject1.getPercentage();
        assertEquals(percentage, retrievedPercentage);
    }

    @Test
    void setPercentage() {
        int setPercentage = 75;
        this.chartEmployeeProject1.setPercentage(setPercentage);
        assertEquals(setPercentage, this.chartEmployeeProject1.getPercentage());
    }

    @Test
    void testToString() {
        String expectedToString = "ChartEmployeeProject{id=3, projectId=3, projectName='Test Project 1', " +
                "employeeBookedMonths=12.0, confirmBooking=true, startDate='2023-01-01', percentage=50}";
        assertEquals(expectedToString, this.chartEmployeeProject1.toString());
    }
}