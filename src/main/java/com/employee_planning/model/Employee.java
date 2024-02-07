package com.employee_planning.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employees")
public class Employee {

	@Id
	@Column(name = "employee_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(name = "name", nullable = false)
	private String name;

	@JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
	@Column(name = "contracted_from")
	private String contractedFrom;

	@JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
	@Column(name = "contracted_to")
	private String contractedTo;

	@OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Nullable
	private List<EmployeeProject> employeeProjects = new ArrayList<>();

	@Column(name = "archived", nullable = false)
	private boolean archived = false;

	public Employee() {
	}

	public Employee(Long id, String firstName, String lastName, String name, String contractedFrom,
					String contractedTo, boolean archived) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.name = name;
		this.contractedFrom = contractedFrom;
		this.contractedTo = contractedTo;
		this.archived = archived;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getName() {
		return name;
	}

	public void setName(String firstName, String lastName) {
		this.name = firstName + " " + lastName;
	}

	public String getContractedFrom() {
		return contractedFrom;
	}

	public void setContractedFrom(String contractedFrom) {
		this.contractedFrom = contractedFrom;
	}

	public String getContractedTo() {
		return contractedTo;
	}

	public void setContractedTo(String contractedTo) {
		this.contractedTo = contractedTo;
	}

	public List<EmployeeProject> getEmployeeProjects() {
		return employeeProjects;
	}

	public void setEmployeeProject(List<EmployeeProject> employeeProjects) {
		this.employeeProjects = employeeProjects;
	}

	public void addEmployeeProject(EmployeeProject employeeProject) {
		assert this.employeeProjects != null;
		this.employeeProjects.add(employeeProject);
		employeeProject.setEmployee(this);
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	@Override
	public String toString() {
		return "Employee{" +
				"id=" + id +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", name='" + name + '\'' +
				", contractedFrom='" + contractedFrom + '\'' +
				", contractedTo='" + contractedTo + '\'' +
				", employeeProjects=" + employeeProjects +
				", archived='" + archived + '\'' +
				'}';
	}
}
