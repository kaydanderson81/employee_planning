package com.employee_planning.controller;

import com.employee_planning.model.Project;
import com.employee_planning.model.CustomProjectDetails;
import com.employee_planning.repository.CustomProjectDetailsRepository;
import com.employee_planning.service.project.CustomProjectDetailsService;
import com.employee_planning.service.project.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ines")
public class CustomProjectDetailsController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CustomProjectDetailsService customProjectDetailsService;

    @Autowired
    private CustomProjectDetailsRepository customProjectDetailsRepository;

    /**
     * Displays the form for updating a custom project.
     *
     * @param id the id of the project to update
     * @param model the model object used to pass data to the view
     * @return "custom_project.html" form for updating a project
     */
    @GetMapping("/showFormForCustomProjectUpdate/{id}")
    public String showFormForCustomUpdate(@PathVariable(value = "id") long id, Model model) {
        Project project = projectService.getProjectById(id);
        model.addAttribute("project", project);
        return "project/custom_project";
    }

    /**
     * Post method for customizing an existing project.
     *
     * @param id the project id
     * @param project the project object from the custom_project form
     * @param redirectAttributes the object passed when redirecting
     * @return "redirect:/ines/update_project" with the updated custom project dates
     */
    @PostMapping("/updateCustomProject/{id}")
    public String updateCustomProject(@PathVariable(value = "id") long id,
                                      @ModelAttribute("project") Project project,
                                      @RequestParam("name") String projectName,
                                      @RequestParam("year") String projectYear,
                                      RedirectAttributes redirectAttributes) {

        CustomProjectDetails customProjectDetails = new CustomProjectDetails();
        customProjectDetails.setProject(project);
        customProjectDetails.setCustomProjectYear(projectYear);
        customProjectDetails.setCustomProjectPersonMonths(project.getProjectLengthInMonths());

        if (customProjectDetailsService.checkIfCustomProjectYearExists(id, projectYear)) {
            redirectAttributes.addFlashAttribute("failed", "Project: " + project.getName() +
                    " already has a custom date for year: " + projectYear);
            return "redirect:/ines/showFormForProjectUpdate/" + id;
        }

        customProjectDetailsService.saveCustomProjectDetails(customProjectDetails);

        redirectAttributes.addFlashAttribute("success", "Added custom dates for project: " + customProjectDetails.getProject().getName());
        return "redirect:/ines/showFormForProjectUpdate/" + id;
    }

    /**
     * Method for deleting a custom project by its id.
     *
     * @param id the CustomProjectDetails id of the CustomProjectDetails being deleted
     * @param redirectAttributes the object passed when redirecting
     * @return "redirect:/ines/update_project" where the CustomProjectDetails is now deleted
     */
    @GetMapping("/deleteCustomProject/{customId}")
    public String deleteProject(@PathVariable(value = "customId") long id,
                                @RequestParam("projectId") Long projectId,
                                RedirectAttributes redirectAttributes) {
        CustomProjectDetails customProjectDetails = customProjectDetailsService.getCustomProjectDetailsById(id);
        this.customProjectDetailsService.deleteCustomProjectDetails(id);
        redirectAttributes.addFlashAttribute("success",
                customProjectDetails.getProject().getName() + " year : " +
                        customProjectDetails.getCustomProjectYear() + ", PM: " +
                        customProjectDetails.getCustomProjectPersonMonths()
                        + " was successfully deleted.");
        return "redirect:/ines/showFormForProjectUpdate/" + projectId;
    }
}
