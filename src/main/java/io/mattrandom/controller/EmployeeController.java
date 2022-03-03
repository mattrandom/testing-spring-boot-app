package io.mattrandom.controller;

import io.mattrandom.model.Employee;
import io.mattrandom.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public Employee createEmployee(@RequestBody Employee employee) {
//        return employeeService.saveEmployee(employee);
//    }

    @PostMapping
    public ResponseEntity<Employee> createEmployeeResponseEntity(@RequestBody Employee employee) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.saveEmployee(employee));
    }
}
