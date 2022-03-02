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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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
    @DisplayName("JUnit test verifying it the exception is being thrown")
    void givenExistingEmail_whenSaveEmployee_thenThrowsException() {
        //given
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        //when & then
        assertThrows(ResourceNotFoundException.class, () -> employeeService.saveEmployee(employee));
    }

}