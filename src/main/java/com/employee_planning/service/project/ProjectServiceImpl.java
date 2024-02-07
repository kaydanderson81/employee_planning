package com.employee_planning.service.project;

import com.employee_planning.model.CustomProjectDetails;
import com.employee_planning.model.EmployeeProject;
import com.employee_planning.model.Project;
import com.employee_planning.model.chart.ChartYear;
import com.employee_planning.repository.EmployeeProjectRepository;
import com.employee_planning.repository.ProjectRepository;
import com.employee_planning.service.employeeProject.EmployeeProjectService;
import com.employee_planning.service.employeeProject.EmployeeProjectServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeProjectRepository employeeProjectRepository;

    @Autowired
    private EmployeeProjectService employeeProjectService;

    @Autowired
    private EmployeeProjectServiceImpl employeeProjectServiceImpl;

    @Autowired
    private CustomProjectDetailsService customProjectDetailsService;


    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public void saveProject(Project project) {
        this.projectRepository.save(project);
    }

    @Override
    public Project getProjectById(long id) {
        Optional<Project> optional = projectRepository.findById(id);
        Project project;
        if (optional.isPresent()) {
            project = optional.get();
        } else {
            throw new RuntimeException("Project not found for id :: " + id);
        }
        return project;
    }

    @Override
    public void deleteProjectById(long id) {
        this.projectRepository.deleteById(id);
    }

    @Override
    public void unarchiveProjectById(long id) {
        Optional<Project> project = projectRepository.findById(id);
        if (project.isPresent()) {
            project.get().setArchived(false);
            saveProject(project.get());
        } else {
            throw new RuntimeException("Project not found for id :: " + id);
        }
    }

    @Override
    public Project setUpdatedProjectAttributes(Long id, Project project) {
        Project projectToUpdate = getProjectById(id);
        projectToUpdate.setProjectNumber(project.getProjectNumber());
        projectToUpdate.setName(project.getName());
        projectToUpdate.setStartDate(project.getStartDate());
        projectToUpdate.setEndDate(project.getEndDate());
        projectToUpdate.setProjectLengthInMonths(project.getProjectLengthInMonths());
        projectToUpdate.setComment(project.getComment());
        projectToUpdate.setArchived(project.isArchived());
        if (projectToUpdate.getStartDate().isEmpty()) {
            projectToUpdate.setStartDate("Date Not Set");
        }
        if (projectToUpdate.getEndDate().isEmpty()) {
            projectToUpdate.setEndDate("Date Not Set");
        }
        return projectToUpdate;
    }

    @Override
    public void updateEmployeeProjectBookedMonths() {
        List<Project> projects = getAllProjects();
        for (Project project : projects) {
            List<Double> listDoubles = employeeProjectRepository.listAllEmployeeBookedMonthsByProjectId(project.getId());

            double total = listDoubles.stream().mapToDouble(Double::doubleValue).sum();
            project.setCurrentBookedMonths(total);
            project.setRemainingBookedMonths(project.getProjectLengthInMonths() - total);
            saveProject(project);
        }
    }

    @Override
    public List<Project> getAListOfProjectsAndTheirPersonMonthsByYear(ChartYear year) {
        LocalDate yearObjStart = LocalDate.parse(year.getYear() + "-01-01");
        LocalDate yearObjEnd = LocalDate.parse(year.getYear() + "-12-31");
        List<Project> projects = projectRepository.findAllProjectsWhereArchivedIsFalse();

        projects.removeIf(project -> {
            if (project.getStartDate().equals("Date Not Set") || project.getEndDate().equals("Date Not Set")) {
                project.setStartDate(year.getYear() + "-01-01");
                project.setEndDate(year.getYear() + "-12-31");
            }
            LocalDate startDate = LocalDate.parse(project.getStartDate());
            LocalDate endDate = LocalDate.parse(project.getEndDate());
            if ((startDate.isBefore(yearObjStart) && endDate.isBefore(yearObjStart))
                    || (startDate.isAfter(yearObjEnd) && endDate.isAfter(yearObjEnd))) {
                return true;
            }
            if (startDate.isBefore(yearObjStart)) {
                startDate = yearObjStart;
            }
            if (endDate.isAfter(yearObjEnd)) {
                endDate = yearObjEnd;
            }
            long months = ChronoUnit.MONTHS.between(startDate, endDate.plusDays(1));
            project.setProjectLengthInMonths(months);
            return false;
        });
        for (Project project : projects) {
            List<EmployeeProject> employeeProjects =
                    employeeProjectServiceImpl.getAllEmployeeProjectsByProjectId(project.getId());

            List<EmployeeProject> filteredEmployeeProjects = employeeProjects.stream()
                    .filter(employeeProject -> {
                        LocalDate epStartDate = LocalDate.parse(employeeProject.getEmployeeProjectStartDate());
                        LocalDate epEndDate = LocalDate.parse(employeeProject.getEmployeeProjectEndDate());
                        return (epStartDate.isEqual(yearObjStart) || epStartDate.isAfter(yearObjStart))
                                && (epEndDate.isEqual(yearObjEnd) || epEndDate.isBefore(yearObjEnd))
                                && (epStartDate.isBefore(yearObjEnd) || epStartDate.isEqual(yearObjEnd))
                                && (epEndDate.isAfter(yearObjStart) || epEndDate.isEqual(yearObjStart));
                    })
                    .collect(Collectors.toList());
            if (!filteredEmployeeProjects.isEmpty()) {
                double sumBookedMonths = filteredEmployeeProjects.stream()
                        .mapToDouble(EmployeeProject::getEmployeeBookedMonths)
                        .sum();

                project.setCurrentBookedMonths(sumBookedMonths);
            } else {
                project.setCurrentBookedMonths(0.0);
            }
            checkIfCustomProjectDetailsExistAndSetProjectsMonthsStartDateAndEndDate(year, project);
        }
        projects.sort(Comparator.comparing(Project::getName));
        return projects;
    }

    @Override
    public Page<Project> findPaginatedArchivedFalse(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.projectRepository.findByArchivedFalse(pageable);
    }

    @Override
    public Page<Project> findPaginatedArchivedTrue(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.projectRepository.findByArchivedTrue(pageable);
    }

    @Override
    public Double getListOfEmployeeBookedMonths(String startDates, String endDates) {
        double monthsBetween = ChronoUnit.MONTHS.between(
                LocalDate.parse(startDates),
                LocalDate.parse(endDates).plusDays(1));
        String substr = endDates.substring(endDates.length() - 2);
        if (substr.equals("15")) {
            return monthsBetween + 0.5;
        }
        return monthsBetween;
    }

    public void checkIfCustomProjectDetailsExistAndSetProjectsMonthsStartDateAndEndDate(ChartYear year, Project project) {
        List<CustomProjectDetails> customProjectDetailsList = customProjectDetailsService.getAllCustomProjectDetailsByProjectId(project.getId());
        if (customProjectDetailsList != null) {
            for (CustomProjectDetails customProjectDetail : customProjectDetailsList) {
                if (customProjectDetail.getCustomProjectYear().equals(year.getYear())) {
                    project.setProjectLengthInMonths(customProjectDetail.getCustomProjectPersonMonths());
                }
            }
        }
    }

}
