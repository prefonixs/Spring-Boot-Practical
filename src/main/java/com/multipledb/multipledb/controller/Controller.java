package com.multipledb.multipledb.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.multipledb.multipledb.employee.entity.Employee;
import com.multipledb.multipledb.employee.service.EmployeeService;
import com.multipledb.multipledb.salary.entity.Salary;
import com.multipledb.multipledb.salary.service.SalaryService;

@RestController
public class Controller {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private SalaryService salaryService;

	@GetMapping("/employee")
	public List<Employee> retriveAllEmployees() {
		return employeeService.getAllEmployees();
	}

	@GetMapping("/employee/{id}")
	public Employee retriveEmployeeById(@PathVariable int id) {
		return employeeService.getEmployeeById(id);
	}

	@PostMapping("/employee")
	public void addUser(@RequestBody Employee employee) {
		employeeService.addEmployee(employee);
	}

	@GetMapping("/employee/{eid}/salary")
	public List<Salary> retriveAllSalaryForUser(@PathVariable int eid) {
		return salaryService.getAllSalariesForUser(eid);
	}

	@PostMapping("/employee/{eid}/salary")
	public void addSalary(@RequestBody Salary salary, @PathVariable int eid) {
		if (retriveEmployeeById(eid) != null) {
			salaryService.addSalary(eid, salary.getAmount());
		}
	}

	@GetMapping("/employee/salary")
	public List<EmployeeSalary> retriveAllUserAndSalary() {
		List<Employee> employees=employeeService.getAllEmployees();
		List<EmployeeSalary> employeeSalaries=new ArrayList<>();
		for (Employee employee : employees) {
			employeeSalaries.add(new EmployeeSalary(employee,salaryService.getAllSalariesForUser(employee.getId())));
		}
		return employeeSalaries; 
	}
}
