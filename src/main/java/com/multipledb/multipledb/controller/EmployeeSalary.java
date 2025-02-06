package com.multipledb.multipledb.controller;

import java.util.List;

import com.multipledb.multipledb.employee.entity.Employee;
import com.multipledb.multipledb.salary.entity.Salary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmployeeSalary {

	private int id;
	private String name;
	private List<Salary> salaries;
	
	public EmployeeSalary(Employee employee,List<Salary> salaries) {
		this.id=employee.getId();
		this.name=employee.getName();
		this.salaries=salaries;
	}
}
