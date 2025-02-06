package com.multipledb.multipledb.salary.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.multipledb.multipledb.salary.entity.Salary;
import com.multipledb.multipledb.salary.repository.SalaryRepository;

@Service
public class SalaryService {

    @Autowired
    private SalaryRepository salaryRepository;

    public List<Salary> getAllSalaries() {
        return salaryRepository.findAll();
    }
    
    public List<Salary> getAllSalariesForUser(int eid) {
    	return salaryRepository.findByEmployeeId(eid);
    }
    
    public void addSalary(int eid,double amount) {
    	salaryRepository.save(new Salary(0,eid,amount));
    }
}
