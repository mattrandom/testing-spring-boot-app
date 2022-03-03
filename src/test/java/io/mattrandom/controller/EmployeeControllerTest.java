package io.mattrandom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mattrandom.model.Employee;
import io.mattrandom.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class EmployeeControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;


    @Test
    @DisplayName("Testing POST method")
    void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //given
        Employee employee = Employee.builder()
                .firstName("Matt")
                .lastName("Random")
                .email("test@gmail.com")
                .build();
        given(employeeService.saveEmployee(any(Employee.class))).willAnswer(invocation -> invocation.getArgument(0));

        //when
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())))
                .andExpect(ResponseBodyMatchers.responseBody().containsObjectAsJson(employee, Employee.class));;
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

        given(employeeService.getAllEmployees()).willReturn(employees);

        //when
        ResultActions response = mockMvc.perform(get("/api/employees"));

        //then
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(employees.size())));
    }

}