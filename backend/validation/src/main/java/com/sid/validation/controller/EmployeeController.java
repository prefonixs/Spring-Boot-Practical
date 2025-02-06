package com.sid.validation.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sid.validation.dto.EmployeeDTO;
import com.sid.validation.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@GetMapping("/get-employees")
	public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
		List<EmployeeDTO> employees = employeeService.getAllEmployees();
		return ResponseEntity.ok(employees);
	}

	@GetMapping("/get-employees/{email}")
	public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable String email) {
		EmployeeDTO employee = employeeService.getEmployeeByEmail(email);
		return ResponseEntity.ok(employee);
	}

	@PatchMapping("/update-employees/{email}")
	public ResponseEntity<String> updateEmployee(@PathVariable String email, @RequestBody EmployeeDTO employeeDTO) {
		employeeService.updateEmployeeByEmail(email, employeeDTO);
		return ResponseEntity.ok("Employee Updated");
	}

	@PostMapping("/create-employees")
	public ResponseEntity<String> saveEmployee(@Valid @RequestPart("employeeDTO") EmployeeDTO employeeDTO,
			@RequestPart("file") MultipartFile file) throws IllegalStateException, IOException {
		employeeService.saveEmployee(employeeDTO, file);
		return ResponseEntity.status(201).body("Employee Added");
	}

	@DeleteMapping("/delete-employees/{email}")
	public ResponseEntity<String> deleteEmployee(@PathVariable String email) {
		System.out.println(email);
		employeeService.deleteEmployee(email);
		return ResponseEntity.status(204).body("Employee Deleted");
	}
	
	@GetMapping("/files/{filename}")
	public ResponseEntity<InputStreamResource> getFile(@PathVariable String filename) throws FileNotFoundException{
	    File file = employeeService.getFile(filename);
	    
	    HttpHeaders headers = new HttpHeaders();      
        headers.add("content-disposition", "inline;filename=" +filename);
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:3000");
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, OPTIONS");
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
        
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .body(resource);
	}
}
