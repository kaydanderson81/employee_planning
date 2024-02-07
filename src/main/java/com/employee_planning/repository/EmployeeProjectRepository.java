package com.employee_planning.repository;

import com.employee_planning.model.EmployeeProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface EmployeeProjectRepository extends JpaRepository<EmployeeProject, Long> {

    @Query(value = "SELECT * FROM employee_projects ep WHERE ep.employee_id = :id", nativeQuery = true)
    List<EmployeeProject> findAllEmployeeProjectByEmployeeId(@Param("id")Long id);

    @Query(value = "SELECT employee_booked_months FROM employee_projects WHERE project_id = :projectId", nativeQuery = true)
    List<Double> listAllEmployeeBookedMonthsByProjectId(@Param("projectId") Long projectId);

    @Query(value = "SELECT employee_project_start_date FROM employee_projects", nativeQuery = true)
    Set<String> findAllEmployeeProjectStartDates();

    @Query(value = "SELECT employee_project_end_date FROM employee_projects", nativeQuery = true)
    Set<String> findAllEmployeeProjectEndDates();

    @Query(value = "SELECT * FROM employee_projects WHERE project_id = :projectId", nativeQuery = true)
    List<EmployeeProject> getAllEmployeeProjectsByProjectId(@Param("projectId") Long projectId);

}
