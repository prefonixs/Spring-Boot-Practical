package com.sid.validation.dto;

import com.sid.validation.entity.Employee;
import com.sid.validation.entity.Employee.Department;
import com.sid.validation.entity.Employee.Gender;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeDTO {

    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    @Column(unique = true)
    private String phoneNumber;
    
    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Department is required")
    private Department department;
    
    @NotBlank(message = "Upload a file")
    private String fileLocation;
    
    private boolean healthInsurance;
    private boolean newsletter;
    private boolean workFromHome;
    
    public EmployeeDTO(Employee source) {
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
