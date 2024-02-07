package com.employee_planning.model;

import com.employee_planning.EmployeeProjectTestBase;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EmployeeTest extends EmployeeProjectTestBase {

    @Test
    void getId() {
        Long id = 1L;
        Employee employee = this.employee1;
        Long retrievedId = employee.getId();
        assertNotNull(retrievedId);
        assertEquals(id, retrievedId);
    }

    @Test
    void setId() {
        Long testEmployeeId = 111L;
        Employee employee = this.employee1;
        employee.setId(testEmployeeId);
        assertNotNull(employee);
        assertEquals(testEmployeeId, employee.getId());
    }

    @Test
    void getFirstName() {
        String name = "Test";
        Employee employee = this.employee1;
        String retrievedName = employee.getFirstName();
        assertNotNull(retrievedName);
        assertEquals(name, retrievedName);
    }

    @Test
    void setFirstName() {
        String name = "Set Name";
        this.employee1.setFirstName(name);
        assertNotNull(this.employee1);
        assertEquals(name, this.employee1.getFirstName());
    }

    @Test
    void getLastName() {
        String name = "Employee1";
        Employee employee = this.employee1;
        String retrievedName = employee.getLastName();
        assertNotNull(retrievedName);
        assertEquals(name, retrievedName);
    }

    @Test
    void setLastName() {
        String name = "Set Name";
        employee1.setLastName(name);
        assertNotNull(employee1);
        assertEquals(name, employee1.getLastName());
    }

    @Test
    void getName() {
        String name = "Test Employee 1";
        Employee employee = this.employee1;
        String retrievedName = employee.getName();
        assertNotNull(retrievedName);
        assertEquals(name, employee.getName());
    }

    @Test
    void setName() {
        String firstName = "Update";
        String lastName = "Name";
        Employee employee = this.employee1;
        employee.setName(firstName, lastName);
        assertNotNull(employee);
        assertEquals(firstName + " " + lastName, employee.getName());
    }

    @Test
    void getContractedFrom() {
        String dateFrom = "2021-01-01";
        Employee employee = this.employee1;
        String retrievedDate = employee.getContractedFrom();
        assertNotNull(retrievedDate);
        assertEquals(dateFrom, retrievedDate);
    }

    @Test
    void setContractedFrom() {
        String dateFrom = "2022-02-01";
        Employee employee = this.employee1;
        employee.setContractedFrom(dateFrom);
        assertNotNull(employee.getContractedFrom());
        assertEquals(dateFrom, employee.getContractedFrom());
    }

    @Test
    void getContractedTo() {
        String dateTo = "2021-12-31";
        Employee employee = this.employee1;
        String retrievedDate = employee.getContractedTo();
        assertNotNull(retrievedDate);
        assertEquals(dateTo, retrievedDate);
    }

    @Test
    void setContractedTo() {
        String dateTo = "2022-11-30";
        Employee employee = this.employee1;
        employee.setContractedTo(dateTo);
        assertNotNull(employee.getContractedTo());
        assertEquals(dateTo, employee.getContractedTo());
    }

    @Test
    void getEmployeeProjects() {
        List<EmployeeProject> employeeProjectList = this.employee1.getEmployeeProjects();
        assertNotNull(employeeProjectList);
        assertEquals(3, employeeProjectList.size());
        assertEquals(9.0, employeeProjectList.get(1).getEmployeeBookedMonths());
        assertEquals("2023-05-01", employeeProjectList.get(2).getEmployeeProjectStartDate());
    }

    @Test
    void setEmployeeProject() {
        List<EmployeeProject> employeeProjectList = new ArrayList<>();
        employeeProjectList.add(this.employeeProject5);
        employeeProjectList.add(this.employeeProject4);
        employeeProjectList.add(this.employeeProject3);
        this.employee3.setEmployeeProject(employeeProjectList);

        assertNotNull(employeeProjectList);
        assertEquals(3, employeeProjectList.size());
        assertEquals("2023-12-31", employeeProjectList.get(0).getEmployeeProjectEndDate());
    }

    @Test
    void addEmployeeProject() {
        assertEquals(5, this.employeeProjects.size());
        employeeProjects.add(employeeProjectToAdd);
        assertNotNull(this.employeeProjects);
        assertEquals(6, this.employeeProjects.size());
    }

    @Test
    void testToString() {
        Long id = 1L;
        String firstName = "John";
        String lastName = "Doe";
        String contractedFrom = "2021-01-01";
        String contractedTo = "2021-12-31";
        boolean archived = false;
        Employee employee = new Employee();
        employee.setId(id);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setName(firstName, lastName);
        employee.setContractedFrom(contractedFrom);
        employee.setContractedTo(contractedTo);
        List<EmployeeProject> employeeProjects = new ArrayList<>();
        employeeProjects.add(new EmployeeProject());
        employee.setEmployeeProject(employeeProjects);
        employee.setArchived(archived);


        String toStringResult = employee.toString();

        String expectedToString =
                "Employee{" +
                        "id=" + id +
                        ", firstName='" + firstName +'\'' +
                        ", lastName='" + lastName +'\'' +
                        ", name='" + firstName + " " + lastName + '\'' +
                        ", contractedFrom='" + contractedFrom + '\'' +
                        ", contractedTo='" + contractedTo + '\'' +
                        ", employeeProjects=" + employeeProjects +
                        ", archived='" + archived + '\'' +
                        '}';
        assertEquals(expectedToString, toStringResult);
    }
}