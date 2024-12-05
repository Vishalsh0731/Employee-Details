package com.test.EmployeeDetailsTaxDeduction.service;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import com.test.EmployeeDetailsTaxDeduction.model.Employee;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final Map<String, Employee> employeeDatabase = new HashMap<>();

    // Add Employee to Database
    public void addEmployee(Employee employee) {
        if (employeeDatabase.containsKey(employee.getEmployeeId())) {
            throw new IllegalArgumentException("Employee ID already exists");
        }
        employeeDatabase.put(employee.getEmployeeId(), employee);
    }

    // Calculate Tax Details for all Employees
    public List<Map<String, Object>> calculateTax() {
        List<Map<String, Object>> taxDetails = new ArrayList<>();

        for (Employee employee : employeeDatabase.values()) {
            Map<String, Object> taxData = new HashMap<>();
            double monthlySalary = employee.getSalary();
            LocalDate doj = employee.getDateOfJoining();
            LocalDate today = LocalDate.now();

            // Calculate total months worked
            int monthsWorked = (int) ChronoUnit.MONTHS.between(
                doj.withDayOfMonth(1),
                today.withDayOfMonth(1)
            );
            monthsWorked = Math.max(0, monthsWorked); // Ensure non-negative months worked

            // Total salary calculation (excluding partial months)
            double totalSalary = monthlySalary * monthsWorked;

            // Exclude partial month if necessary
            if (doj.getDayOfMonth() > 1) {
                totalSalary -= ((monthlySalary / 30) * (doj.getDayOfMonth() - 1));
            }

            // Yearly salary calculation
            double yearlySalary = totalSalary;

            // Tax Calculation
            double tax = 0.0;

            // Tax Slabs:
            if (yearlySalary > 250000) {
                double taxable = Math.min(250000, yearlySalary - 250000);
                tax += taxable * 0.05;
            }
            if (yearlySalary > 500000) {
                double taxable = Math.min(500000, yearlySalary - 500000);
                tax += taxable * 0.10;
            }
            if (yearlySalary > 1000000) {
                tax += (yearlySalary - 1000000) * 0.20;
            }

            // Cess Calculation for income above 2.5 million
            double cess = 0.0;
            if (yearlySalary > 2500000) {
                cess += (yearlySalary - 2500000) * 0.02;
            }

            // Add calculated details to response map
            taxData.put("EmployeeID", employee.getEmployeeId());
            taxData.put("FirstName", employee.getFirstName());
            taxData.put("LastName", employee.getLastName());
            taxData.put("YearlySalary", yearlySalary);
            taxData.put("TaxAmount", tax);
            taxData.put("CessAmount", cess);

            taxDetails.add(taxData);
        }

        return taxDetails;
    }
}
