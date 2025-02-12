package com.sid.validation.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sid.validation.dto.EmployeeDTO;
import com.sid.validation.service.EmployeeService;
import com.sid.validation.service.S3Service;

import jakarta.validation.Valid;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private S3Service s3Service;

    @GetMapping("/get-employees")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/get-employees/{email}")
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable String email) {
        return ResponseEntity.ok(employeeService.getEmployeeByEmail(email));
    }

    @PatchMapping("/update-employees/{email}")
    public ResponseEntity<String> updateEmployee(@PathVariable String email, @RequestBody EmployeeDTO employeeDTO) {
        employeeService.updateEmployeeByEmail(email, employeeDTO);
        return ResponseEntity.ok("Employee Updated");
    }

    @PostMapping("/create-employees")
    public ResponseEntity<String> saveEmployee(@Valid @RequestPart("employeeDTO") EmployeeDTO employeeDTO,
                                               @RequestPart("file") MultipartFile file) throws IOException {
        employeeService.saveEmployee(employeeDTO, file);
        return ResponseEntity.status(201).body("Employee Added");
    }

    @DeleteMapping("/delete-employees/{email}")
    public ResponseEntity<String> deleteEmployee(@PathVariable String email) {
        employeeService.deleteEmployee(email);
        return ResponseEntity.status(204).body("Employee Deleted");
    }
}
