package io.mattrandom.repository;

import io.mattrandom.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test") // it uses in-memory DB config
class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

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
    @DisplayName("JUnit test for saving Employee object")
    void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        //given

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

        Employee employee2 = Employee.builder()
                .firstName("Matheus")
                .lastName("Test")
                .email("email@gmail.com")
                .build();

        List<Employee> employees = employeeRepository.saveAll(List.of(employee, employee2));

        //when
        List<Employee> employeesFounded = employeeRepository.findAll();

        //then
        assertThat(employeesFounded).isNotNull();
        assertThat(employeesFounded.get(0).getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(employeesFounded).hasSize(2);

    }

    @Test
    @DisplayName("JUnit test for fetching a single Employee object")
    void givenEmployee_whenFindById_thenEmployeeObject() {
        //given
        Employee empSaved = employeeRepository.save(employee);

        //when
        Optional<Employee> empByID = employeeRepository.findById(employee.getId());

        //then
        assertThat(empByID.get()).isNotNull();
        assertThat(empByID.get().getLastName()).hasSize(employee.getLastName().length());

    }

    @Test
    @DisplayName("JUnit test for fetching an Employee object by 'email' property")
    void givenEmployee_whenFindByEmail_thenEmployeeObject() {
        //given
        Employee empSaved = employeeRepository.save(employee);

        //when
        Employee empByEmail = employeeRepository.findByEmail(employee.getEmail()).get();

        //then
        assertThat(empByEmail).isNotNull();
        assertThat(empByEmail.getEmail()).isEqualTo(employee.getEmail());
    }

    @Test
    @DisplayName("JUnit test for checking if an email of Employee object was correctly updated")
    void givenEmployee_whenUpdateEmployee_thenUpdatedEmployee() {
        //given
        Employee empSaved = employeeRepository.save(employee);

        //when
        Employee empFetched = employeeRepository.findById(employee.getId()).get();
        empFetched.setEmail("changed@gmail.com");
        Employee empUpdated = employeeRepository.save(empFetched);

        //then
        assertThat(empUpdated.getEmail()).isEqualTo("changed@gmail.com");
    }

    @Test
    @DisplayName("JUnit test for verifying if an Employee was properly removed")
    void givenEmployee_whenDeleteObject_thenRemoveEmployee() {
        //given
        Employee empSaved = employeeRepository.save(employee);

        //when
        employeeRepository.delete(empSaved);
        Optional<Employee> empById = employeeRepository.findById(employee.getId());

        //then
        assertThat(empById).isEmpty();
    }

    @Test
    @DisplayName("JUnit test for verifying if an JPQL method (custom query with index) is correctly executed")
    void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject() {
        //given
        Employee empSaved = employeeRepository.save(employee);

        //when
        Employee byFirstNameAndLastName = employeeRepository.findByJPQL("Matt", "Random");

        //then
        assertThat(byFirstNameAndLastName).isNotNull();
        assertThat(byFirstNameAndLastName.getFirstName()).isEqualTo("Matt");
        assertThat(byFirstNameAndLastName.getLastName()).isEqualTo("Random");
    }

    @Test
    @DisplayName("JUnit test for verifying if an JPQL method (custom query with named parameters) is correctly executed")
    void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject() {
        //given
        Employee empSaved = employeeRepository.save(employee);

        //when
        Employee byFirstNameAndLastName = employeeRepository.findByJPQLNamedParams("Matt", "Random");

        //then
        assertThat(byFirstNameAndLastName).isNotNull();
        assertThat(byFirstNameAndLastName.getFirstName()).isEqualTo("Matt");
        assertThat(byFirstNameAndLastName.getLastName()).isEqualTo("Random");
    }

    @Test
    @DisplayName("JUnit test for verifying if an Native SQL method (Custom Native Query with index parameters) is correctly executed")
    void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject() {
        //given
        Employee empSaved = employeeRepository.save(employee);

        //when
        Employee byFirstNameAndLastName = employeeRepository.findByNativeSQL("Matt", "Random");

        //then
        assertThat(byFirstNameAndLastName).isNotNull();
        assertThat(byFirstNameAndLastName.getFirstName()).isEqualTo("Matt");
        assertThat(byFirstNameAndLastName.getLastName()).isEqualTo("Random");
    }

    @Test
    @DisplayName("JUnit test for verifying if an Native SQL method (Custom Native Query with named parameters) is correctly executed")
    void givenFirstNameAndLastName_whenFindByNativeSQLNamedParams_thenReturnEmployeeObject() {
        //given
        Employee empSaved = employeeRepository.save(employee);

        //when
        Employee byFirstNameAndLastName = employeeRepository.findByNativeSQNamedParams("Matt", "Random");

        //then
        assertThat(byFirstNameAndLastName).isNotNull();
        assertThat(byFirstNameAndLastName.getFirstName()).isEqualTo("Matt");
        assertThat(byFirstNameAndLastName.getLastName()).isEqualTo("Random");
    }
}