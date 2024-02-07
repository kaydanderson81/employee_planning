package com.employee_planning.service.employee;


import com.employee_planning.model.Employee;
import com.employee_planning.model.EmployeeProject;
import com.employee_planning.model.Project;
import com.employee_planning.repository.EmployeeProjectRepository;
import com.employee_planning.repository.EmployeeRepository;
import com.employee_planning.repository.ProjectRepository;
import com.employee_planning.service.employeeProject.EmployeeProjectService;
import com.employee_planning.service.project.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;


@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private EmployeeProjectRepository employeeProjectRepository;

	@Autowired
	private EmployeeProjectService employeeProjectService;


	@Override
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	@Override
	public Employee saveEmployee(Employee employee) {
		employee.setName(employee.getFirstName(), employee.getLastName());
		return this.employeeRepository.save(employee);
	}

	@Override
	public Employee saveEmployeeAndEmployeeProject(Employee employee, EmployeeProject employeeProject) {
		employee.setName(employee.getFirstName(), employee.getLastName());

		employeeProject.setEmployee(employee);
		Project project = projectService.getProjectById(employeeProject.getProject().getId());
		employeeProject.setProject(project);
		employeeProject.setEmployeeProjectName(project.getName());

		List<EmployeeProject> employeeProjects = employee.getEmployeeProjects();
		assert employeeProjects != null;
		employeeProjects.add(employeeProject);

		return this.employeeRepository.save(employee);
    }

	@Override
	public Employee setUpdatedEmployeeAttributes(Long id, Employee employee) {
		Employee updatedEmployee = getEmployeeById(id);
		updatedEmployee.setFirstName(employee.getFirstName());
		updatedEmployee.setLastName(employee.getLastName());
		updatedEmployee.setName(employee.getFirstName(), employee.getLastName());
		updatedEmployee.setContractedFrom(employee.getContractedFrom());
		updatedEmployee.setContractedTo(employee.getContractedTo());
		updatedEmployee.setArchived(employee.isArchived());
		if (employee.getContractedTo().isEmpty()) {
			updatedEmployee.setContractedTo("Date not set");
		}
		return updatedEmployee;
	}


	@Override
	public Employee getEmployeeById(long id) {
		Optional<Employee> optional = employeeRepository.findById(id);
		Employee employee;
		if (optional.isPresent()) {
			employee = optional.get();
		} else {
			throw new RuntimeException("Employee not found for id :: " + id);
		}
		return employee;
	}

	@Override
	public void deleteEmployeeById(long id) {
		this.employeeRepository.deleteById(id);
	}

	@Override
	public void unarchiveEmployeeById(long id) {
		Optional<Employee> employee = employeeRepository.findById(id);
		if (employee.isPresent()) {
			employee.get().setArchived(false);
			saveEmployee(employee.get());
		} else {
			throw new RuntimeException("Employee not found for id :: " + id);
		}
    }


	@Override
	public Double getEmployeeBookedMonths(String startDates, String endDates) {
		double monthsBetween = ChronoUnit.MONTHS.between(
				LocalDate.parse(startDates),
				LocalDate.parse(endDates).plusDays(1));
		String subStart = startDates.substring(startDates.length() - 2);
		String subEnd = endDates.substring(endDates.length() - 2);
		if (subStart.equals("15") && !subEnd.equals("14")) {
			return monthsBetween + 0.5;
		}
		if (!subStart.equals("15") && subEnd.equals("14")) {
			return monthsBetween + 0.5;
		}
		if (subStart.equals("15") && monthsBetween < 0.5) {
			return monthsBetween + 1;
		} else {
			return  monthsBetween;
		}
	}
}
