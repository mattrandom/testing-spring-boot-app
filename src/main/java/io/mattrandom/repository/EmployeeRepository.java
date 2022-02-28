package io.mattrandom.repository;

import io.mattrandom.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    @Query("select e from Employee e where e.firstName = ?1 and e.lastName = ?2")
    Employee findByJPQL(String firstName, String lastName);

    @Query("select e from Employee e where e.firstName = :firstName and e.lastName = :lastName")
    Employee findByJPQLNamedParams(String firstName, String lastName);

    @Query(
            value = "select * from employees e where e.first_name=?1 and e.last_name=?2",
            nativeQuery = true
    )
    Employee findByNativeSQL(String firstName, String lastName);


    @Query(
            value = "select * from employees e where e.first_name=:firstName and e.last_name=:lastName",
            nativeQuery = true
    )
    Employee findByNativeSQNamedParams(String firstName, String lastName);
}