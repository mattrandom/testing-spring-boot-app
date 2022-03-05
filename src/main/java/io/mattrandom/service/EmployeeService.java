package io.mattrandom.service;

import io.mattrandom.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    Employee saveEmployee(Employee employee);

    List<Employee> getAllEmployees();

    Employee getEmployeeById(Long id);

    Optional<Employee> getEmployeeByIdOptional(Long id);

    Employee updateEmployee(Employee employeeRequest);

    void deleteEmployee(Long id);
}
