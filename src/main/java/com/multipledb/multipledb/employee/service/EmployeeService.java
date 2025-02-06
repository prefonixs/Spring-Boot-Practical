package com.multipledb.multipledb.employee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multipledb.multipledb.employee.entity.Employee;
import com.multipledb.multipledb.employee.repository.EmployeeRepository;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
    
    public void addEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

	public Employee getEmployeeById(int id) {
		return employeeRepository.findById(id).get();
	}
}