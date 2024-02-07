package com.employee_planning.service.project;

import com.employee_planning.EmployeeProjectTestBase;
import com.employee_planning.model.CustomProjectDetails;
import com.employee_planning.repository.CustomProjectDetailsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
class CustomProjectDetailsServiceImplTest extends EmployeeProjectTestBase {

    @Mock
    private CustomProjectDetailsRepository customProjectDetailsRepository;

    @InjectMocks
    private CustomProjectDetailsServiceImpl customProjectDetailsServiceImpl;

    @Test
    @DisplayName("Should get all CustomProjectDetails from database by a Project id")
    void getAllCustomProjectDetailsByProjectId() {
        when(customProjectDetailsRepository.listAllCustomProjectDetailsByProjectId(anyLong())).thenReturn(this.customProjectDetailsList);
        List<CustomProjectDetails> customProjectDetails = customProjectDetailsServiceImpl.getAllCustomProjectDetailsByProjectId(anyLong());
        assertEquals(2, customProjectDetails.size());
        assertNotNull(customProjectDetails);
        verify(customProjectDetailsRepository, times(1)).listAllCustomProjectDetailsByProjectId(anyLong());
    }

    @Test
    @DisplayName("Should save a CustomProjectDetails to database")
    void saveCustomProjectDetails() {
        CustomProjectDetails customProjectDetails = new CustomProjectDetails();
        customProjectDetailsServiceImpl.saveCustomProjectDetails(customProjectDetails);
        verify(customProjectDetailsRepository, times(1)).save(customProjectDetails);
    }

    @Test
    @DisplayName("Should return a CustomProjectDetails by Id")
    void getCustomProjectDetailsById() {
        when(customProjectDetailsRepository.findById(anyLong())).thenReturn(Optional.ofNullable(this.customProjectDetails1));
        CustomProjectDetails customProjectDetails = customProjectDetailsServiceImpl.getCustomProjectDetailsById(1L);
        assertNotNull(customProjectDetails);
        assertEquals(customProjectDetails.getId(), this.customProjectDetails1.getId());
    }

    @Test
    @DisplayName("Should return a Project that doesn't exist by Id")
    void getCustomProjectDetailsById_RuntimeException() {
        long id = 999L;
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                customProjectDetailsServiceImpl.getCustomProjectDetailsById(id));
        String expectedMessage = "Custom Project Details not found for id :: " + id;
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Should delete a CustomProjectDetails by id")
    void deleteCustomProjectDetails() {
        customProjectDetailsServiceImpl.deleteCustomProjectDetails(this.customProjectDetails1.getId());
        verify(customProjectDetailsRepository, times(1)).deleteById(this.customProjectDetails1.getId());
    }

    @Test
    void deleteAllCustomProjectDetailsByProjectId() {

        customProjectDetailsServiceImpl.deleteCustomProjectDetails(this.customProjectDetails1.getId());
        verify(customProjectDetailsRepository, times(1)).deleteById(this.customProjectDetails1.getId());
    }

    @Test
    void checkIfCustomProjectYearExists_True() {
        when(customProjectDetailsRepository.listAllCustomProjectDetailsByProjectId(anyLong())).thenReturn(this.customProjectDetailsList);
        boolean result = customProjectDetailsServiceImpl.checkIfCustomProjectYearExists(this.project1.getId(), "2023");
        assertTrue(result);
    }

    @Test
    void checkIfCustomProjectYearExists_False() {
        when(customProjectDetailsRepository.listAllCustomProjectDetailsByProjectId(anyLong())).thenReturn(this.customProjectDetailsList);
        boolean result = customProjectDetailsServiceImpl.checkIfCustomProjectYearExists(this.project1.getId(), "2000");
        assertFalse(result);
    }
}