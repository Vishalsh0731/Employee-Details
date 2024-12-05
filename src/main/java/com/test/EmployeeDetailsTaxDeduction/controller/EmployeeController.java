package com.test.EmployeeDetailsTaxDeduction.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import com.test.EmployeeDetailsTaxDeduction.service.EmployeeService;
import com.test.EmployeeDetailsTaxDeduction.model.Employee;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
	 private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<String> addEmployee(@RequestBody Employee employee) {
        try {
            employeeService.addEmployee(employee);
            return ResponseEntity.status(HttpStatus.CREATED).body("Employee added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/tax")
    public ResponseEntity<List<Map<String, Object>>> calculateTax() {
        return ResponseEntity.ok(employeeService.calculateTax());
    }
}

