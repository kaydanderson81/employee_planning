package com.employee_planning.model;

import com.employee_planning.EmployeeProjectTestBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProjectTest extends EmployeeProjectTestBase {

    @Test
    void getId() {
        Long id = 1L;
        Long retrievedId = this.project1.getId();
        assertNotNull(retrievedId);
        assertEquals(id, retrievedId);
    }

    @Test
    void setId() {
        Long testProjectId = 11L;
        this.project1.setId(testProjectId);
        assertNotNull(this.project1);
        assertEquals(testProjectId, this.project1.getId());
    }

    @Test
    void getProjectNumber() {
        Long projectNumber = 111L;
        Long retrievedProjectNumber = this.project1.getProjectNumber();
        assertNotNull(retrievedProjectNumber);
        assertEquals(projectNumber, retrievedProjectNumber);
    }

    @Test
    void setProjectNumber() {
        Long setProjectNumber = 12345L;
        this.project1.setProjectNumber(setProjectNumber);
        assertNotNull(this.project1.getProjectNumber());
        assertEquals(setProjectNumber, this.project1.getProjectNumber());
    }

    @Test
    void getName() {
        String name = "Test Project 1";
        String retrievedName = this.project1.getName();
        assertNotNull(retrievedName);
        assertEquals(name, retrievedName);
    }

    @Test
    void setName() {
        String name = "Set Project";
        this.project1.setName(name);
        assertNotNull(this.project1.getName());
        assertEquals(name, this.project1.getName());
    }

    @Test
    void getStartDate() {
        String startDate = "2021-01-01";
        String retrievedDate = this.project1.getStartDate();
        assertNotNull(retrievedDate);
        assertEquals(startDate, retrievedDate);

    }

    @Test
    void setStartDate() {
        String startDate = "2022-02-02";
        this.project1.setStartDate(startDate);
        assertNotNull(this.project1.getStartDate());
        assertEquals(startDate, this.project1.getStartDate());
    }

    @Test
    void getEndDate() {
        String endDate = "2021-12-31";
        String retrievedDate = this.project1.getEndDate();
        assertNotNull(retrievedDate);
        assertEquals(endDate, retrievedDate);
    }

    @Test
    void setEndDate() {
        String endDate = "2022-12-31";
        this.project1.setEndDate(endDate);
        assertNotNull(this.project1.getEndDate());
        assertEquals(endDate, this.project1.getEndDate());
    }

    @Test
    void getProjectLengthInMonths() {
        double lengthInMonths = 12;
        double retrievedMonths = this.project1.getProjectLengthInMonths();
        assertEquals(lengthInMonths, retrievedMonths);
    }

    @Test
    void setProjectLengthInMonths() {
        double months = 7;
        this.project1.setProjectLengthInMonths(months);
        assertEquals(months, this.project1.getProjectLengthInMonths());
    }

    @Test
    void getCurrentBookedMonths() {
        double currentMonths = 12;
        double retrievedMonths = this.project1.getCurrentBookedMonths();
        assertEquals(currentMonths, retrievedMonths);
    }

    @Test
    void setCurrentBookedMonths() {
        double currentMonths = 7;
        this.project1.setCurrentBookedMonths(currentMonths);
        assertEquals(currentMonths, this.project1.getCurrentBookedMonths());
    }

    @Test
    void getRemainingBookedMonths() {
        double remainingMonths = 0;
        double retrievedMonths = this.project1.getRemainingBookedMonths();
        assertEquals(remainingMonths, retrievedMonths);
    }

    @Test
    void setRemainingBookedMonths() {
        double remainingMonths = 6;
        this.project1.setRemainingBookedMonths(remainingMonths);
        assertEquals(remainingMonths, this.project1.getRemainingBookedMonths());
    }

    @Test
    void getNumberOfEmployees() {
        int numberEmployees = 2;
        double retrievedNumberEmployees = this.project1.getNumberOfEmployees();
        assertEquals(numberEmployees, retrievedNumberEmployees);
    }

    @Test
    void setNumberOfEmployees() {
        int numberEmployees = 5;
        this.project1.setNumberOfEmployees(numberEmployees);
        assertEquals(numberEmployees, this.project1.getNumberOfEmployees());
    }

    @Test
    void getComment() {
        String comment = "Test Comment 1";
        String retrievedComment = this.project1.getComment();
        assertNotNull(retrievedComment);
        assertEquals(comment, retrievedComment);
    }

    @Test
    void setComment() {
        String comment = "Set Comment 1";
        this.project1.setComment(comment);
        assertNotNull(this.project1.getComment());
        assertEquals(comment, this.project1.getComment());
    }

    @Test
    public void testToString() {
        Long id = 1L;
        Long projectNumber = 111L;
        String name = "Test Project 1";
        String startDate = "2021-01-01";
        String endDate = "2021-12-31";
        double projectLengthInMonths = 12.0;
        double currentBookedMonths = 12.0;
        double remainingBookedMonths = 0.0;
        int numberOfEmployees = 2;
        String comment = "Test Comment 1";
        boolean archived = false;

        Project project = new Project();
        project.setId(id);
        project.setProjectNumber(projectNumber);
        project.setName(name);
        project.setStartDate(startDate);
        project.setEndDate(endDate);
        project.setProjectLengthInMonths(projectLengthInMonths);
        project.setCurrentBookedMonths(currentBookedMonths);
        project.setRemainingBookedMonths(remainingBookedMonths);
        project.setNumberOfEmployees(numberOfEmployees);
        project.setComment(comment);
        project.setArchived(archived);

        String toStringResult = project.toString();

        String expectedToString =
                "Project{" +
                        "id=" + id +
                        ", projectNumber=" + projectNumber +
                        ", name='" + name + '\'' +
                        ", startDate='" + startDate + '\'' +
                        ", endDate='" + endDate + '\'' +
                        ", projectLengthInMonths=" + projectLengthInMonths +
                        ", currentBookedMonths=" + currentBookedMonths +
                        ", remainingBookedMonths=" + remainingBookedMonths +
                        ", numberOfEmployees=" + numberOfEmployees +
                        ", comment='" + comment + '\'' +
                        ", archived='" + archived + '\'' +
                        '}';
        assertEquals(expectedToString, toStringResult);
    }
}