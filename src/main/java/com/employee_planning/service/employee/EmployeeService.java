package com.employee_planning.service.employee;

import com.employee_planning.model.Employee;
import com.employee_planning.model.EmployeeProject;

import java.util.List;

public interface EmployeeService {

	List<Employee> getAllEmployees();

	Employee saveEmployee(Employee employee);
	Employee saveEmployeeAndEmployeeProject(Employee employee, EmployeeProject employeeProject);

	Employee setUpdatedEmployeeAttributes(Long id, Employee employee);

	Employee getEmployeeById(long id);
	void deleteEmployeeById(long id);

	void unarchiveEmployeeById(long id);
	Double getEmployeeBookedMonths(String startDates, String endDates);
}
