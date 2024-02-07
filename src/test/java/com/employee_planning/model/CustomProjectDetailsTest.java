package com.employee_planning.model;

import com.employee_planning.EmployeeProjectTestBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomProjectDetailsTest extends EmployeeProjectTestBase {

    @Test
    void getId() {
        Long id = 1L;
        Long retrievedId = this.customProjectDetails1.getId();
        assertNotNull(retrievedId);
        assertEquals(id, retrievedId);
    }

    @Test
    void setId() {
        Long testSetId = 11L;
        this.customProjectDetails1.setId(testSetId);
        assertEquals(testSetId, this.customProjectDetails1.getId());
    }

    @Test
    void getProject() {
        Project project = this.project1;
        Project retrievedProject = this.customProjectDetails1.getProject();
        assertNotNull(retrievedProject);
        assertEquals(project, retrievedProject);
    }

    @Test
    void setProject() {
        Project projectToSet = this.project5;
        this.customProjectDetails1.setProject(projectToSet);
        assertEquals(projectToSet, this.customProjectDetails1.getProject());
    }

    @Test
    void getCustomProjectYear() {
        String year = "2023";
        String retrievedYear = this.customProjectDetails1.getCustomProjectYear();
        assertNotNull(retrievedYear);
        assertEquals(year, retrievedYear);
    }

    @Test
    void setCustomProjectYear() {
        String yearToSet = "2000";
        this.customProjectDetails1.setCustomProjectYear(yearToSet);
        assertEquals(yearToSet, this.customProjectDetails1.getCustomProjectYear());
    }

    @Test
    void getCustomProjectPersonMonths() {
        double personMonths = 10.0;
        double retrievedPersonMonths = this.customProjectDetails1.getCustomProjectPersonMonths();
        assertEquals(personMonths, retrievedPersonMonths);
    }

    @Test
    void setCustomProjectPersonMonths() {
        double monthsToSet = 8.5;
        this.customProjectDetails1.setCustomProjectPersonMonths(monthsToSet);
        assertEquals(monthsToSet, this.customProjectDetails1.getCustomProjectPersonMonths());
    }

    @Test
    void testToString() {
        CustomProjectDetails customProjectDetails1 = new CustomProjectDetails(1L, project1, "2023", 10.0);
        String expected = "CustomProjectDetails{" +
                "id=1, " +
                "project=" + this.project1 + ", " +
                "customProjectYear='2023', " +
                "customProjectPersonMonths=10.0" +
                '}';

        assertEquals(expected, customProjectDetails1.toString());
    }
}