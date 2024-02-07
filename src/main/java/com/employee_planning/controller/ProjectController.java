package com.employee_planning.controller;

import com.employee_planning.model.Employee;
import com.employee_planning.model.EmployeeProject;
import com.employee_planning.model.Project;
import com.employee_planning.model.CustomProjectDetails;
import com.employee_planning.project.ProjectWarning;
import com.employee_planning.repository.EmployeeProjectRepository;
import com.employee_planning.repository.ProjectRepository;
import com.employee_planning.service.employee.EmployeeService;
import com.employee_planning.service.employeeProject.EmployeeProjectService;
import com.employee_planning.service.project.CustomProjectDetailsService;
import com.employee_planning.service.project.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/ines")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CustomProjectDetailsService customProjectDetailsService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeProjectService employeeProjectService;

    @Autowired
    private EmployeeProjectRepository employeeProjectRepository;

    /**
     * Displays the list of all projects.
     *
     * @param model the model object used to pass data to the view
     * @return paginated list of all projects
     */
    @GetMapping("/projects")
    public String viewProjectsPage(Model model) {
        return findProjectPaginated(1, "name", "asc", model);
    }

    /**
     * Displays the list of all archived projects.
     *
     * @param model the model object used to pass data to the view
     * @return paginated list of all archived projects
     */
    @GetMapping("/archivedProjects")
    public String viewArchivedProjectsPage(Model model) {
        return findArchivedProjectPaginated(1, "name", "asc", model);
    }

    /**
     * Displays the form for creating a new project.
     *
     * @param model the model object used to pass data to the view
     * @return "new_project.html" form for a new project
     */
    @GetMapping("/showNewProjectForm")
    public String showNewProjectForm(Model model) {
        Project project = new Project();
        model.addAttribute("project", project);
        return "project/new_project.html";
    }

    /**
     * Post method for saving a new project.
     *
     * @param project the project to be saved
     * @param redirectAttributes the object passed when redirecting
     * @return "redirect:/ines/projects" with the added new project
     */
    @PostMapping("/saveProject")
    public String saveProject(@ModelAttribute("project") Project project, RedirectAttributes redirectAttributes) {
        if (projectRepository.existsByName(project.getName())) {
            redirectAttributes.addFlashAttribute("failed", "Project name: " + project.getName() + " already exists");
            return "redirect:/ines/showNewProjectForm";
        }
        if (projectRepository.existsByProjectNumber(project.getProjectNumber())) {
            redirectAttributes.addFlashAttribute("failed", "Project with number: " + project.getProjectNumber() + " already exists");
            return "redirect:/ines/showNewProjectForm";
        }
        if (project.getStartDate().isEmpty()) {
            project.setStartDate("Date Not Set");
        }
        if (project.getEndDate().isEmpty()) {
            project.setEndDate("Date Not Set");
        }
        projectService.saveProject(project);
        redirectAttributes.addFlashAttribute("success", "Added project: " + project.getName() + " to list");
        return "redirect:/ines/projects";
    }

    /**
     * Displays the form for adding a project to an existing employee.
     *
     * @param model the model object used to pass data to the view
     * @param id the id of the existing employee
     * @return "add_Project_To_Employee.html" form for a new employeeProject
     */
    @GetMapping("/showFormForAddProjectToEmployee/{id}")
    public String showFormForAddProjectToEmployee(@PathVariable(value = "id") long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id);
        List<Project> projects = projectService.getAllProjects();
        List<EmployeeProject> findEmployeeProjects = employeeProjectRepository.findAllEmployeeProjectByEmployeeId(id);

        model.addAttribute("employee", employee);
        model.addAttribute("projects", projects);
        model.addAttribute("employeeProject", new EmployeeProject());
        model.addAttribute("employeeSavedProject", findEmployeeProjects);
        return "project/add_Project_To_Employee";
    }

    /**
     * Post method for saving a new employeeProject object to an existing Employee. An employeeProject is the link
     * between an Employee and a project.
     *
     * @param employee the existing employee
     * @param employeeProject the employeeProject object associated with the existing employee
     * @param redirectAttributes the object passed when redirecting
     * @return "redirect:/ines/employees" with the added new employeeProject associated to existing employee
     */
    @PostMapping("/addProjectToEmployee/{id}")
    public String addProjectToEmployee(@PathVariable(value = "id") long id,
                                       @ModelAttribute("employee") Employee employee,
                                       @ModelAttribute EmployeeProject employeeProject,
                                       RedirectAttributes redirectAttributes) {

        String validationError = ProjectWarning.validateEmployeeProject(employeeProject);
        Employee updateEmployee = employeeService.getEmployeeById(id);
        double employeeBookedMonths = employeeService.getEmployeeBookedMonths(employeeProject.getEmployeeProjectStartDate(),
                employeeProject.getEmployeeProjectEndDate());

        if (!validationError.isEmpty()) {
            redirectAttributes.addFlashAttribute("failed", validationError);
            return "redirect:/ines/showFormForAddProjectToEmployee/" + id;
        }

        EmployeeProject newEmployeeProject =
                employeeProjectService.setEmployeeProjectAttributes(updateEmployee, employeeProject, employeeBookedMonths);

        employeeService.saveEmployeeAndEmployeeProject(updateEmployee, newEmployeeProject);
        redirectAttributes.addAttribute("updatedEmployeeId", employee.getId());
        return "redirect:/ines/employees";
    }

    /**
     * Displays the form for updating a project.
     *
     * @param id the id of the project to update
     * @param model the model object used to pass data to the view
     * @return "update_project.html" form for updating a project
     */
    @GetMapping("/showFormForProjectUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") long id, Model model) {
        Project project = projectService.getProjectById(id);
        List<CustomProjectDetails> customProjectDetailsList = customProjectDetailsService.getAllCustomProjectDetailsByProjectId(id);
        model.addAttribute("project", project);
        model.addAttribute("customProjectList", customProjectDetailsList);
        return "project/update_project";
    }

    /**
     * Post method for updating an existing project.
     *
     * @param id the project id
     * @param project the project object from the update_project form
     * @param redirectAttributes the object passed when redirecting
     * @return "redirect:/ines/projects" with the updated project
     */
    @PostMapping("/updateProject/{id}")
    public String updateProject(@PathVariable(value = "id") long id,
                                @ModelAttribute("project") Project project,
                                RedirectAttributes redirectAttributes) {
        Project projectToUpdate = projectService.setUpdatedProjectAttributes(id, project);
        projectService.saveProject(projectToUpdate);
        redirectAttributes.addFlashAttribute("success", "Updated project: " + project.getName());
        return "redirect:/ines/projects";
    }

    /**
     * Displays the form for updating an employeeProject.
     *
     * @param id the id of the employeeProject to update
     * @param model the model object used to pass data to the view
     * @return "update_employee_project.html" form for updating an existing employeeProject
     */
    @GetMapping("/showFormForEmployeeProjectUpdate/{id}")
    public String showFormForUpdateEmployeeProject(@PathVariable(value = "id") long id, Model model) {
        EmployeeProject employeeProject = employeeProjectService.getEmployeeProjectById(id);
        Employee employee = employeeService.getEmployeeById(employeeProject.getEmployee().getId());
        List<EmployeeProject> findEmployeeProjects = employeeProjectRepository.findAllEmployeeProjectByEmployeeId(employee.getId());
        model.addAttribute("employee", employee);
        model.addAttribute("employeeProject", employeeProject);
        model.addAttribute("employeeSavedProject", findEmployeeProjects);
        return "project/update_employee_project";
    }

    /**
     * Post method for updating an existing employeeProject.
     *
     * @param id the project id
     * @param employeeProjects the employeeProject object from the update_employee_project form
     * @param redirectAttributes the object passed when redirecting
     * @return "redirect:/ines/employees" with the updated employeeProject
     */
    @PostMapping("/updateEmployeeProject/{id}")
    public String updateEmployeeProject(@PathVariable(value = "id") long id,
                                        @ModelAttribute("employeeProject") EmployeeProject employeeProjects,
                                        RedirectAttributes redirectAttributes) {
        String validationError = ProjectWarning.validateEmployeeProject(employeeProjects);
        Long employeeId = employeeProjects.getEmployee().getId();
        Double monthList =
                employeeService.getEmployeeBookedMonths(employeeProjects.getEmployeeProjectStartDate(),
                        employeeProjects.getEmployeeProjectEndDate());

        EmployeeProject updateEmployeeProject = employeeProjectService.setUpdatedEmployeeProjectAttributes(id, employeeProjects, monthList);

        if (!validationError.isEmpty()) {
            redirectAttributes.addFlashAttribute("failed", validationError);
            return "redirect:/ines/showFormForEmployeeProjectUpdate/" + id;
        }

        employeeProjectService.saveEmployeeProject(updateEmployeeProject);
        redirectAttributes.addAttribute("updatedEmployeeId", employeeId);
        return "redirect:/ines/employees";

    }

    /**
     * Method for deleting a project by its id, but will not delete the project if associated employeeProjects exist.
     *
     * @param id the employee id of the project being deleted
     * @param redirectAttributes the object passed when redirecting
     * @return "redirect:/ines/projects" where the project is now deleted
     */
    @GetMapping("/deleteProject/{deleteId}")
    public String deleteProject(@PathVariable(value = "deleteId") long id,  RedirectAttributes redirectAttributes) {
        List<EmployeeProject> employeeProjects = employeeProjectRepository.getAllEmployeeProjectsByProjectId(id);
        List<CustomProjectDetails> customProjectDetailsList = customProjectDetailsService.getAllCustomProjectDetailsByProjectId(id);
        String projectName = projectService.getProjectById(id).getName();
        String validationError = ProjectWarning.validateListOfEmployeeProjects(employeeProjects);
        if (!employeeProjects.isEmpty()) {
            redirectAttributes.addFlashAttribute("failed", validationError);
            return "redirect:/ines/projects";
        }
        if (!customProjectDetailsList.isEmpty()) {
            customProjectDetailsService.deleteAllCustomProjectDetailsByProjectId(id);
        }
        this.projectService.deleteProjectById(id);
        redirectAttributes.addFlashAttribute("success",  projectName + " was successfully deleted.");
        return "redirect:/ines/projects";
    }

    /**
     * Method for setting archived to false by project id.
     *
     * @param id the project id of the project archive status being changed
     * @param redirectAttributes the object passed when redirecting
     * @return "redirect:/ines/projects" where the project archive status is now changed
     */
    @GetMapping("/unarchiveProject/{deleteId}")
    public String unarchiveProject(@PathVariable(value = "deleteId") long id,  RedirectAttributes redirectAttributes) {
        String projectName = projectService.getProjectById(id).getName();
        this.projectService.unarchiveProjectById(id);
        redirectAttributes.addFlashAttribute("success",  projectName + " was successfully unarchived.");
        return "redirect:/ines/projects";
    }

    /**
     * Method for paginating the list of projects.
     *
     * @param pageNo the current page number
     * @param sortField sorting by name, date, id etc.
     * @param sortDir direction to sort e.g. ascending or descending
     * @param model the model object used to pass data to the view
     * @return "redirect:/ines/projects" where the project is now deleted
     */
    @GetMapping("/projectPage/{pageNo}")
    public String findProjectPaginated(@PathVariable(value = "pageNo") int pageNo,
                                       @RequestParam("sortField") String sortField,
                                       @RequestParam("sortDir") String sortDir,
                                       Model model) {
        int pageSize = 10;

        Page<Project> page = projectService.findPaginatedArchivedFalse(pageNo, pageSize, sortField, sortDir);
        List<Project> listProjects = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listProjects", listProjects);
        return "project/projects.html";
    }

    /**
     * Method for paginating the list of archived projects.
     *
     * @param pageNo the current page number
     * @param sortField sorting by name, date, id etc.
     * @param sortDir direction to sort e.g. ascending or descending
     * @param model the model object used to pass data to the view
     * @return "redirect:/ines/archivedProjects" where the project is now deleted
     */
    @GetMapping("/archivedProjectPage/{pageNo}")
    public String findArchivedProjectPaginated(@PathVariable(value = "pageNo") int pageNo,
                                       @RequestParam("sortField") String sortField,
                                       @RequestParam("sortDir") String sortDir,
                                       Model model) {
        int pageSize = 10;

        Page<Project> page = projectService.findPaginatedArchivedTrue(pageNo, pageSize, sortField, sortDir);
        List<Project> listProjects = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listProjects", listProjects);
        return "project/archived_projects.html";
    }

}
