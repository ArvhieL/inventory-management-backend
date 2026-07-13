package com.inventory.manager.controller;

import com.inventory.manager.model.Employee;
import com.inventory.manager.model.EmploymentType;
import com.inventory.manager.model.LaborHistory;
import com.inventory.manager.repository.EmployeeRepository;
import com.inventory.manager.repository.LaborHistoryRepository;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")

public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final LaborHistoryRepository laborHistoryRepository;

    public EmployeeController(EmployeeRepository employeeRepository, LaborHistoryRepository laborHistoryRepository) {
        this.employeeRepository = employeeRepository;
        this.laborHistoryRepository = laborHistoryRepository;
    }


    @PostMapping
    public Employee hireEmployee(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    // THE PAYROLL ENDPOINT
    @GetMapping("/{id}/paycheck")
    public Map<String, Object> calculatePaycheck(@PathVariable Long id) {
        // 1. Find the mechanic
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found!"));

        // 2. Fetch all their timesheets
        List<LaborHistory> timesheets = laborHistoryRepository.findByEmployeeId(id);

        // 3. Calculate total labor revenue TODAY
        BigDecimal totalLaborRevenue = BigDecimal.ZERO;
        LocalDate today = LocalDate.now();

        for (LaborHistory sheet : timesheets) {
            // Check if the timesheet belongs to an order, and if that order was completed today
            if (sheet.getOrder() != null && sheet.getOrder().getDate() != null) {
                LocalDate orderDate = sheet.getOrder().getDate().toLocalDate();

                if (orderDate.equals(today)) {
                    // flat fee billed to the customer for this specific repair
                    BigDecimal jobRevenue = sheet.getLaborCharge();
                    totalLaborRevenue = totalLaborRevenue.add(jobRevenue);
                }
            }
        }

        // 4. Calculate labor percent
        BigDecimal commissionPay = totalLaborRevenue.multiply(employee.getLaborPercentage());
        BigDecimal finalPay = commissionPay;

        // 5. Daily rate added on top
        if (employee.getEmploymentType() == EmploymentType.REGULAR) {
            if (employee.getDailyRate() != null) {
                finalPay = finalPay.add(employee.getDailyRate());
            }
        }

        // Return JSON response report
        return Map.of(
                "mechanicName", employee.getFirstName() + " " + employee.getLastName(),
                "employmentType", employee.getEmploymentType(),
                "dateCalculated", today.toString(),
                "totalLaborRevenueToday", totalLaborRevenue,
                "commissionEarned", commissionPay,
                "finalTakeHomePay", finalPay
        );
    }
}