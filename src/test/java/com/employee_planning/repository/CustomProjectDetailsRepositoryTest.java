package com.employee_planning.repository;

import com.employee_planning.model.CustomProjectDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "/EmployeeProjectTest.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class CustomProjectDetailsRepositoryTest {

    @Autowired
    private CustomProjectDetailsRepository customProjectDetailsRepository;

    @Test
    void listAllCustomProjectDetailsByProjectId() {
        List<CustomProjectDetails> customProjectDetailsList = customProjectDetailsRepository.listAllCustomProjectDetailsByProjectId(111L);
        assertNotNull(customProjectDetailsList);
        assertEquals(2, customProjectDetailsList.size());
    }
}