package io.mattrandom.repository;

import io.mattrandom.model.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @DisplayName("JUnit test for saving Employee object")
    void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        //given
        Employee employee = Employee.builder()
                .firstName("Matt")
                .lastName("Random")
                .email("test@gmail.com")
                .build();

        //when
        Employee savedEmployee = employeeRepository.save(employee);

        //then
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee).isEqualTo(employee);
        assertThat(savedEmployee.getId()).isGreaterThanOrEqualTo(1);
    }

    @Test
    @DisplayName("JUnit test for getting Employees collection")
    void givenEmployeesList_whenFindAll_thenReturnEmployeesList() {
        //given
        Employee employee1 = Employee.builder()
                .firstName("Matt")
                .lastName("Random")
                .email("test@gmail.com")
                .build();

        Employee employee2 = Employee.builder()
                .firstName("Matheus")
                .lastName("Test")
                .email("email@gmail.com")
                .build();

        List<Employee> employees = employeeRepository.saveAll(List.of(employee1, employee2));

        //when
        List<Employee> employeesFounded = employeeRepository.findAll();

        //then
        assertThat(employeesFounded).isNotNull();
        assertThat(employeesFounded.get(0).getFirstName()).isEqualTo(employee1.getFirstName());
        assertThat(employeesFounded).hasSize(2);

    }

    @Test
    @DisplayName("JUnit test for fetching a single Employee object")
    void givenEmployee_whenFindById_thenEmployeeObject() {
        //given
        Employee employee = Employee.builder()
                .firstName("Matt")
                .lastName("Random")
                .email("test@gmail.com")
                .build();
        Employee empSaved = employeeRepository.save(employee);

        //when
        Optional<Employee> empByID = employeeRepository.findById(employee.getId());

        //then
        assertThat(empByID.get()).isNotNull();
        assertThat(empByID.get().getLastName()).hasSize(employee.getLastName().length());

    }
}