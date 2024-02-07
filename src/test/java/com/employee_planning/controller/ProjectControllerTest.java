package com.employee_planning.controller;

import com.employee_planning.EmployeeProjectTestBase;
import com.employee_planning.model.Employee;
import com.employee_planning.model.EmployeeProject;
import com.employee_planning.model.Project;
import com.employee_planning.repository.CustomProjectDetailsRepository;
import com.employee_planning.repository.EmployeeProjectRepository;
import com.employee_planning.repository.ProjectRepository;
import com.employee_planning.service.employee.EmployeeService;
import com.employee_planning.service.employeeProject.EmployeeProjectService;
import com.employee_planning.service.project.ProjectService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class ProjectControllerTest extends EmployeeProjectTestBase {
    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private CustomProjectDetailsRepository customProjectDetailsRepository;
    @InjectMocks
    private ProjectController projectController;

    @MockBean
    private EmployeeProjectService employeeProjectService;

    @MockBean
    private EmployeeProjectRepository employeeProjectRepository;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Test
    @DisplayName("Get request showing the projects page with a list of Project objects")
    void viewProjectsPage() {
        Model model = new ExtendedModelMap();
        when(projectService.findPaginatedArchivedFalse(1, 10, "name", "asc"))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        String result = projectController.viewProjectsPage(model);

        verify(projectService).findPaginatedArchivedFalse(1, 10, "name", "asc");
        assertEquals("project/projects.html", result);
    }

    @Test
    @DisplayName("Get request showing the archived projects page with a list of archived Project objects")
    void viewArchivedProjectsPage() {
        Model model = new ExtendedModelMap();
        when(projectService.findPaginatedArchivedTrue(1, 10, "name", "asc"))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        String result = projectController.viewArchivedProjectsPage(model);

        verify(projectService).findPaginatedArchivedTrue(1, 10, "name", "asc");
        assertEquals("project/archived_projects.html", result);
    }

    @Test
    @DisplayName("Get request showing the form for creating a new Project")
    void showNewProjectForm() throws Exception {
        mockMvc.perform(get("/ines/showNewProjectForm"))
                .andExpect(status().isOk())
                .andExpect(view().name("project/new_project.html"))
                .andExpect(model().attributeExists("project"));

        mockMvc.perform(get("/ines/showNewProjectForm"))
                .andExpect(result -> {
                    Map<String, Object> model = Objects.requireNonNull(result.getModelAndView()).getModel();
                    assertTrue(model.containsKey("project"));
                    Object projectObj = model.get("project");
                    assertTrue(projectObj instanceof Project);
                });
    }

    @Test
    @DisplayName("Post request to save a new Project object")
    void saveProject_Success() throws Exception {
        when(projectRepository.existsByName(Mockito.anyString())).thenReturn(false);
        when(projectRepository.existsByProjectNumber(anyLong())).thenReturn(false);

        mockMvc.perform(post("/ines/saveProject")
                        .flashAttr("project", project1))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("success"))
                .andExpect(MockMvcResultMatchers.flash().attribute("success", "Added project: Test Project 1 to list"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/ines/projects"));

        verify(projectService, Mockito.times(1)).saveProject(project1);

        assertEquals("Test Project 1", project1.getName());
        assertEquals(111, project1.getProjectNumber());
        assertEquals("2021-01-01", project1.getStartDate());
        assertEquals("2021-12-31", project1.getEndDate());
    }

    @Test
    @DisplayName("Post request fail with Project object name already exists")
    public void testSaveProject_Fail_ProjectNameExists() throws Exception {
        Project existingProject = project1;
        existingProject.setName("Existing Project");
        when(projectRepository.existsByName(Mockito.anyString())).thenReturn(true);

        mockMvc.perform(post("/ines/saveProject")
                        .param("name", "Existing Project")
                        .param("projectNumber", "123456")
                        .param("startDate", "2023-01-01")
                        .param("endDate", "2023-12-31"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("failed"))
                .andExpect(MockMvcResultMatchers.flash().attribute("failed", "Project name: Existing Project already exists"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/ines/showNewProjectForm"));

        verify(projectService, never()).saveProject(any());
    }

    @Test
    @DisplayName("Post request fail with Project object projectNumber already exists")
    public void testSaveProject_Fail_ProjectNumberExists() throws Exception {
        Project existingProject = project1;
        existingProject.setProjectNumber(12345L);
        when(projectRepository.existsByProjectNumber(anyLong())).thenReturn(true);

        mockMvc.perform(post("/ines/saveProject")
                        .param("name", "New Project")
                        .param("projectNumber", "123456")
                        .param("startDate", "2023-01-01")
                        .param("endDate", "2023-12-31"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("failed"))
                .andExpect(MockMvcResultMatchers.flash().attribute("failed", "Project with number: 123456 already exists"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/ines/showNewProjectForm"));

        verify(projectService, never()).saveProject(any());
    }

    @Test
    @DisplayName("Get request showing the form for adding a new EmployeeProject object to an Employee")
    void showFormForAddProjectToEmployee() throws Exception {
        long employeeId = 1L;
        Employee employee = new Employee();
        employee.setId(employeeId);
        when(employeeService.getEmployeeById(employeeId)).thenReturn(employee);
        when(projectService.getAllProjects()).thenReturn(projects);

        List<EmployeeProject> employeeProjects = Arrays.asList(employeeProject1, employeeProject2, employeeProject3);
        when(employeeProjectRepository.findAllEmployeeProjectByEmployeeId(employeeId)).thenReturn(employeeProjects);

        mockMvc.perform(get("/ines/showFormForAddProjectToEmployee/{id}", employeeId))
                .andExpect(status().isOk())
                .andExpect(view().name("project/add_Project_To_Employee"))
                .andExpect(model().attributeExists("employee", "projects", "employeeProject", "employeeSavedProject"))
                .andExpect(model().attribute("employee", employee))
                .andExpect(model().attribute("projects", projects))
                .andExpect(model().attribute("employeeProject", new EmployeeProject()))
                .andExpect(model().attribute("employeeSavedProject", employeeProjects));
    }

    @Test
    @DisplayName("Post request test for adding a new EmployeeProject to an existing Employee")
    void testAddProjectToEmployee_Success() throws Exception {
        Employee existingEmployee = employee1;

        when(employeeService.getEmployeeById(existingEmployee.getId())).thenReturn(existingEmployee);
        when(employeeService.getEmployeeBookedMonths(anyString(), anyString())).thenReturn(employeeProjectToAdd.getEmployeeBookedMonths());

        when(employeeProjectService.setEmployeeProjectAttributes(any(Employee.class), any(EmployeeProject.class), anyDouble()))
                .thenReturn(employeeProjectToAdd);

        mockMvc.perform(post("/ines/addProjectToEmployee/{id}", existingEmployee.getId())
                        .param("employeeProjectStartDate", "2023-07-01")
                        .param("employeeProjectEndDate", "2023-10-31"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ines/employees?updatedEmployeeId=" + existingEmployee.getId()));

        verify(employeeService, times(1)).getEmployeeById(existingEmployee.getId());
        verify(employeeService, times(1)).getEmployeeBookedMonths("2023-07-01", "2023-10-31");
        verify(employeeProjectService, times(1)).setEmployeeProjectAttributes(any(Employee.class), any(EmployeeProject.class), anyDouble());
        verify(employeeService, times(1)).saveEmployeeAndEmployeeProject(any(Employee.class), any(EmployeeProject.class));

    }

    @Test
    @DisplayName("Post request test fails adding a new EmployeeProject to an existing Employee with validation error for incorrect employeeProjectStartDate")
    public void testAddProjectToEmployee_ValidationFailed_IncorrectEmployeeProjectStartDate() throws Exception {
        Employee existingEmployee = employee1;

        when(employeeService.getEmployeeById(existingEmployee.getId())).thenReturn(existingEmployee);
        when(employeeService.getEmployeeBookedMonths(anyString(), anyString())).thenReturn(employeeProjectToAdd.getEmployeeBookedMonths());

        when(employeeProjectService.setEmployeeProjectAttributes(any(Employee.class), any(EmployeeProject.class), anyDouble()))
                .thenReturn(employeeProjectToAdd);

        mockMvc.perform(post("/ines/addProjectToEmployee/{id}", existingEmployee.getId())
                        .param("employeeProjectStartDate", "2023-07-02")
                        .param("employeeProjectEndDate", "2023-10-31"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ines/showFormForAddProjectToEmployee/" + existingEmployee.getId()))
                        .andExpect(flash().attributeExists("failed"));

        verify(employeeService, times(1)).getEmployeeById(existingEmployee.getId());
        verify(employeeService, times(1)).getEmployeeBookedMonths("2023-07-02", "2023-10-31");
        verify(employeeProjectService, times(0)).setEmployeeProjectAttributes(any(Employee.class), any(EmployeeProject.class), anyDouble());
        verify(employeeService, times(0)).saveEmployeeAndEmployeeProject(any(Employee.class), any(EmployeeProject.class));
    }

    @Test
    @DisplayName("Post request test fails adding a new EmployeeProject to an existing Employee with validation error for incorrect employeeProjectEndDate")
    public void testAddProjectToEmployee_ValidationFailed_IncorrectEmployeeProjectEndDate() throws Exception {
        Employee existingEmployee = employee1;

        when(employeeService.getEmployeeById(existingEmployee.getId())).thenReturn(existingEmployee);
        when(employeeService.getEmployeeBookedMonths(anyString(), anyString())).thenReturn(employeeProjectToAdd.getEmployeeBookedMonths());

        when(employeeProjectService.setEmployeeProjectAttributes(any(Employee.class), any(EmployeeProject.class), anyDouble()))
                .thenReturn(employeeProjectToAdd);

        mockMvc.perform(post("/ines/addProjectToEmployee/{id}", existingEmployee.getId())
                        .param("employeeProjectStartDate", "2023-07-01")
                        .param("employeeProjectEndDate", "2023-10-30"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ines/showFormForAddProjectToEmployee/" + existingEmployee.getId()))
                .andExpect(flash().attributeExists("failed"));

        verify(employeeService, times(1)).getEmployeeById(existingEmployee.getId());
        verify(employeeService, times(1)).getEmployeeBookedMonths("2023-07-01", "2023-10-30");
        verify(employeeProjectService, times(0)).setEmployeeProjectAttributes(any(Employee.class), any(EmployeeProject.class), anyDouble());
        verify(employeeService, times(0)).saveEmployeeAndEmployeeProject(any(Employee.class), any(EmployeeProject.class));
    }


    @Test
    @DisplayName("Get request showing the form for updating an existing Project")
    void showFormForUpdate() throws Exception {
        when(projectService.getProjectById(1L)).thenReturn(project1);
        when(customProjectDetailsRepository.listAllCustomProjectDetailsByProjectId(anyLong())).thenReturn(customProjectDetailsList);
        mockMvc.perform(get("/ines/showFormForProjectUpdate/" + project1.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("project/update_project"))
                .andExpect(model().attributeExists("project"));

        mockMvc.perform(get("/ines/showFormForProjectUpdate/" + project1.getId()))
                .andExpect(result -> {
                    Map<String, Object> model = Objects.requireNonNull(result.getModelAndView()).getModel();
                    assertTrue(model.containsKey("project"));
                    Object projectObj = model.get("project");
                    assertTrue(projectObj instanceof Project);
                });
    }

    @Test
    @DisplayName("Post request for updating an existing Project")
    void updateProject() {
        long projectId = 1L;
        Project originalProject = project1;
        originalProject.setId(projectId);
        originalProject.setName("Original Project");
        Project updatedProject = new Project();
        updatedProject.setId(projectId);
        updatedProject.setName("Updated Project");

        when(projectService.setUpdatedProjectAttributes(projectId, updatedProject)).thenReturn(updatedProject);
        String result = projectController.updateProject(projectId, updatedProject, redirectAttributes);

        verify(projectService).saveProject(updatedProject);
        verify(redirectAttributes).addFlashAttribute("success", "Updated project: " + updatedProject.getName());
        assertEquals("redirect:/ines/projects", result);
    }

    @Test
    @DisplayName("Get request test showing form for updating employee project")
    public void testShowFormForUpdateEmployeeProject() {
        long employeeProjectId = 1L;

        when(employeeProjectService.getEmployeeProjectById(employeeProjectId)).thenReturn(employeeProject1);
        when(employeeService.getEmployeeById(employeeProject1.getEmployee().getId())).thenReturn(employee1);
        when(employeeProjectRepository.findAllEmployeeProjectByEmployeeId(employee1.getId())).thenReturn(employeeProjects);
        Model model = mock(Model.class);

        String result = projectController.showFormForUpdateEmployeeProject(employeeProjectId, model);

        verify(model).addAttribute("employee", employee1);
        verify(model).addAttribute("employeeProject", employeeProject1);
        verify(model).addAttribute("employeeSavedProject", employeeProjects);
        assertEquals("project/update_employee_project", result);
    }

    @Test
    @DisplayName("Post request test updating an EmployeeProject")
    public void testUpdateEmployeeProject() throws Exception {
        long employeeProjectId = 1L;
        when(employeeService.getEmployeeBookedMonths(employeeProject1.getEmployeeProjectStartDate(),
                                                    employeeProject1.getEmployeeProjectEndDate()))
                .thenReturn(employeeProject1.getEmployeeBookedMonths());

        EmployeeProject expectedUpdatedProject = new EmployeeProject();
        when(employeeProjectService.setUpdatedEmployeeProjectAttributes(
                employeeProjectId, employeeProject1, employeeProject1.getEmployeeBookedMonths()))
                .thenReturn(expectedUpdatedProject);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/ines/updateEmployeeProject/" + employeeProjectId)
                        .flashAttr("employeeProject", employeeProject1)
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/ines/employees?updatedEmployeeId=" + employee1.getId()))
                .andReturn();

        HttpServletRequest request = mvcResult.getRequest();
        FlashMap flashMap = (FlashMap) request.getAttribute(DispatcherServlet.OUTPUT_FLASH_MAP_ATTRIBUTE);
        flashMap.put("updatedEmployeeId", employee1.getId());

        verify(employeeProjectService).setUpdatedEmployeeProjectAttributes(
                employeeProjectId, employeeProject1, employeeProject1.getEmployeeBookedMonths());

        verify(employeeProjectService).saveEmployeeProject(expectedUpdatedProject);
    }

    @Test
    @DisplayName("Post request test updating an EmployeeProject with an incorrect Start Date")
    public void testUpdateEmployeeProject_ValidationFailed_IncorrectEmployeeProjectStartDate() throws Exception {
        this.employeeProject1.setEmployeeProjectStartDate("2023-07-03");

        mockMvc.perform(post("/ines/updateEmployeeProject/1")
                        .flashAttr("employeeProject", this.employeeProject1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ines/showFormForEmployeeProjectUpdate/1"))
                .andExpect(flash().attributeExists("failed"));

        verify(employeeProjectService, never()).saveEmployeeProject(any());
    }

    @Test
    @DisplayName("Post request test updating an EmployeeProject with an incorrect End Date")
    public void testUpdateEmployeeProject_ValidationFailed_IncorrectEmployeeProjectEndDate() throws Exception {
        this.employeeProject1.setEmployeeProjectEndDate("2023-10-30");

        mockMvc.perform(post("/ines/updateEmployeeProject/1")
                        .flashAttr("employeeProject", this.employeeProject1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ines/showFormForEmployeeProjectUpdate/1"))
                .andExpect(flash().attributeExists("failed"));

        verify(employeeProjectService, never()).saveEmployeeProject(any());
    }


    @Test
    @DisplayName("Get Request test deleting a project")
    public void testDeleteProject() throws Exception {
        long projectId = 1L;
        List<EmployeeProject> employeeProjects = new ArrayList<>();
        when(employeeProjectRepository.getAllEmployeeProjectsByProjectId(projectId))
                .thenReturn(employeeProjects);

        when(projectService.getProjectById(projectId))
                .thenReturn(project1);
        when(customProjectDetailsRepository.listAllCustomProjectDetailsByProjectId(anyLong())).thenReturn(customProjectDetailsList);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/ines/deleteProject/" + projectId))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/ines/projects"))
                .andReturn();

        verify(employeeProjectRepository).getAllEmployeeProjectsByProjectId(projectId);
        verify(projectService).getProjectById(projectId);
        verify(projectService).deleteProjectById(projectId);

        HttpServletRequest request = mvcResult.getRequest();
        FlashMap flashMap = (FlashMap) request.getAttribute(DispatcherServlet.OUTPUT_FLASH_MAP_ATTRIBUTE);
        assertNotNull(flashMap);
        assertEquals(project1.getName() + " was successfully deleted.", flashMap.get("success"));

    }

    @Test
    @DisplayName("Test deleteProject fails when employee projects exist")
    public void testDeleteProject_FailsWithStillExistingEmployeeProjects() throws Exception {
        long projectId = 1L;
        List<EmployeeProject> employeeProjects = new ArrayList<>();
        employeeProjects.add(employeeProject1);

        when(employeeProjectRepository.getAllEmployeeProjectsByProjectId(projectId))
                .thenReturn(employeeProjects);

        when(projectService.getProjectById(projectId))
                .thenReturn(project1);

        when(customProjectDetailsRepository.listAllCustomProjectDetailsByProjectId(anyLong())).thenReturn(customProjectDetailsList);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/ines/deleteProject/" + projectId))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/ines/projects"))
                .andReturn();

        verify(employeeProjectRepository).getAllEmployeeProjectsByProjectId(projectId);
        verify(projectService, never()).deleteProjectById(projectId);

        HttpServletRequest request = mvcResult.getRequest();
        FlashMap flashMap = (FlashMap) request.getAttribute(DispatcherServlet.OUTPUT_FLASH_MAP_ATTRIBUTE);
        assertNotNull(flashMap);

        String expectedErrorMessage = "There are still 1 Employees booked to project " +
                employeeProject1.getProject().getName() + ": " + employeeProject1.getEmployee().getName();
        assertEquals(expectedErrorMessage, flashMap.get("failed"));
    }

    @Test
    @DisplayName("Un-archive a project")
    void unarchiveProject() throws Exception {
        when(projectService.getProjectById(anyLong())).thenReturn(this.project1);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/ines/unarchiveProject/" + this.project1.getId()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/ines/projects"))
                .andReturn();

        verify(projectService).getProjectById(this.project1.getId());

        HttpServletRequest request = mvcResult.getRequest();
        FlashMap flashMap = (FlashMap) request.getAttribute(DispatcherServlet.OUTPUT_FLASH_MAP_ATTRIBUTE);
        assertNotNull(flashMap);
        assertEquals(project1.getName() + " was successfully unarchived.", flashMap.get("success"));
    }

    @Test
    @DisplayName("Get request tests the paginated number and sorting field and sorting directory")
    void findProjectPaginated() {
        int pageNo = 1;
        String sortField = "projectNumber";
        String sortDir = "asc";
        int pageSize = 10;

        Page<Project> projectPage = new PageImpl<>(new ArrayList<>()); // Create an empty page
        when(projectService.findPaginatedArchivedFalse(pageNo, pageSize, sortField, sortDir))
                .thenReturn(projectPage);

        Model model = new ExtendedModelMap();
        String result = projectController.findProjectPaginated(pageNo, sortField, sortDir, model);

        verify(projectService).findPaginatedArchivedFalse(pageNo, pageSize, sortField, sortDir);
        assertEquals("project/projects.html", result);
        assertEquals(pageNo, model.getAttribute("currentPage"));
        assertEquals(projectPage.getTotalPages(), model.getAttribute("totalPages"));
        assertEquals(projectPage.getTotalElements(), model.getAttribute("totalItems"));
        assertEquals(sortField, model.getAttribute("sortField"));
        assertEquals(sortDir, model.getAttribute("sortDir"));
        assertEquals("desc", model.getAttribute("reverseSortDir"));
        assertEquals(projectPage.getContent(), model.getAttribute("listProjects"));
    }
}