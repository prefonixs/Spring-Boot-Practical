package com.sid.validation.entity;

import com.sid.validation.dto.EmployeeDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Employee {
	public enum Department {
		HR, IT, FINANCE, MARKETING
	}
	public enum Gender {
		MALE,FEMALE
	}

	@Id
	@GeneratedValue
	private Long id;
	private String name;
	
	@Column(unique = true)
	private String email;
	
	@Column(unique = true)
	private String phoneNumber;
	
	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Enumerated(EnumType.STRING)
	private Department department;
	
	private String fileLocation;
	
    private boolean healthInsurance;
    private boolean newsletter;
    private boolean workFromHome;

	
	public Employee(EmployeeDTO source) {
		this.name=source.getName();
		this.email=source.getEmail();
		this.phoneNumber=source.getPhoneNumber();
		this.gender=source.getGender();
		this.department=source.getDepartment();
		this.fileLocation=source.getFileLocation();
		this.healthInsurance=source.isHealthInsurance();
		this.newsletter=source.isNewsletter();
		this.workFromHome=source.isWorkFromHome();
	}
}
