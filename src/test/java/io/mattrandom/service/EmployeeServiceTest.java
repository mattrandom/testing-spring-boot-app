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
    private EmployeeRepository employeeRepository;

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
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        given(employeeRepository.save(any(Employee.class))).willReturn(employee);

        //when
        Employee employeeSaved = employeeService.saveEmployee(employee);

        //then
        assertThat(employeeSaved).isNotNull();
    }

    @Test
    @DisplayName("JUnit test verifying if the exception is being thrown")
    void givenExistingEmail_whenSaveEmployee_thenThrowsException() {
        //given
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        //when & then
        assertThrows(ResourceNotFoundException.class, () -> employeeService.saveEmployee(employee));
        verify(employeeRepository, never()).save(any(Employee.class));
        then(employeeRepository).should(never()).save(any(Employee.class));
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
        given(employeeRepository.findAll()).willReturn(employeeList);

        //when
        List<Employee> getEmployeeList = employeeService.getAllEmployees();

        //then
        assertThat(getEmployeeList).hasSize(2);
        assertThat(getEmployeeList.size()).isEqualTo(2);
        then(employeeRepository).should(times(1)).findAll();
        then(employeeRepository).should(atLeast(1)).findAll();
        then(employeeRepository).should(atMostOnce()).findAll();
    }

    @Test
    @DisplayName("JUnit test for verifying if Employee's colelction is empty")
    void givenEmptyEmployeeList_whenGetAllEmployees_thenReturnEmptyEmployeeList() {
        //given
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        //when
        List<Employee> getEmployeeList = employeeService.getAllEmployees();

        //then
        assertThat(getEmployeeList).isEmpty();
        assertThat(getEmployeeList).hasSize(0);
    }

}