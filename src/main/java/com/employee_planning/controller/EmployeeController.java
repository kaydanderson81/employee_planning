package com.employee_planning.controller;


import com.employee_planning.model.Employee;
import com.employee_planning.model.EmployeeProject;
import com.employee_planning.model.Project;
import com.employee_planning.model.chart.ChartEmployee;
import com.employee_planning.model.chart.ChartYear;
import com.employee_planning.project.ProjectWarning;
import com.employee_planning.repository.EmployeeProjectRepository;
import com.employee_planning.repository.EmployeeRepository;
import com.employee_planning.repository.ProjectRepository;
import com.employee_planning.service.chart.ChartService;
import com.employee_planning.service.employee.EmployeeService;
import com.employee_planning.service.employeeProject.EmployeeProjectService;
import com.employee_planning.service.project.CustomProjectDetailsServiceImpl;
import com.employee_planning.service.project.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Year;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/ines")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeProjectRepository employeeProjectRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeProjectService employeeProjectService;

    @Autowired
    private CustomProjectDetailsServiceImpl customProjectDetailsServiceImpl;

    @Autowired
    private ChartService chartService;

    public EmployeeController() {
    }

    /**
     * Displays the list of all employees.
     *
     * @param model the model object used to pass data to the view
     * @return "employee.html" list of all employees
     */
    @GetMapping("/employees")
    public String viewEmployeesPage(Model model) {
        List<Employee> employees = employeeRepository.findAllEmployeesWhereArchivedIsFalse();
        projectService.updateEmployeeProjectBookedMonths();
        employees.sort(Comparator.comparing(Employee::getLastName));
        model.addAttribute("listEmployees", employees);
        return "employees.html";
    }

    /**
     * Displays the list of all archived employees.
     *
     * @param model the model object used to pass data to the view
     * @return "employee_archived.html" list of all employees
     */
    @GetMapping("/archivedEmployees")
    public String viewEmployeeArchivedPage(Model model) {
        List<Employee> employees = employeeRepository.findAllEmployeesWhereArchivedIsTrue();
        projectService.updateEmployeeProjectBookedMonths();
        employees.sort(Comparator.comparing(Employee::getLastName));
        model.addAttribute("listEmployees", employees);
        return "employees_archived.html";
    }

    /**
     * Displays the form for creating a new employee.
     *
     * @param model the model object used to pass data to the view
     * @return "new_employee.html" form for a new employee
     */
    @GetMapping("/showNewEmployeeForm")
    public String showNewEmployeeForm(Model model) {
        Employee employee = new Employee();
        List<Project> projects = projectService.getAllProjects();
        projects.sort(Comparator.comparing(Project::getName));
        model.addAttribute("employee", employee);
        model.addAttribute("projects", projects);
        model.addAttribute("employeeProjects", new EmployeeProject());
        return "new_employee";
    }



    /**
     * Post method for saving a new employee with an associated employeeProject object.
     *
     * @param employee the employee to be saved
     * @param employeeProjects the employeeProject object to be saved
     * @param redirectAttributes the object passed when redirecting
     * @return "redirect:/ines/employees" with the added new employee and the associated employeeProject
     */
    @PostMapping("/saveEmployee")
    public String saveEmployee(@ModelAttribute("employee") Employee employee,
                               @ModelAttribute EmployeeProject employeeProjects,
                               RedirectAttributes redirectAttributes) {

        String validationError = ProjectWarning.validateEmployeeProject(employeeProjects);
        if (!validationError.isEmpty()) {
            redirectAttributes.addFlashAttribute("failed", validationError);
            return "redirect:/ines/showNewEmployeeForm";
        }
        if (employeeRepository.existsByName(employee.getName())) {
            redirectAttributes.addFlashAttribute("failed", "Employee with name: " + employee.getName() +
                    " already exists, please try another name.");
            return "redirect:/ines/showNewEmployeeForm";
        }

        double bookedMonths =
                employeeService.getEmployeeBookedMonths(employeeProjects.getEmployeeProjectStartDate(),
                        employeeProjects.getEmployeeProjectEndDate());
        employeeProjects.setEmployeeBookedMonths(bookedMonths);

        if (employeeProjects.getPercentage() != 100) {
            employeeProjects.setEmployeeBookedMonths(
                    employeeProjectService.getEmployeeBookedMonthsByPercentage(employeeProjects.getPercentage(),
                            employeeProjects.getEmployeeBookedMonths()));
        }

        employeeService.saveEmployeeAndEmployeeProject(employee, employeeProjects);
        redirectAttributes.addAttribute("updatedEmployeeId", employee.getId());
        return "redirect:/ines/employees";
    }

    /**
     * Displays the form for updating an existing employee.
     *
     * @param id the employee id of the employee being updated
     * @param model the model object used to pass data to the view
     * @return "update_employee.html" form for updating an employee
     */
    @GetMapping("/showFormForEmployeeUpdate/{id}")
    public String showFormForUpdate(@PathVariable( value = "id") long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employee);
        return "update_employee";
    }

    /**
     * Post method for updating an existing employee object by id.
     *
     * @param id the id of the updated employee
     * @param employee the new employee info from the form
     * @param redirectAttributes the object passed when redirecting
     * @return "redirect:/ines/employees" with the added updated employee
     */
    @PostMapping("/updateEmployee/{id}")
    public String updateEmployeeWithProjects(@PathVariable( value = "id") long id,
                                             @ModelAttribute("employee") Employee employee, RedirectAttributes redirectAttributes) {
        Employee updatedEmployee = employeeService.setUpdatedEmployeeAttributes(id, employee);
        employeeService.saveEmployee(updatedEmployee);
        redirectAttributes.addAttribute("updatedEmployeeId", updatedEmployee.getId());
        return "redirect:/ines/employees";
    }

    /**
     * Method for deleting an employee by its id, this will also delete any associated employeeProject objects.
     *
     * @param id the employee id of the employee being deleted
     * @return "redirect:/ines/employees" where the employee is now deleted
     */
    @GetMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable (value = "id") long id) {
        this.employeeService.deleteEmployeeById(id);
        return "redirect:/ines/employees";
    }

    /**
     * Method for setting archived to false by employee id.
     *
     * @param id the Employee id of the employee archive status being changed
     * @param redirectAttributes the object passed when redirecting
     * @return "redirect:/ines/employees" where the Employee archive status is now changed
     */
    @GetMapping("/unarchiveEmployee/{employeeId}")
    public String unarchiveEmployee(@PathVariable(value = "employeeId") long id,  RedirectAttributes redirectAttributes) {
        String employee = employeeService.getEmployeeById(id).getName();
        this.employeeService.unarchiveEmployeeById(id);
        redirectAttributes.addFlashAttribute("success",  employee + " was successfully unarchived.");
        return "redirect:/ines/employees";
    }

    /**
     * Method for deleting an employeeProject by its id, from an existing employee.
     *
     * @param id the employeeProject id of the employeeProject being deleted
     * @return "redirect:/ines/employees" where the employeeProject is now deleted from the employee
     */
    @GetMapping("/deleteEmployeeProject/{id}")
    public String deleteEmployeeProject(@PathVariable (value = "id") long id, RedirectAttributes redirectAttributes) {
        EmployeeProject employeeProject = employeeProjectService.getEmployeeProjectById(id);
        this.employeeProjectService.deleteEmployeeProjectById(id);
        redirectAttributes.addAttribute("updatedEmployeeId", employeeProject.getEmployee().getId());
        return "redirect:/ines/employees";
    }

    /**
     * Displays 2 different charts for both employees and for projects.
     *
     * @param selectedYear the year of the required chart data
     * @param model the model object used to pass data to the view
     * @return "chart.html" page
     */
    @GetMapping("/chart")
    public String viewChartPage(@RequestParam(value = "year", required = false) String selectedYear, Model model) {
        ChartYear year;
        if (selectedYear != null && !selectedYear.isEmpty()) {
            year = new ChartYear(selectedYear);
        } else {
            year = new ChartYear(String.valueOf(Year.now()));
        }

        List<ChartEmployee> chartEmployees = chartService.getAllEmployeesByEmployeeProjectStartDate(year);

        List<String> yearList = employeeProjectService.findAllStartAndEndDatesByYear();
        List<Project> projects = projectService.getAListOfProjectsAndTheirPersonMonthsByYear(year);

        model.addAttribute("employees", chartEmployees);
        model.addAttribute("projects", projects);
        model.addAttribute("yearList", yearList);
        model.addAttribute("currentYear", year.getYear());
        return "chart.html";
    }
}
