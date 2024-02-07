package com.employee_planning.service.project;

import com.employee_planning.model.Project;
import com.employee_planning.model.chart.ChartYear;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProjectService {
    List<Project> getAllProjects();
    void saveProject(Project project);
    Project getProjectById(long id);
    void deleteProjectById(long id);
    void unarchiveProjectById(long id);
    Project setUpdatedProjectAttributes(Long id, Project project);
    void updateEmployeeProjectBookedMonths();

    List<Project> getAListOfProjectsAndTheirPersonMonthsByYear(ChartYear year);
    Page<Project> findPaginatedArchivedFalse(int pageNo, int pageSize, String sortField, String sortDirection);
    Page<Project> findPaginatedArchivedTrue(int pageNo, int pageSize, String sortField, String sortDirection);

    Double getListOfEmployeeBookedMonths(String startDates, String endDates);
}
