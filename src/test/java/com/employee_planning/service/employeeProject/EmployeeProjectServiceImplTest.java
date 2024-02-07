package com.employee_planning.service.employeeProject;

import com.employee_planning.model.EmployeeProject;
import com.employee_planning.repository.EmployeeProjectRepository;
import com.employee_planning.EmployeeProjectTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
class EmployeeProjectServiceImplTest extends EmployeeProjectTestBase {

    @InjectMocks
    private EmployeeProjectServiceImpl employeeProjectService;

    @Mock
    private EmployeeProjectRepository employeeProjectRepository;


    @Test
    @DisplayName("Should get all employeeProjects from database")
    void getAllEmployeeProjects() {
        List<EmployeeProject> employeeProjects = new ArrayList<>();
        employeeProjects.add(employeeProject1);
        employeeProjects.add(employeeProject2);
        employeeProjects.add(employeeProject3);

        when(employeeProjectRepository.findAll()).thenReturn(employeeProjects);
        List<EmployeeProject> getAllEmployeeProjects = employeeProjectService.getAllEmployeeProjects();
        assertEquals(3, getAllEmployeeProjects.size());
        assertNotNull(getAllEmployeeProjects);
        verify(employeeProjectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get an employeeProject by id")
    void getEmployeeProjectById() {
        when(employeeProjectRepository.findById(anyLong())).thenReturn(Optional.of(employeeProject1));
        EmployeeProject existingEmployeeProject = employeeProjectService.getEmployeeProjectById(1L);
        assertNotNull(existingEmployeeProject);
        assertEquals(existingEmployeeProject.getId(), employeeProject1.getId());
    }

    @Test
    @DisplayName("Should save an employeeProject to the database")
    void saveEmployeeProject() {
        EmployeeProject newEmployeeProject = new EmployeeProject();
        employeeProjectService.saveEmployeeProject(newEmployeeProject);
        verify(employeeProjectRepository, times(1)).save(newEmployeeProject);
    }

    @Test
    @DisplayName("Should set the attributes to a new EmployeeProject")
    void setEmployeeProjectAttributes() {
        double bookedMonths = 6.0;
        EmployeeProject addedEmployeeProject =
                employeeProjectService.setEmployeeProjectAttributes(employee1, employeeProject2, bookedMonths);
        assertNotNull(addedEmployeeProject);
        assertEquals("Test Project 1" , addedEmployeeProject.getEmployeeProjectName());
        assertEquals(100, addedEmployeeProject.getPercentage());
        assertEquals(bookedMonths, addedEmployeeProject.getEmployeeBookedMonths());
    }

    @Test
    @DisplayName("Should set the attributes when updating an EmployeeProject")
    void setUpdatedEmployeeProjectAttributes() {
        long id = 2L;
        double bookedMonths = 6.0;
        when(employeeProjectRepository.findById(id)).thenReturn(Optional.ofNullable(employeeProject2));
        EmployeeProject addedEmployeeProject =
                employeeProjectService.setUpdatedEmployeeProjectAttributes(id, employeeProject5, bookedMonths);
        assertNotNull(addedEmployeeProject);
        assertEquals("Test Project 1" , addedEmployeeProject.getEmployeeProjectName());
        assertEquals(50, addedEmployeeProject.getPercentage());
        assertEquals(bookedMonths / 2, addedEmployeeProject.getEmployeeBookedMonths());
    }


    @Test
    @DisplayName("Should return a list of employeeProjects for a project by project id")
    void getAllEmployeeProjectsByProjectId() {
        long id = 1L;
        List<EmployeeProject> employeeProjects = new ArrayList<>();
        employeeProjects.add(employeeProject1);
        employeeProjects.add(employeeProject2);
        employeeProjects.add(employeeProject3);

        when(employeeProjectRepository.getAllEmployeeProjectsByProjectId(id)).thenReturn(employeeProjects);
        List<EmployeeProject> getAllEmployeeProjects = employeeProjectService.getAllEmployeeProjectsByProjectId(id);

        assertEquals(getAllEmployeeProjects.size(), 3);
        assertNotNull(getAllEmployeeProjects);
        verify(employeeProjectRepository, times(1)).getAllEmployeeProjectsByProjectId(id);
    }

    @Test
    @DisplayName("Should delete an employeeProject by id")
    void deleteEmployeeProjectById() {
        employeeProjectService.deleteEmployeeProjectById(employeeProject1.getId());
        verify(employeeProjectRepository, times(1)).deleteById(employeeProject1.getId());
    }

    @Test
    @DisplayName("Should return a list of Years (as strings) that are currently active EmployeeProjects in the database")
    void findAllStartAndEndDatesByYear() {
        Set<String> startDates = new HashSet<>();
        startDates.add(employeeProject1.getEmployeeProjectStartDate());
        startDates.add(employeeProject2.getEmployeeProjectStartDate());
        startDates.add(employeeProject3.getEmployeeProjectStartDate());

        Set<String> endDates = new HashSet<>();
        endDates.add(employeeProject1.getEmployeeProjectEndDate());
        endDates.add(employeeProject2.getEmployeeProjectEndDate());
        endDates.add(employeeProject3.getEmployeeProjectEndDate());
        when(employeeProjectRepository.findAllEmployeeProjectStartDates()).thenReturn(startDates);
        when(employeeProjectRepository.findAllEmployeeProjectEndDates()).thenReturn(endDates);

        List<String> result = employeeProjectService.findAllStartAndEndDatesByYear();

        List<String> expected = Arrays.asList("2021", "2022", "2023");
        assertEquals(expected, result);

        verify(employeeProjectRepository, times(1)).findAllEmployeeProjectStartDates();
        verify(employeeProjectRepository, times(1)).findAllEmployeeProjectEndDates();
    }

    @Test
    void getEmployeeBookedMonthsByPercentage() {
        int percentage = 50;
        double bookedMonths = 10.0;
        Double expectedBookedMonths = 5.0;
        Double actualBookedMonths = employeeProjectService.getEmployeeBookedMonthsByPercentage(percentage, bookedMonths);
        assertEquals(expectedBookedMonths, actualBookedMonths);

    }
}