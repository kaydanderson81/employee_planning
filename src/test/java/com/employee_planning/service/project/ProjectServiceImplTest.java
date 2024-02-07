package com.employee_planning.service.project;

import com.employee_planning.EmployeeProjectTestBase;
import com.employee_planning.model.Project;
import com.employee_planning.model.chart.ChartYear;
import com.employee_planning.repository.EmployeeProjectRepository;
import com.employee_planning.repository.ProjectRepository;
import com.employee_planning.service.employeeProject.EmployeeProjectServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
class ProjectServiceImplTest extends EmployeeProjectTestBase {

    @InjectMocks
    private ProjectServiceImpl projectServiceImpl;

    @Mock
    private ProjectService projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private EmployeeProjectRepository employeeProjectRepository;

    @Mock
    private EmployeeProjectServiceImpl employeeProjectService;

    @Mock
    private CustomProjectDetailsService customProjectDetailsService;



    @Test
    @DisplayName("Should get all projects from database")
    void getAllProjects() {
        when(projectRepository.findAll()).thenReturn(projects);
        List<Project> getAllProjects = projectServiceImpl.getAllProjects();
        assertEquals(4, getAllProjects.size());
        assertNotNull(getAllProjects);
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should save a Project to database")
    void saveProject() {
        Project newProject = new Project();
        projectServiceImpl.saveProject(newProject);
        verify(projectRepository, times(1)).save(newProject);
    }

    @Test
    @DisplayName("Should return a Project by Id")
    void getProjectById() {
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project1));
        Project existingProject = projectServiceImpl.getProjectById(1L);
        assertNotNull(existingProject);
        assertEquals(existingProject.getId(), project1.getId());
    }

    @Test
    @DisplayName("Should return a Project that doesn't exist by Id")
    void getProjectById_RuntimeException() {
        long id = 999L;
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                projectServiceImpl.getProjectById(id));
        String expectedMessage = "Project not found for id :: " + id;
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Should delete a Project by id")
    void deleteProjectById() {
        projectServiceImpl.deleteProjectById(project1.getId());
        verify(projectRepository, times(1)).deleteById(project1.getId());
    }

    @Test
    void setUpdatedProjectAttributes() {
        Project updatedProject = new Project();
        updatedProject.setProjectNumber(2L);
        updatedProject.setName("Project 2");
        updatedProject.setStartDate("2023-01-01");
        updatedProject.setEndDate("Date Not Set");
        updatedProject.setProjectLengthInMonths(24);
        updatedProject.setComment("Updated comment");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project1));

        Project result = projectServiceImpl.setUpdatedProjectAttributes(1L, updatedProject);

        Assertions.assertEquals(project1.getId(), result.getId());
        Assertions.assertEquals(updatedProject.getProjectNumber(), result.getProjectNumber());
        Assertions.assertEquals(updatedProject.getName(), result.getName());
        Assertions.assertEquals(updatedProject.getStartDate(), result.getStartDate());
        Assertions.assertEquals(project1.getEndDate(), result.getEndDate());
        Assertions.assertEquals(updatedProject.getProjectLengthInMonths(), result.getProjectLengthInMonths());
        Assertions.assertEquals(updatedProject.getComment(), result.getComment());
        Assertions.assertEquals("2023-01-01", result.getStartDate());
        Assertions.assertEquals("Date Not Set", result.getEndDate());
    }

    @Test
    void setUpdatedProjectAttributes_WithDateNotSet() {
        Project updatedProject = new Project();
        updatedProject.setProjectNumber(2L);
        updatedProject.setName("Project 2");
        updatedProject.setStartDate("");
        updatedProject.setEndDate("");
        updatedProject.setProjectLengthInMonths(24);
        updatedProject.setComment("Updated comment");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project1));

        Project result = projectServiceImpl.setUpdatedProjectAttributes(1L, updatedProject);

        Assertions.assertEquals(project1.getId(), result.getId());
        Assertions.assertEquals(updatedProject.getProjectNumber(), result.getProjectNumber());
        Assertions.assertEquals(updatedProject.getName(), result.getName());
        Assertions.assertEquals("Date Not Set", result.getStartDate());
        Assertions.assertEquals(project1.getEndDate(), result.getEndDate());
        Assertions.assertEquals(updatedProject.getProjectLengthInMonths(), result.getProjectLengthInMonths());
        Assertions.assertEquals(updatedProject.getComment(), result.getComment());
        Assertions.assertEquals("Date Not Set", result.getStartDate());
        Assertions.assertEquals("Date Not Set", result.getEndDate());
    }

    @Test
    @DisplayName("Should update employee project booked months")
    void updateEmployeeProjectBookedMonths() {
        List<Double> listDoubles = Arrays.asList(6.0, 9.0, 12.0);
        double total = listDoubles.stream().mapToDouble(Double::doubleValue).sum();

        List<Project> projects = new ArrayList<>();
        projects.add(project1);

        when(employeeProjectRepository.listAllEmployeeBookedMonthsByProjectId(anyLong()))
                .thenReturn(listDoubles);

        when(projectRepository.findAll()).thenReturn(projects);
        when(projectRepository.save(any(Project.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        projectServiceImpl.updateEmployeeProjectBookedMonths();

        assertEquals(total, project1.getCurrentBookedMonths(), 0.01);
        assertEquals(project1.getProjectLengthInMonths() - total, project1.getRemainingBookedMonths(), 0.01);

        verify(employeeProjectRepository, times(1)).listAllEmployeeBookedMonthsByProjectId(anyLong());
        verify(projectRepository, times(1)).save(any(Project.class));  // Update the verification here

    }

    @Test
    void testGetAListOfProjectsAndTheirPersonMonthsByYear() {
        ChartYear year = new ChartYear("2022");
        this.projects.add(project5);
        this.projects.add(new Project(6L, 666L, "Test Project 6", "Date Not Set", "Date Not Set",
                12, 12, 0, 2, "Test Comment 1", false));
        when(projectRepository.findAllProjectsWhereArchivedIsFalse()).thenReturn(projects);
        when(employeeProjectService.getAllEmployeeProjectsByProjectId(anyLong())).thenReturn(employeeProjects);
        when(customProjectDetailsService.getAllCustomProjectDetailsByProjectId(anyLong()))
                .thenReturn(this.customProjectDetailsList);
        List<Project> result = projectServiceImpl.getAListOfProjectsAndTheirPersonMonthsByYear(year);
        assertEquals(projects.size(), result.size());
    }


    @Test
    void findPaginated() {
        int pageNo = 1;
        int pageSize = 10;
        String sortField = "name";
        String sortDirection = "asc";

        Page<Project> expectedPage = new PageImpl<>(this.projects);

        Pageable expectedPageable = PageRequest.of(0, pageSize, Sort.by(sortField).ascending());
        when(this.projectRepository.findByArchivedFalse(expectedPageable)).thenReturn(expectedPage);

        Page<Project> result = this.projectServiceImpl.findPaginatedArchivedFalse(pageNo, pageSize, sortField, sortDirection);

        assertEquals(expectedPage, result);
        verify(this.projectRepository, times(1)).findByArchivedFalse(expectedPageable);
    }

    @Test
    void findArchivedPaginated() {
        int pageNo = 1;
        int pageSize = 10;
        String sortField = "name";
        String sortDirection = "asc";

        Page<Project> expectedPage = new PageImpl<>(this.projects);

        Pageable expectedPageable = PageRequest.of(0, pageSize, Sort.by(sortField).ascending());
        when(this.projectRepository.findByArchivedTrue(expectedPageable)).thenReturn(expectedPage);

        Page<Project> result = this.projectServiceImpl.findPaginatedArchivedTrue(pageNo, pageSize, sortField, sortDirection);

        assertEquals(expectedPage, result);
        verify(this.projectRepository, times(1)).findByArchivedTrue(expectedPageable);
    }


    @Test
    void getListOfEmployeeBookedMonths() {
        String startDates = "2023-01-01";
        String endDates = "2023-06-30";

        double expectedMonthsBetween = 6.0;

        when(projectService.getListOfEmployeeBookedMonths(startDates, endDates))
                .thenReturn(expectedMonthsBetween);

        Double result = projectService.getListOfEmployeeBookedMonths(startDates, endDates);

        assertEquals(expectedMonthsBetween, result, 0.01);
    }

    @Test
    void getListOfEmployeeBookedMonths_WithStartDateStartingWith15() {
        String startDates = "2023-01-01";
        String endDates = "2023-06-15";

        double expectedMonthsBetween = 5.5;

        when(projectService.getListOfEmployeeBookedMonths(startDates, endDates))
                .thenReturn(expectedMonthsBetween);

        Double result = projectService.getListOfEmployeeBookedMonths(startDates, endDates);

        assertEquals(expectedMonthsBetween, result, 0.01);
        assertEquals(expectedMonthsBetween, result);
    }
}