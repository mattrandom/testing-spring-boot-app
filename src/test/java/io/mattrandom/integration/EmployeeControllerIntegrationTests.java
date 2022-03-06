package io.mattrandom.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mattrandom.model.Employee;
import io.mattrandom.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static io.mattrandom.controller.ResponseBodyMatchers.responseBody;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
    }

    @Test
    @DisplayName("Testing POST method - integration testing")
    void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //given
        Employee employee = Employee.builder()
                /*
                 * Setting an ID due to '.andExpect(responseBody().containsObjectAsJson(employee, Employee.class))'
                 * method which uses '.usingRecursiveComparison()' method - it is recursively comparing fields' values
                 * If we left uninitialized 'id' field listed below then we would get an AssertionError:
                 * 'field/property 'id' differ:
                        - actual value  : 1L
                        - expected value: null'
                 */
                .id(1L)
                .firstName("Matt")
                .lastName("Random")
                .email("test@gmail.com")
                .build();

        //when
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())))
                .andExpect(responseBody().containsObjectAsJson(employee, Employee.class));
    }

    @Test
    @DisplayName("Testing GET all method - integration testing")
    void givenEmployeeList_whenGetEmployees_thenReturnEmployeeList() throws Exception {
        //given
        Employee employee = Employee.builder()
                .firstName("Matt")
                .lastName("Random")
                .email("test@gmail.com")
                .build();

        Employee employee2 = Employee.builder()
                .firstName("Matheus")
                .lastName("Siegmund")
                .email("ok@gmail.com")
                .build();

        List<Employee> employees = Arrays.asList(employee, employee2);
        employeeRepository.saveAll(employees);

        //when
        ResultActions responseGet = mockMvc.perform(get("/api/employees"));

        //then
        responseGet.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(employees.size())))
                .andExpect(jsonPath("$.[0].firstName", is(employees.get(0).getFirstName())));
    }

    @Test
    @DisplayName("Testing GET all method - integration testing - alternative version")
    void givenEmployeeList_whenGetEmployees_thenReturnEmployeeLis_ALTERNATIVE() throws Exception {
        //given
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("Matt")
                .lastName("Random")
                .email("test@gmail.com")
                .build();

        Employee employee2 = Employee.builder()
                .id(2L)
                .firstName("Matheus")
                .lastName("Siegmund")
                .email("ok@gmail.com")
                .build();

        //when
        ResultActions employeePost = mockMvc.perform(post("/api/employees")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        ResultActions employee2Post = mockMvc.perform(post("/api/employees")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee2)));

        ResultActions responseGet = mockMvc.perform(get("/api/employees"));

        List<Employee> employees = employeeRepository.findAll();

        //then
        responseGet.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(employees.size())))
                .andExpect(jsonPath("$.[0].firstName", is(employees.get(0).getFirstName())));

        employeePost.andExpect(responseBody().containsObjectAsJson(employee, Employee.class));

        employee2Post.andExpect(responseBody().containsObjectAsJson(employee2, Employee.class));
    }


}
