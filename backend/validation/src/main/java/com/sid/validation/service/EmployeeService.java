package com.sid.validation.service;

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

    @Autowired
    private S3Service s3Service;

    public void saveEmployee(EmployeeDTO employeeDTO, MultipartFile file) throws IOException {
        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new RuntimeException("Email is already taken.");
        }

        if (employeeRepository.existsByPhoneNumber(employeeDTO.getPhoneNumber())) {
            throw new RuntimeException("Phone number is already taken.");
        }

        Employee employee = new Employee(employeeDTO);
        Employee savedEmployee = employeeRepository.save(employee);

        String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String newFileName = savedEmployee.getId() + fileExtension;

        // Upload to S3
        String fileUrl = s3Service.uploadFile(file, newFileName);

        savedEmployee.setFileLocation(fileUrl);
        employeeRepository.save(savedEmployee);
    }

    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeDTO> employeeDTOs = new ArrayList<>();
        employees.forEach(emp -> employeeDTOs.add(new EmployeeDTO(emp)));
        return employeeDTOs;
    }

    @Transactional
    public void deleteEmployee(String email) {
        Employee employee = employeeRepository.findByEmail(email);
        if (employee != null) {
            s3Service.deleteFile(employee.getFileLocation());
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
        if (!employee.getEmail().equals(employeeDTO.getEmail()) && employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new RuntimeException("Email is already taken.");
        }
        if (!employee.getPhoneNumber().equals(employeeDTO.getPhoneNumber()) && employeeRepository.existsByPhoneNumber(employeeDTO.getPhoneNumber())) {
            throw new RuntimeException("Phone number is already taken.");
        }
        Employee updatedEmployee = new Employee(employeeDTO);
        updatedEmployee.setId(employee.getId());
        employeeRepository.save(updatedEmployee);
    }
}
