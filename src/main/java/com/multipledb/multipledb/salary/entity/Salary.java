package com.multipledb.multipledb.salary.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "salary")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Salary {

	@Id
	@GeneratedValue
	private int id;

	private int employeeId;
	private double amount;
}
