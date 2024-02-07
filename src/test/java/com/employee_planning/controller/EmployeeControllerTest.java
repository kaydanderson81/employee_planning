package com.employee_planning.controller;

import com.employee_planning.EmployeeProjectTestBase;
import com.employee_planning.model.Employee;
import com.employee_planning.model.EmployeeProject;
import com.employee_planning.model.Project;
import com.employee_planning.model.chart.ChartEmployee;
import com.employee_planning.model.chart.ChartYear;
import com.employee_planning.repository.EmployeeRepository;
import com.employee_planning.service.chart.ChartServiceImpl;
import com.employee_planning.service.employee.EmployeeService;
import com.employee_planning.service.employeeProject.EmployeeProjectService;
import com.employee_planning.service.project.ProjectService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class EmployeeControllerTest extends EmployeeProjectTestBase {

    @MockBean
    private EmployeeRepository employeeRepository;
    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private ChartServiceImpl chartService;

    @MockBean
    private EmployeeProjectService employeeProjectService;

    @InjectMocks
    private EmployeeController employeeController;


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ChartServiceImpl chartServiceImpl;

    @Test
    @DisplayName("Get request to display the employees page with a list of Employee objects")
    void viewEmployeesPage() throws Exception {
        List<Employee> employeeList = employees;
        when(employeeRepository.findAllEmployeesWhereArchivedIsFalse()).thenReturn(employeeList);
        doNothing().when(projectService).updateEmployeeProjectBookedMonths();
        mockMvc.perform(MockMvcRequestBuilders.get("/ines/employees"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("listEmployees"))
                .andExpect(MockMvcResultMatchers.model().attribute("listEmployees", employeeList));

    }

    @Test
    @DisplayName("Get request to display the employees page with a list of Employee objects with archived set to true")
    void viewEmployeeArchivedPage() throws Exception {
        List<Employee> employeeList = employees;
        for (Employee employee : employeeList) {
            employee.setArchived(true);
        }
        when(employeeRepository.findAllEmployeesWhereArchivedIsTrue()).thenReturn(employeeList);
        doNothing().when(projectService).updateEmployeeProjectBookedMonths();
        mockMvc.perform(MockMvcRequestBuilders.get("/ines/archivedEmployees"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("listEmployees"))
                .andExpect(MockMvcResultMatchers.model().attribute("listEmployees", employeeList));
    }

    @Test
    @DisplayName("Get request showing the form for creating a new Employee, with list of current projects and a new EmployeeProject object")
    void showNewEmployeeForm() throws Exception {
        when(projectService.getAllProjects()).thenReturn(projects);
        mockMvc.perform(MockMvcRequestBuilders.get("/ines/showNewEmployeeForm"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("new_employee"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("employee"))
                .andExpect(MockMvcResultMatchers.model().attribute("projects", projects))
                .andExpect(MockMvcResultMatchers.model().attributeExists("employeeProjects"))
                .andExpect(MockMvcResultMatchers.model().attribute("employeeProjects", new EmployeeProject()));
        verify(projectService, times(1)).getAllProjects();
    }

    @Test
    @DisplayName("Post request to save a new Employee object with a new EmployeeProject object")
    void testSaveEmployee_Success() throws Exception {
        when(employeeProjectService.getEmployeeBookedMonthsByPercentage(anyInt(), anyDouble())).thenReturn(employeeProject1.getEmployeeBookedMonths());
        when(employeeService.getEmployeeBookedMonths(anyString(), anyString())).thenReturn(10.0);
        when(employeeRepository.existsByName(anyString())).thenReturn(false);

        mockMvc.perform(post("/ines/saveEmployee")
                                .param("employeeProjectStartDate", "2023-07-01")
                                .param("employeeProjectEndDate", "2023-08-31"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ines/employees"));

        verify(employeeProjectService, times(1)).getEmployeeBookedMonthsByPercentage(0, 10.0);
        verify(employeeService, times(1)).getEmployeeBookedMonths("2023-07-01", "2023-08-31");
        verify(employeeService, times(1)).saveEmployeeAndEmployeeProject(any(Employee.class), any(EmployeeProject.class));
    }

    @Test
    @DisplayName("Post request validation fail with incorrect date on a new Employee object with a new EmployeeProject object")
    void testSaveEmployee_ValidationFailed_IncorrectDate() throws Exception {
        when(employeeProjectService.getEmployeeBookedMonthsByPercentage(anyInt(), anyDouble())).thenReturn(5.0);
        when(employeeService.getEmployeeBookedMonths(anyString(), anyString())).thenReturn(10.0);
        when(employeeRepository.existsByName(anyString())).thenReturn(false);

        mockMvc.perform(post("/ines/saveEmployee")
                                .param("employeeProjectStartDate", "2023-07-20")
                                .param("employeeProjectEndDate", "2023-06-20"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ines/showNewEmployeeForm"))
                .andExpect(flash().attributeExists("failed"));

        verify(employeeProjectService, times(0)).getEmployeeBookedMonthsByPercentage(anyInt(), anyDouble());
        verify(employeeService, times(0)).getEmployeeBookedMonths(anyString(), anyString());
        verify(employeeRepository, times(0)).existsByName(anyString());
    }

//    @Test
//    @DisplayName("Post request fail with Employee object name already exists")
//    public void testSaveEmployee_Fail_EmployeeNameExists() throws Exception {
//        this.employee1.setName("Test", "Employee 1");
//        when(employeeRepository.existsByName(Mockito.anyString())).thenReturn(true);
//
//        mockMvc.perform(post("/ines/saveEmployee")
//                        .param("firstName", this.employee1.getFirstName())
//                        .param("lastName", this.employee1.getLastName())
//                        .param("name", this.employee1.getLastName())
//                        .param("employeeProjects.employeeProjectStartDate", "2023-07-01")
//                        .param("employeeProjects.employeeProjectEndDate", "2023-08-31"))
//                .andExpect(MockMvcResultMatchers.flash().attributeExists("failed"))
//                .andExpect(MockMvcResultMatchers.flash().attribute("failed", "Employee with name: Existing Employee already exists, please try another name."))
//                .andExpect(MockMvcResultMatchers.redirectedUrl("/ines/showNewEmployeeForm"));
//
//        verify(employeeService, never()).saveEmployee(any());
//    }

    @Test
    @DisplayName("Get request showing the form for updating an existing Employee")
    void showFormForUpdate() {
        long employeeId = 1L;
        when(employeeService.getEmployeeById(anyLong())).thenReturn(employee1);
        Model model = mock(Model.class);
        String viewName = employeeController.showFormForUpdate(employeeId, model);

        verify(employeeService, times(1)).getEmployeeById(employeeId);
        verify(model, times(1)).addAttribute("employee", employee1);
        assertEquals("update_employee", viewName);
    }

    @Test
    @DisplayName("Get request showing the form for updating an existing Employee and associated EmployeeProject " +
            "object, then redirect to that employee accordion in the list")
    void updateEmployeeWithProjects() throws Exception {
        long employeeId = 1L;
        when(employeeService.setUpdatedEmployeeAttributes(anyLong(), any())).thenReturn(employee2);
        when(employeeService.saveEmployee(any())).thenReturn(employee2);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/ines/updateEmployee/" + employeeId)
                        .flashAttr("employee", employee1)
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/ines/employees?updatedEmployeeId=" + employee2.getId()))
                .andReturn();

        HttpServletRequest request = mvcResult.getRequest();
        FlashMap flashMap = (FlashMap) request.getAttribute(DispatcherServlet.OUTPUT_FLASH_MAP_ATTRIBUTE);
        flashMap.put("updatedEmployeeId", employee2.getId());
    }

    @Test
    @DisplayName("Get request for deleting an Employee and all associated EmployeeProject objects")
    void deleteEmployee() throws Exception {
        long employeeId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.get("/ines/deleteEmployee/" + employeeId))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/ines/employees"));
        verify(employeeService, times(1)).deleteEmployeeById(employeeId);
    }

    @Test
    @DisplayName("Get request for deleting an Employee and all associated EmployeeProject objects, then redirect to " +
            "that employee accordion in the list")
    void deleteEmployeeProject() throws Exception{
        long employeeId = 1L;
        when(employeeProjectService.getEmployeeProjectById(anyLong())).thenReturn(employeeProject1);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/ines/deleteEmployeeProject/" + employeeId)
                        .flashAttr("employee", employee1))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/ines/employees?updatedEmployeeId=" + employeeProject1.getId()))
                .andReturn();

        HttpServletRequest request = mvcResult.getRequest();
        FlashMap flashMap = (FlashMap) request.getAttribute(DispatcherServlet.OUTPUT_FLASH_MAP_ATTRIBUTE);
        flashMap.put("updatedEmployeeId", employeeProject1.getId());
    }

    @Test
    @DisplayName("Get request un-archiving an employee")
    void unarchiveEmployee() throws Exception {
        long employeeId = 1L;
        when(employeeService.getEmployeeById(anyLong())).thenReturn(employee1);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/ines/unarchiveEmployee/" + employeeId)
                        .flashAttr("employee", employee1))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/ines/employees"))
                .andReturn();

        HttpServletRequest request = mvcResult.getRequest();
        FlashMap flashMap = (FlashMap) request.getAttribute(DispatcherServlet.OUTPUT_FLASH_MAP_ATTRIBUTE);
        flashMap.put("success", employeeProject1.getId());
    }


    @Test
    @DisplayName("Get request showing the chart page")
    void viewChartPage() {
        Model model = new ExtendedModelMap();
        String selectedYear = "2023";

        List<ChartEmployee> chartEmployees = new ArrayList<>();
        List<String> yearList = Arrays.asList("2021", "2022", "2023");
        List<Project> projects = new ArrayList<>();

        when(chartService.getAllEmployeesByEmployeeProjectStartDate(any(ChartYear.class)))
                .thenReturn(chartEmployees);

        when(employeeProjectService.findAllStartAndEndDatesByYear())
                .thenReturn(yearList);

        when(projectService.getAListOfProjectsAndTheirPersonMonthsByYear(any(ChartYear.class)))
                .thenReturn(projects);

        String result = employeeController.viewChartPage(selectedYear, model);

        verify(chartService).getAllEmployeesByEmployeeProjectStartDate(any(ChartYear.class));
        verify(employeeProjectService).findAllStartAndEndDatesByYear();
        verify(projectService).getAListOfProjectsAndTheirPersonMonthsByYear(any(ChartYear.class));
        assertEquals("chart.html", result);
    }

    @Test
    @DisplayName("Get request showing the chart page if year is empty")
    void viewChartPage_ifYearIsEmpty() {
        Model model = new ExtendedModelMap();

        List<ChartEmployee> chartEmployees = new ArrayList<>();
        List<String> yearList = Arrays.asList("2021", "2022", "2023");
        List<Project> projects = new ArrayList<>();

        when(chartService.getAllEmployeesByEmployeeProjectStartDate(any(ChartYear.class)))
                .thenReturn(chartEmployees);

        when(employeeProjectService.findAllStartAndEndDatesByYear())
                .thenReturn(yearList);

        when(projectService.getAListOfProjectsAndTheirPersonMonthsByYear(any(ChartYear.class)))
                .thenReturn(projects);

        String result = employeeController.viewChartPage(null, model);

        verify(chartService).getAllEmployeesByEmployeeProjectStartDate(any(ChartYear.class));
        verify(employeeProjectService).findAllStartAndEndDatesByYear();
        verify(projectService).getAListOfProjectsAndTheirPersonMonthsByYear(any(ChartYear.class));
        assertEquals("chart.html", result);
    }
}