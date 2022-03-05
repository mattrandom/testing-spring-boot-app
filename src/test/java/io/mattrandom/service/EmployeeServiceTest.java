package io.mattrandom.service;

import io.mattrandom.exception.ResourceNotFoundException;
import io.mattrandom.model.Employee;
import io.mattrandom.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepositoryMock;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .firstName("Matt")
                .lastName("Random")
                .email("test@gmail.com")
                .build();
    }

    @Test
    @DisplayName("JUnit test for saveEmployee method")
    void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
        //given
        given(employeeRepositoryMock.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        given(employeeRepositoryMock.save(any(Employee.class))).willReturn(employee);

        //when
        Employee employeeSaved = employeeService.saveEmployee(employee);

        //then
        assertThat(employeeSaved).isNotNull();
    }

    @Test
    @DisplayName("JUnit test verifying if the exception is being thrown")
    void givenExistingEmail_whenSaveEmployee_thenThrowsException() {
        //given
        given(employeeRepositoryMock.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        //when & then
        assertThrows(ResourceNotFoundException.class, () -> employeeService.saveEmployee(employee));
        verify(employeeRepositoryMock, never()).save(any(Employee.class));
        then(employeeRepositoryMock).should(never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("JUnit test for fetching Employee's collection")
    void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeeList() {
        //given
        Employee employee2 = Employee.builder()
                .firstName("Matheus")
                .lastName("Siegmund")
                .email("random@gmail.com")
                .build();
        List<Employee> employeeList = List.of(this.employee, employee2);
        given(employeeRepositoryMock.findAll()).willReturn(employeeList);

        //when
        List<Employee> getEmployeeList = employeeService.getAllEmployees();

        //then
        assertThat(getEmployeeList).hasSize(2);
        assertThat(getEmployeeList.size()).isEqualTo(2);
        then(employeeRepositoryMock).should(times(1)).findAll();
        then(employeeRepositoryMock).should(atLeast(1)).findAll();
        then(employeeRepositoryMock).should(atMostOnce()).findAll();
    }

    @Test
    @DisplayName("JUnit test for verifying if Employee's collection is empty")
    void givenEmptyEmployeeList_whenGetAllEmployees_thenReturnEmptyEmployeeList() {
        //given
        given(employeeRepositoryMock.findAll()).willReturn(Collections.emptyList());

        //when
        List<Employee> getEmployeeList = employeeService.getAllEmployees();

        //then
        assertThat(getEmployeeList).isEmpty();
        assertThat(getEmployeeList).hasSize(0);
    }

    @Test
    @DisplayName("JUnit test for fetching single Employee object by given ID")
    void givenEmployeeObject_whenGetEmployeeById_thenReturnEmployee() {
        //given
        given(employeeRepositoryMock.findById(anyLong())).willReturn(Optional.of(employee));

        //when
        Employee emp = employeeService.getEmployeeById(anyLong());

        //then
        assertThat(emp).isNotNull();
        then(employeeRepositoryMock).should(atLeastOnce()).findById(anyLong());
    }

    @Test
    @DisplayName("JUnit test for testing exception invocation")
    void givenEmployeeObjet_whenGetEmployeeById_thenReturnEmployee() {
        //given
        given(employeeRepositoryMock.findById(anyLong())).willReturn(Optional.empty());

        //when
        //then
        assertThrows(ResourceNotFoundException.class, () -> employeeService.getEmployeeById(anyLong()));
        then(employeeRepositoryMock).should(atLeastOnce()).findById(anyLong());
    }

    @Test
    @DisplayName("JUnit test for updating Employee object")
    void givenEmployeeObjet_whenUpdateEmployee_thenReturnEmployeeUpdated() {
        //given
        given(employeeRepositoryMock.save(employee)).willReturn(employee);
        employee.setEmail("whatever@asd.com");
        employee.setFirstName("Mattrandom");

        //when
        Employee updatedEmployee = employeeService.updateEmployee(this.employee);

        //then
        assertThat(updatedEmployee.getFirstName()).isNotEqualTo("Matt");

    }

    @Test
    @DisplayName("JUnit test for deleting Employee object by id")
    void givenEmployeeId_whenDeleteEmployee_thenReturnNothing() {
        //given
        Long employeeId = 1L;

        //when
        employeeService.deleteEmployee(employeeId);

        //then
        then(employeeRepositoryMock).should(atLeastOnce()).deleteById(employeeId);

    }



}