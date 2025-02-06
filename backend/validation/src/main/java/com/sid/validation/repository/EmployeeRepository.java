package com.sid.validation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sid.validation.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{

	boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
    
    void deleteByEmail(String email);
    
    Employee findByEmail(String email);
}
