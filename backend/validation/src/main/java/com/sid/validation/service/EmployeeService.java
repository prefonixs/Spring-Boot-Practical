package com.sid.validation.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sid.validation.dto.EmployeeDTO;
import com.sid.validation.entity.Employee;
import com.sid.validation.repository.EmployeeRepository;

import jakarta.transaction.Transactional;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;
	String uploadDirPath = "E:\\Spring Boot Test\\full stk validation\\uploads";

	public void saveEmployee(EmployeeDTO employeeDTO, MultipartFile file) throws IllegalStateException, IOException {
		if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
			throw new RuntimeException("Email is already taken.");
		}

		if (employeeRepository.existsByPhoneNumber(employeeDTO.getPhoneNumber())) {
			throw new RuntimeException("Phone number is already taken.");
		}

//		File uploadDir = new File(uploadDirPath);
//		if (!uploadDir.exists()) {
//			uploadDir.mkdirs();
//		}
		Employee employee = new Employee(employeeDTO);
//		System.out.println(employee.getFileLocation());
		Employee emp= employeeRepository.save(employee);
		
		String originalFileName = file.getOriginalFilename();
		String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
//		String newFileName = employeeDTO.getEmail().split("@")[0] + "_" + employeeDTO.getEmail() + fileExtension;
		String newFileName = emp.getId()+fileExtension;

		String filePath = uploadDirPath + File.separator + newFileName;

		File testFile = new File(filePath);
		file.transferTo(testFile);

		employee.setFileLocation(newFileName);
		employeeRepository.save(employee);
	}

	public List<EmployeeDTO> getAllEmployees() {
		List<Employee> employees = employeeRepository.findAll();
		List<EmployeeDTO> emplyeesDtos = new ArrayList<EmployeeDTO>();
		employees.forEach(emp -> {
			emplyeesDtos.add(new EmployeeDTO(emp));
		});
		return emplyeesDtos;
	}

	@Transactional
	public void deleteEmployee(String email) {
		Employee employee = employeeRepository.findByEmail(email);
		if (employee != null) {
			File fileToDelete = getFile(employee.getFileLocation());
			if (fileToDelete.exists()) {
				boolean fileDeleted = fileToDelete.delete();
				if (!fileDeleted) {
					throw new RuntimeException("Failed to delete the file.");
				}
			}
		employeeRepository.deleteByEmail(email);
		} else {
	        throw new RuntimeException("Employee not found.");
	    }
	}

	public EmployeeDTO getEmployeeByEmail(String email) {
		return new EmployeeDTO(employeeRepository.findByEmail(email));
	}

	public void updateEmployeeByEmail(String email, EmployeeDTO employeeDTO) {
		Employee employee = employeeRepository.findByEmail(email);
		if (!employee.getEmail().equals(employeeDTO.getEmail())
				&& employeeRepository.existsByEmail(employeeDTO.getEmail())) {
			throw new RuntimeException("Email is already taken.");
		}

		if (!employee.getPhoneNumber().equals(employeeDTO.getPhoneNumber())
				&& employeeRepository.existsByPhoneNumber(employeeDTO.getPhoneNumber())) {
			throw new RuntimeException("Phone number is already taken.");
		}
		Employee employee2 = new Employee(employeeDTO);
		employee2.setId(employee.getId());
		employeeRepository.save(employee2);
	}

	public File getFile(String filename) {
		return new File(uploadDirPath +"\\"+ filename);
	}
}
