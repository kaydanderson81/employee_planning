package com.employee_planning.repository;

import com.employee_planning.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Boolean existsByName(String name);
    Boolean existsByProjectNumber(Long number);
    Page<Project> findByArchivedFalse(Pageable pageable);
    Page<Project> findByArchivedTrue(Pageable pageable);

    @Query(value = "SELECT * FROM projects p WHERE p.archived = false", nativeQuery = true)
    List<Project> findAllProjectsWhereArchivedIsFalse();

}
