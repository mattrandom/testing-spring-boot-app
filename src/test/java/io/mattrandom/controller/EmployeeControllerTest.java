package io.mattrandom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mattrandom.model.Employee;
import io.mattrandom.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static io.mattrandom.controller.ResponseBodyMatchers.responseBody;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class EmployeeControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeServiceMockBean;


    @Test
    @DisplayName("Testing POST method")
    void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //given
        Employee employee = Employee.builder()
                .firstName("Matt")
                .lastName("Random")
                .email("test@gmail.com")
                .build();
        given(employeeServiceMockBean.saveEmployee(any(Employee.class))).willAnswer(invocation -> invocation.getArgument(0));

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
    @DisplayName("Testing GET all method")
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

        given(employeeServiceMockBean.getAllEmployees()).willReturn(employees);

        //when
        ResultActions response = mockMvc.perform(get("/api/employees"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(employees.size())))
                .andExpect(jsonPath("$.[0].firstName", is(employees.get(0).getFirstName())));
    }

    @Test
    @DisplayName("Testing GET by id method - positive scenario")
    void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        //given
        Long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Matt")
                .lastName("Random")
                .email("test@gmail.com")
                .build();
        given(employeeServiceMockBean.getEmployeeByIdOptional(employeeId)).willReturn(Optional.of(employee));

        //when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @Test
    @DisplayName("Testing GET by id method - negative scenario")
    void givenInvalidEmployeeId_whenGetEmployeeById_thenNotFound() throws Exception {
        //given
        Long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Matt")
                .lastName("Random")
                .email("test@gmail.com")
                .build();
        given(employeeServiceMockBean.getEmployeeByIdOptional(employeeId)).willReturn(Optional.empty());

        //when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //then
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Testing PUT method - positive scenario")
    void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {
        //given
        Employee employee = Employee.builder()
                .firstName("Matt")
                .lastName("Random")
                .email("test@gmail.com")
                .build();

        Employee employeeRequest = Employee.builder()
                .firstName("Request")
                .lastName("Request")
                .email("request@gmail.com")
                .build();
        given(employeeServiceMockBean.getEmployeeByIdOptional(1L)).willReturn(Optional.of(employee));
        given(employeeServiceMockBean.updateEmployee(any(Employee.class))).willAnswer(invocation -> invocation.getArgument(0));

        //when
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", 1L)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeRequest)));

        //then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employeeRequest.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employeeRequest.getLastName())))
                .andExpect(jsonPath("$.email", is(employeeRequest.getEmail())))
                .andExpect(responseBody().containsObjectAsJson(employeeRequest, Employee.class));

        then(employeeServiceMockBean).should(times(1)).updateEmployee(any(Employee.class));
    }

    @Test
    @DisplayName("Testing PUT method - negative scenario")
    void givenUpdatedEmployee_whenUpdateEmployee_thenNotFound() throws Exception {
        //given
        Employee employee = Employee.builder()
                .firstName("Matt")
                .lastName("Random")
                .email("test@gmail.com")
                .build();

        Employee employeeRequest = Employee.builder()
                .firstName("Request")
                .lastName("Request")
                .email("request@gmail.com")
                .build();
        given(employeeServiceMockBean.getEmployeeByIdOptional(1L)).willReturn(Optional.empty());
        given(employeeServiceMockBean.updateEmployee(any(Employee.class))).willAnswer(invocation -> invocation.getArgument(0));

        //when
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", 1L)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeRequest)));

        //then
        response.andDo(print())
                .andExpect(status().isNotFound());

        then(employeeServiceMockBean).should(times(0)).updateEmployee(any(Employee.class));
    }


    @Test
    @DisplayName("Testing DELETE method")
    void givenEmployeeId_whenDeleteEmployee_thenNoContent() throws Exception {
        //given
        willDoNothing().given(employeeServiceMockBean).deleteEmployee(anyLong());

        //when
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", anyLong()));

        //then
        response.andDo(print())
                .andExpect(status().isNoContent());
    }
}