package com.employee_planning;

import com.employee_planning.model.CustomProjectDetails;
import com.employee_planning.model.Employee;
import com.employee_planning.model.EmployeeProject;
import com.employee_planning.model.Project;
import com.employee_planning.model.chart.ChartEmployee;
import com.employee_planning.model.chart.ChartEmployeeProject;
import com.employee_planning.model.chart.ChartYear;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class EmployeeProjectTestBase {

    protected Employee employee1;
    protected Employee employee2;
    protected Employee employee3;

    protected ChartEmployee chartEmployee1;

    protected Project project1;
    protected Project project2;
    protected Project project3;
    protected Project project4;
    protected Project project5;

    protected ChartEmployeeProject chartEmployeeProject1;
    protected ChartEmployeeProject chartEmployeeProject2;

    protected ChartEmployeeProject chartEmployeeProjectToAdd;
    protected ChartEmployeeProject chartEmployeeProjectToAddOutsideGivenYear;

    protected EmployeeProject employeeProject1;
    protected EmployeeProject employeeProject2;
    protected EmployeeProject employeeProject3;
    protected EmployeeProject employeeProject4;
    protected EmployeeProject employeeProject5;

    protected EmployeeProject employeeProjectToAdd;
    protected EmployeeProject employeeProjectToAddOutsideGivenYear;

    protected List<Employee> employees = new ArrayList<>();
    protected List<Project> projects = new ArrayList<>();
    protected List<EmployeeProject> employeeProjects = new ArrayList<>();
    protected List<ChartEmployee> chartEmployees = new ArrayList<>();
    protected List<ChartEmployeeProject> chartEmployeeProjects = new ArrayList<>();

    protected CustomProjectDetails customProjectDetails1;
    protected CustomProjectDetails customProjectDetails2;

    protected List<CustomProjectDetails> customProjectDetailsList = new ArrayList<>();

    protected ChartYear chartYear;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        employee1 = new Employee(1L, "Test", "Employee1", "Test Employee 1",
                "2021-01-01", "2021-12-31", true);
        employee2 = new Employee(2L, "Test", "Employee2", "Test Employee 2",
                "2022-02-01", "2022-12-31", true);
        employee3 = new Employee(3L, "Test", "Employee3", "Test Employee 3",
                "2023-03-01", "2023-12-31", false);

        project1 = new Project(1L, 111L, "Test Project 1", "2021-01-01", "2021-12-31",
                12, 12, 0, 2, "Test Comment 1", false);

        project2 = new Project(2L, 222L, "Test Project 2", "2022-01-01", "2022-12-31",
                12, 9, 3, 3, "Test Comment 2", false);

        project3 = new Project(3L, 333L, "Test Project 3", "2023-01-01", "2023-12-31",
                12, 6, 6, 4, "Test Comment 3", false);

        project4 = new Project(4L, 444L, "Test Project 4", "2023-06-01", "2023-12-31",
                6, 3, 6, 2, "Test Comment 4", false);

        project5 = new Project(5L, 555L, "Test Project 5", "2022-06-01", "2024-01-01",
                20, 0, 20, 0, "Test Comment 5", false);

        employeeProject1 = new EmployeeProject(1L, employee1, project1, 6.0,
                "2021-01-01", "2021-12-31", project1.getName(),
                true, 100);
        employeeProject2 = new EmployeeProject(2L, employee2, project1, 9.0,
                "2022-01-01", "2022-12-31", project1.getName(),
                false, 100);
        employeeProject3 = new EmployeeProject(3L, employee3, project1, 12.0,
                "2023-01-01", "2023-12-31", project1.getName(),
                true, 50);

        employeeProject4 = new EmployeeProject(4L, employee1, project2, 9.0,
                "2022-04-01", "2022-12-31", project2.getName(),
                false, 100);
        employeeProject5 = new EmployeeProject(5L, employee1, project3, 12.0,
                "2023-05-01", "2023-12-31", project3.getName(),
                true, 50);

        employeeProjectToAdd = new EmployeeProject(6L, employee1, project4, 4.0,
                "2023-07-01", "2023-10-31", project4.getName(),
                true, 50);

        employeeProjectToAddOutsideGivenYear = new EmployeeProject(7L, employee1, project4, 14.0,
                "2022-07-01", "2024-10-31", project4.getName(),
                true, 100);

        chartEmployeeProject1 = new ChartEmployeeProject(3L, employeeProject3.getId(), employeeProject3.getProject().getName(),
                employeeProject3.getEmployeeBookedMonths(), employeeProject3.getConfirmBooking(),
                employeeProject3.getEmployeeProjectStartDate(), employeeProject3.getPercentage());

        chartEmployeeProject2 = new ChartEmployeeProject(5L, employeeProject5.getId(), employeeProject5.getProject().getName(),
                employeeProject5.getEmployeeBookedMonths(), employeeProject5.getConfirmBooking(),
                employeeProject5.getEmployeeProjectStartDate(), employeeProject5.getPercentage());

        chartEmployeeProjectToAdd = new ChartEmployeeProject(7L, employeeProjectToAdd.getId(), employeeProjectToAdd.getProject().getName(),
                employeeProjectToAdd.getEmployeeBookedMonths(), employeeProjectToAdd.getConfirmBooking(),
                employeeProjectToAdd.getEmployeeProjectStartDate(), employeeProjectToAdd.getPercentage());

        chartEmployeeProjectToAddOutsideGivenYear = new ChartEmployeeProject(8L, employeeProjectToAddOutsideGivenYear.getId(),
                employeeProjectToAddOutsideGivenYear.getProject().getName(),
                employeeProjectToAddOutsideGivenYear.getEmployeeBookedMonths(), employeeProjectToAddOutsideGivenYear.getConfirmBooking(),
                employeeProjectToAddOutsideGivenYear.getEmployeeProjectStartDate(), employeeProjectToAddOutsideGivenYear.getPercentage());

        customProjectDetails1 = new CustomProjectDetails(1L, project1, "2023", 10.0);
        customProjectDetails2 = new CustomProjectDetails(2L, project2, "2024", 11.0);

        customProjectDetailsList.add(customProjectDetails1);
        customProjectDetailsList.add(customProjectDetails2);

        chartYear = new ChartYear("2023");

        chartEmployeeProjects.add(chartEmployeeProject1);
        chartEmployeeProjects.add(chartEmployeeProject2);

        chartEmployee1 = new ChartEmployee(1L, "Chart Employee 1", chartEmployeeProjects);
        chartEmployees.add(chartEmployee1);

        employee1.addEmployeeProject(employeeProject1);
        employee1.addEmployeeProject(employeeProject4);
        employee1.addEmployeeProject(employeeProject5);

        employee2.addEmployeeProject(employeeProject2);
        employee2.addEmployeeProject(employeeProject3);
        employee2.addEmployeeProject(employeeProjectToAddOutsideGivenYear);

        employees.add(employee1);
        employees.add(employee2);
        employees.add(employee3);

        projects.add(project1);
        projects.add(project2);
        projects.add(project3);
        projects.add(project4);

        employeeProjects.add(employeeProject1);
        employeeProjects.add(employeeProject2);
        employeeProjects.add(employeeProject3);
        employeeProjects.add(employeeProject4);
        employeeProjects.add(employeeProject5);

    }
}
