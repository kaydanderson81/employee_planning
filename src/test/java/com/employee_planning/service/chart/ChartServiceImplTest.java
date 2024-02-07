package com.employee_planning.service.chart;

import com.employee_planning.EmployeeProjectTestBase;
import com.employee_planning.model.Employee;
import com.employee_planning.model.EmployeeProject;
import com.employee_planning.model.Project;
import com.employee_planning.model.chart.ChartEmployee;
import com.employee_planning.model.chart.ChartEmployeeProject;
import com.employee_planning.model.chart.ChartYear;
import com.employee_planning.repository.EmployeeRepository;
import com.employee_planning.repository.ProjectRepository;
import com.employee_planning.service.employeeProject.EmployeeProjectService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
class ChartServiceImplTest extends EmployeeProjectTestBase {

    @Autowired
    private ChartService chartService;

    @InjectMocks
    private ChartServiceImpl chartServiceImpl;

    @Mock
    private EmployeeProjectService employeeProjectService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ProjectRepository projectRepository;



    @Test
    void testFindAllChartEmployeesFromFindAllEmployees() {
        List<ChartEmployee> expectedChartEmployees = new ArrayList<>();
        for (Employee employee : this.employees) {
            ChartEmployee chartEmployee = new ChartEmployee();
            chartEmployee.setId(employee.getId());
            chartEmployee.setName(employee.getName());
            List<ChartEmployeeProject> chartEmployeeProjects = new ArrayList<>();
            long index = 1L;
            assert employee.getEmployeeProjects() != null;
            for (EmployeeProject employeeProject : employee.getEmployeeProjects()) {
                ChartEmployeeProject chartEmployeeProject = new ChartEmployeeProject();
                chartEmployeeProject.setId(index);
                chartEmployeeProject.setProjectId(employeeProject.getProject().getId());
                chartEmployeeProject.setProjectName(employeeProject.getEmployeeProjectName());
                chartEmployeeProject.setEmployeeBookedMonths(employeeProject.getEmployeeBookedMonths());
                chartEmployeeProject.setConfirmBooking(employeeProject.getConfirmBooking());
                chartEmployeeProject.setStartDate(employeeProject.getEmployeeProjectStartDate());
                chartEmployeeProjects.add(chartEmployeeProject);
                index += 1;
            }
            chartEmployee.setChartEmployeeProjects(chartEmployeeProjects);
            expectedChartEmployees.add(chartEmployee);
        }

        List<ChartEmployee> result = this.chartServiceImpl.findAllChartEmployeesFromFindAllEmployees(this.employees);

        assertThat(expectedChartEmployees.size(), is(equalTo(result.size())));
        assertThat(expectedChartEmployees.get(0).getChartEmployeeProjects().size(),
                is(equalTo(result.get(0).getChartEmployeeProjects().size())));
    }



    @Test
    void getAllEmployeesByEmployeeProjectStartDate() {


        List<ChartEmployee> result = this.chartService.findAllChartEmployeesFromFindAllEmployees(employees);
        List<ChartEmployeeProject> chartEmployeeProjectList = result.get(1).getChartEmployeeProjects();
        chartEmployeeProjectList.add(this.chartEmployeeProjectToAddOutsideGivenYear);

        result.get(1).setChartEmployeeProjects(chartEmployeeProjectList);

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals("Test Employee 1", result.get(0).getName());

        ChartEmployeeProject project1Result = result.get(0).getChartEmployeeProjects().get(0);
        Assertions.assertEquals("2021-01-01", project1Result.getStartDate());
        Assertions.assertEquals("Test Project 1", project1Result.getProjectName());

        ChartEmployeeProject project2Result = result.get(1).getChartEmployeeProjects().get(0);
        Assertions.assertEquals("2022-01-01", project2Result.getStartDate());
        Assertions.assertEquals("Test Project 1", project2Result.getProjectName());
    }

    @Test
    void setEmployeeProjectMonthsByPercentage() {
        when(this.employeeProjectService.getEmployeeBookedMonthsByPercentage(50, 12.0)).thenReturn(6.0);

        List<EmployeeProject> result = this.chartServiceImpl.setEmployeeProjectMonthsByPercentage(this.employeeProjects);
        System.out.println(result);
        assertEquals(5, result.size());
        assertEquals(6.0, result.get(2).getEmployeeBookedMonths());
        assertEquals(9.0, result.get(1).getEmployeeBookedMonths());
    }

    @Test
    void removeAllEmployeeProjectsOutsideTheGivenYear() {
        when(this.employeeRepository.findAll()).thenReturn(this.employees);
        ChartYear year = new ChartYear("2023");
        List<Employee> result = this.chartServiceImpl.removeAllEmployeeProjectsOutsideTheGivenYear(year);
        assertEquals(2, result.size());
    }

    @Test
    void removeAllProjectsOutsideTheGivenYear() {
        when(this.projectRepository.findAll()).thenReturn(this.projects);
        this.projects.add(project5);
        ChartYear year = new ChartYear("2023");
        List<Project> result = this.chartServiceImpl.removeAllProjectsOutsideTheGivenYear(year);
        assertEquals(3, result.size());
    }
}