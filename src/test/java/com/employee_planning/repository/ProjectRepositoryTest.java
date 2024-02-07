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
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void existsByName() {
        String existingName = "Test Project 1";
        boolean exists = projectRepository.existsByName(existingName);
        assertTrue(exists);
    }

    @Test
    public void testExistsByName_NonExistingName_ReturnsFalse() {
        String nonExistingName = "Non Existing Project";
        boolean exists = projectRepository.existsByName(nonExistingName);
        assertFalse(exists);
    }

    @Test
    void existsByProjectNumber() {
        Long existingName = 1L;
        boolean exists = projectRepository.existsByProjectNumber(existingName);
        assertTrue(exists);
    }

    @Test
    void existsByProjectNumber_FailsNonExistentProjectNumber() {
        Long existingName = 999L;
        boolean exists = projectRepository.existsByProjectNumber(existingName);
        assertFalse(exists);
    }

}