package com.employee_planning.repository;

import com.employee_planning.model.CustomProjectDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomProjectDetailsRepository extends JpaRepository<CustomProjectDetails, Long> {

    @Query(value = "SELECT * FROM custom_project_details cpd WHERE cpd.project_id = :projectId", nativeQuery = true)
    List<CustomProjectDetails> listAllCustomProjectDetailsByProjectId(@Param("projectId") Long projectId);


}
