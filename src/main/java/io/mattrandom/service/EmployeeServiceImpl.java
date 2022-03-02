package io.mattrandom.service;

import io.mattrandom.exception.ResourceNotFoundException;
import io.mattrandom.model.Employee;
import io.mattrandom.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Employee saveEmployee(Employee employee) {
        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());

        if (savedEmployee.isPresent()) {
            throw new ResourceNotFoundException("Employee already exist with given email: " + employee.getEmail());
        }

        return employeeRepository.save(employee);
    }
}
