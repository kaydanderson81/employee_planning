package com.employee_planning.repository;

import com.employee_planning.model.EmployeeProject;
import com.employee_planning.EmployeeProjectTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "/EmployeeProjectTest.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class EmployeeProjectRepositoryTest extends EmployeeProjectTestBase {

    @Autowired
    private EmployeeProjectRepository employeeProjectRepository;

    @Test
    @DisplayName("Should find all EmployeeProject by Employee id")
    void findAllEmployeeProjectByEmployeeId() {
        List<EmployeeProject> employees = employeeProjectRepository.findAllEmployeeProjectByEmployeeId(101L);
        System.out.println(employees);
        assertNotNull(employees);
        assertEquals(3, employees.size());
    }

    @Test
    @DisplayName("Should find all EmployeeProject employeeBookedMonths by Project id")
    void listAllEmployeeBookedMonthsByProjectId() {
        List<Double> employeeProjectBookedMonths = employeeProjectRepository.listAllEmployeeBookedMonthsByProjectId(111L);
        List<Double> doubleList = new ArrayList<>();
        doubleList.add(6.0);
        doubleList.add(9.0);
        doubleList.add(12.0);
        assertNotNull(employeeProjectBookedMonths);
        assertEquals(doubleList, employeeProjectBookedMonths);
        assertEquals(3, employeeProjectBookedMonths.size());
    }

    @ParameterizedTest
    @ValueSource(longs = { 111L, 222L })
    @DisplayName("Should find all employeeProject employeeBookedMonths by Project id with different parameters")
    void listAllEmployeeBookedMonthsByProjectId_Parameterized(Long projectId) {
        List<Double> employeeProjectBookedMonths = employeeProjectRepository.listAllEmployeeBookedMonthsByProjectId(projectId);
        List<Double> expectedBookedMonths1 = new ArrayList<>();
        List<Double> expectedBookedMonths2 = new ArrayList<>();
        expectedBookedMonths1.add(6.0);
        expectedBookedMonths1.add(9.0);
        expectedBookedMonths1.add(12.0);
        expectedBookedMonths2.add(9.0);
        assertNotNull(employeeProjectBookedMonths);
        if (projectId.equals(111L)) {
            assertEquals(expectedBookedMonths1, employeeProjectBookedMonths);
        } else if (projectId.equals(222L)) {
            assertEquals(expectedBookedMonths2, employeeProjectBookedMonths);
        }
    }

    @Test
    @DisplayName("Should find all EmployeeProject employeeProjectStartDate")
    void findAllEmployeeProjectStartDates() {
        Set<String> employeeProjectStartDates = employeeProjectRepository.findAllEmployeeProjectStartDates();
        Set<String> expectedStartDates = new HashSet<>();
        expectedStartDates.add("2021-01-01");
        expectedStartDates.add("2022-01-01");
        expectedStartDates.add("2023-01-01");
        expectedStartDates.add("2022-04-01");
        expectedStartDates.add("2023-05-01");
        assertNotNull(employeeProjectStartDates);
        assertEquals(5, employeeProjectStartDates.size());
        assertEquals(expectedStartDates, employeeProjectStartDates);
    }

    @Test
    @DisplayName("Should find all EmployeeProject employeeProjectEndDate")
    void findAllEmployeeProjectEndDates() {
        Set<String> employeeProjectEndDates = employeeProjectRepository.findAllEmployeeProjectEndDates();
        Set<String> expectedEndDates = new HashSet<>();
        expectedEndDates.add("2021-12-31");
        expectedEndDates.add("2022-12-31");
        expectedEndDates.add("2023-12-31");
        assertNotNull(employeeProjectEndDates);
        assertEquals(3, employeeProjectEndDates.size());
        assertEquals(expectedEndDates, employeeProjectEndDates);
    }

    @Test
    @DisplayName("Should find all EmployeeProject by Project id")
    void getAllEmployeeProjectsByProjectId() {
        List<EmployeeProject> employeeProjectList = employeeProjectRepository.getAllEmployeeProjectsByProjectId(111L);
        assertNotNull(employeeProjectList);
        assertEquals(3, employeeProjectList.size());
    }
}