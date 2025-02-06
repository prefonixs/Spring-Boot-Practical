package com.multipledb.multipledb.salary.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.multipledb.multipledb.salary.entity.Salary;

public interface SalaryRepository extends JpaRepository<Salary, Integer>{

	public List<Salary> findByEmployeeId(int eid);
}
