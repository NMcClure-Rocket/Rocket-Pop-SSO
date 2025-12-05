package com.example.rocketpop.repository;

import com.example.rocketpop.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    
    Optional<Employee> findByEmail(String email);
    
    Optional<Employee> findById(Integer id);
}
