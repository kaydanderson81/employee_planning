package com.employee_planning.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "/EmployeeProjectTest.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void testExistsByName_ExistingName_ReturnsTrue() {
        String existingName = "Test Employee 1";
        boolean exists = employeeRepository.existsByName(existingName);
        assertTrue(exists);
    }

    @Test
    public void testExistsByName_NonExistingName_ReturnsFalse() {
        String nonExistingName = "Non Existing Employee";
        boolean exists = employeeRepository.existsByName(nonExistingName);
        assertFalse(exists);
    }
}