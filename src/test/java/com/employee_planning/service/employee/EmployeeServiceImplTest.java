package com.employee_planning.service.employee;

import com.employee_planning.model.Employee;
import com.employee_planning.repository.EmployeeRepository;
import com.employee_planning.EmployeeProjectTestBase;
import com.employee_planning.service.project.ProjectServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
class EmployeeServiceImplTest extends EmployeeProjectTestBase {

    @InjectMocks
    private EmployeeServiceImpl employeeServiceImpl;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ProjectServiceImpl projectService;


    @Test
    @DisplayName("Should get all employees from database")
    void getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        employees.add(employee1);
        employees.add(employee2);
        employees.add(employee3);

        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> getAllEmployees = employeeServiceImpl.getAllEmployees();
        assertEquals(3, getAllEmployees.size());
        assertNotNull(getAllEmployees);
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should save an Employee to database")
    void saveEmployee() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee1);
        Employee newEmployee = employeeServiceImpl.saveEmployee(employee1);
        verify(employeeRepository, times(1)).save(any());
        assertNotNull(newEmployee);
        assertEquals(newEmployee.getName(), employee1.getName());
    }

    @Test
    @DisplayName("Should save a new employee with an associated employeeProject")
    void saveEmployeeAndEmployeeProject() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee1);
        when(projectService.getProjectById(eq(employeeProject1.getProject().getId()))).thenReturn(project1);

        Employee newEmployee = employeeServiceImpl.saveEmployeeAndEmployeeProject(employee1, employeeProject1);

        verify(employeeRepository, times(1)).save(any());
        verify(projectService, times(1)).getProjectById(eq(employeeProject1.getProject().getId()));

        assertNotNull(newEmployee);
        assertEquals(1, newEmployee.getId());
        assert newEmployee.getEmployeeProjects() != null;
        assertEquals(4, newEmployee.getEmployeeProjects().size());
    }

    @Test
    void setUpdatedEmployeeAttributes() {
        long id = 1L;
        when(employeeRepository.findById(id)).thenReturn(Optional.ofNullable(this.employee1));
        Employee result = employeeServiceImpl.setUpdatedEmployeeAttributes(id, this.employee2);
        assertEquals(1L, result.getId());
        assertEquals("Test Employee2", result.getName());
    }

    @Test
    void setUpdatedEmployeeAttributes_WithNoDateSet() {
        long id = 1L;
        when(employeeRepository.findById(id)).thenReturn(Optional.ofNullable(this.employee1));
        this.employee2.setContractedTo("");
        Employee result = employeeServiceImpl.setUpdatedEmployeeAttributes(id, this.employee2);
        assertEquals(1L, result.getId());
        assertEquals("Test Employee2", result.getName());
        assertEquals("Date not set", result.getContractedTo());
    }

    @Test
    @DisplayName("Should return an Employee by Id")
    void getEmployeeById() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee1));
        Employee existingEmployee = employeeServiceImpl.getEmployeeById(1L);
        assertNotNull(existingEmployee);
        assertEquals(existingEmployee.getId(), employee1.getId());
    }

    @Test
    @DisplayName("Should return a runtimeException as the employeeId doesn't exist")
    void getChemicalById_Fail_ThrowsRuntimeException() {
        assertThrows(RuntimeException.class, () -> employeeServiceImpl.getEmployeeById(999));
    }

    @Test
    @DisplayName("Should delete an Employee by id")
    void deleteEmployeeById() {
        employeeServiceImpl.deleteEmployeeById(employee1.getId());
        verify(employeeRepository, times(1)).deleteById(employee1.getId());
    }

    @Test
    void getEmployeeBookedMonths() {
        String startDate1 = "2023-01-01";
        String endDate1 = "2023-06-30";
        Double expectedMonths1 = 6.0;
        Double actualMonths1 = employeeServiceImpl.getEmployeeBookedMonths(startDate1, endDate1);
        assertEquals(expectedMonths1, actualMonths1);
    }
}
