package com.employee_planning.repository;

import com.employee_planning.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Boolean existsByName(String name);

    @Query(value = "SELECT * FROM employees e WHERE e.archived = false", nativeQuery = true)
    List<Employee> findAllEmployeesWhereArchivedIsFalse();

    @Query(value = "SELECT * FROM employees e WHERE e.archived = true", nativeQuery = true)
    List<Employee> findAllEmployeesWhereArchivedIsTrue();
}
