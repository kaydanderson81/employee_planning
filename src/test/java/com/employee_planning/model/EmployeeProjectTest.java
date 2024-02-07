package com.employee_planning.model;

import com.employee_planning.EmployeeProjectTestBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeProjectTest extends EmployeeProjectTestBase {

    @Test
    void getId() {
        long id = 1L;
        long retrievedId = this.employeeProject1.getId();
        assertEquals(id, retrievedId);

    }

    @Test
    void setId() {
        long setId = 11L;
        this.employeeProject1.setId(setId);
        assertEquals(setId, this.employeeProject1.getId());
    }

    @Test
    void getEmployee() {
        Employee employee = this.employee1;
        Employee retrievedEmployee = this.employeeProject1.getEmployee();
        assertNotNull(retrievedEmployee);
        assertEquals(employee, retrievedEmployee);
    }

    @Test
    void setEmployee() {
        Employee setEmployee = employee3;
        this.employeeProject1.setEmployee(setEmployee);
        assertNotNull(this.employeeProject1.getEmployee());
        assertEquals(setEmployee, this.employeeProject1.getEmployee());
    }

    @Test
    void getProject() {
        Project project = this.project1;
        Project retrievedProject = this.employeeProject1.getProject();
        assertNotNull(this.employeeProject1.getProject());
        assertEquals(project, retrievedProject);
    }

    @Test
    void setProject() {
        Project setProject = this.project3;
        this.employeeProject1.setProject(setProject);
        assertNotNull(this.employeeProject1.getProject());
        assertEquals(setProject, this.employeeProject1.getProject());

    }

    @Test
    void getEmployeeBookedMonths() {
        double months = 6.0;
        double retrievedMonths = this.employeeProject1.getEmployeeBookedMonths();
        assertEquals(months, retrievedMonths);
    }

    @Test
    void setEmployeeBookedMonths() {
        double setMonths = 10.0;
        this.employeeProject1.setEmployeeBookedMonths(setMonths);
        assertEquals(setMonths, this.employeeProject1.getEmployeeBookedMonths());
    }

    @Test
    void getEmployeeProjectStartDate() {
        String startDate = "2021-01-01";
        String retrievedStartDate = this.employeeProject1.getEmployeeProjectStartDate();
        assertEquals(startDate, retrievedStartDate);
    }

    @Test
    void setEmployeeProjectStartDate() {
        String startDate = "2022-02-01";
        this.employeeProject1.setEmployeeProjectStartDate(startDate);
        assertEquals(startDate, this.employeeProject1.getEmployeeProjectStartDate());
    }

    @Test
    void getEmployeeProjectEndDate() {
        String endDate = "2021-12-31";
        String retrievedEndDate = this.employeeProject1.getEmployeeProjectEndDate();
        assertEquals(endDate, retrievedEndDate);
    }

    @Test
    void setEmployeeProjectEndDate() {
        String endDate = "2022-12-31";
        this.employeeProject1.setEmployeeProjectEndDate(endDate);
        assertEquals(endDate, this.employeeProject1.getEmployeeProjectEndDate());
    }

    @Test
    void getEmployeeProjectName() {
        String name = "Test Project 1";
        String retrievedName = this.employeeProject1.getProject().getName();
        assertEquals(name, retrievedName);
    }

    @Test
    void setEmployeeProjectName() {
        String name = "Set Name";
        this.employeeProject1.getProject().setName(name);
        assertEquals(name, this.employeeProject1.getProject().getName());
    }

    @Test
    void getConfirmBooking() {
        boolean confirm = true;
        boolean retrievedConfirm = this.employeeProject1.getConfirmBooking();
        assertEquals(confirm, retrievedConfirm);
    }

    @Test
    void setConfirmBooking() {
        boolean setConfirm = false;
        this.employeeProject1.setConfirmBooking(setConfirm);
        assertEquals(setConfirm, this.employeeProject1.getConfirmBooking());
    }

    @Test
    void getPercentage() {
        int percentage = 100;
        int retrievedPercentage = this.employeeProject1.getPercentage();
        assertEquals(percentage, retrievedPercentage);
    }

    @Test
    void setPercentage() {
        int setPercentage = 50;
        this.employeeProject1.setPercentage(setPercentage);
        assertEquals(setPercentage, this.employeeProject1.getPercentage());
    }

    @Test
    void testEquals() {
        EmployeeProject equalInstance = new EmployeeProject();
        equalInstance.setId(1L);
        equalInstance.setEmployee(employee1);
        equalInstance.setProject(project1);
        equalInstance.setEmployeeBookedMonths(6.0);
        equalInstance.setEmployeeProjectStartDate("2021-01-01");
        equalInstance.setEmployeeProjectEndDate("2021-12-31");
        equalInstance.setEmployeeProjectName("Test Project 1");
        equalInstance.setConfirmBooking(true);
        equalInstance.setPercentage(100);
        assertEquals(employeeProject1, equalInstance);
        assertNotEquals(employeeProject1, employeeProject2);
    }

    @Test
    void testHashCode() {
        Employee employee1 = new Employee();
        employee1.setId(101L);

        Project project1 = new Project();
        project1.setId(111L);

        EmployeeProject employeeProject1 = new EmployeeProject();
        employeeProject1.setId(1L);
        employeeProject1.setEmployee(employee1);
        employeeProject1.setProject(project1);
        employeeProject1.setEmployeeBookedMonths(6.0);
        employeeProject1.setEmployeeProjectStartDate("2021-01-01");
        employeeProject1.setEmployeeProjectEndDate("2021-12-31");
        employeeProject1.setEmployeeProjectName("Test Project 1");

        EmployeeProject employeeProject2 = new EmployeeProject();
        employeeProject2.setId(1L);
        employeeProject2.setEmployee(employee1);
        employeeProject2.setProject(project1);
        employeeProject2.setEmployeeBookedMonths(6.0);
        employeeProject2.setEmployeeProjectStartDate("2021-01-01");
        employeeProject2.setEmployeeProjectEndDate("2021-12-31");
        employeeProject2.setEmployeeProjectName("Test Project 1");

        EmployeeProject differentInstance = new EmployeeProject();
        differentInstance.setId(3L);
        differentInstance.setEmployee(employee1);
        differentInstance.setProject(project1);
        differentInstance.setEmployeeBookedMonths(9.0);
        differentInstance.setEmployeeProjectStartDate("2022-01-01");
        differentInstance.setEmployeeProjectEndDate("2022-12-31");
        differentInstance.setEmployeeProjectName("Test Project 2");

        assertEquals(employeeProject1.hashCode(), employeeProject2.hashCode());
        assertNotEquals(employeeProject1.hashCode(), differentInstance.hashCode());
    }

    @Test
    void testToString() {
        Employee employee = new Employee(1L, "Test", "Employee1", "Test Employee 1", "2021-01-01", "2021-12-31", true);
        Project project = new Project(1L, 111L, "Test Project 1", "2021-01-01", "2021-12-31", 12.0, 12.0, 0.0, 2, "Test Comment 1", true);

        EmployeeProject employeeProject = new EmployeeProject(1L, employee, project, 6.0, "2021-01-01", "2021-12-31", "Test Project 1", true, 100);

        String expectedToString = "EmployeeProject{" +
                "id=1, " +
                "project=Project{id=1, projectNumber=111, name='Test Project 1', startDate='2021-01-01', " +
                "endDate='2021-12-31', projectLengthInMonths=12.0, currentBookedMonths=12.0, " +
                "remainingBookedMonths=0.0, numberOfEmployees=2, comment='Test Comment 1', archived='true'}, " +
                "employeeBookedMonths=6.0, employeeProjectStartDate='2021-01-01', " +
                "employeeProjectEndDate='2021-12-31', employeeProjectName='Test Project 1', " +
                "confirmBooking=true, percentage=100}";

        assertEquals(expectedToString, employeeProject.toString());
    }
}