package com.multipledb.multipledb.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.multipledb.multipledb.employee.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer>{

	public Employee findByName(String name);
}
