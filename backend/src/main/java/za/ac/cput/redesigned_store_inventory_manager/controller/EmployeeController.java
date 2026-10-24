package za.ac.cput.redesigned_store_inventory_manager.controller;

/*
 EmployeeController.java
 Employee Controller class
 Author: Templeton Liyabona Dyantyi (222623047)
 Date: 19 July 2026
*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.redesigned_store_inventory_manager.domain.Employee;
import za.ac.cput.redesigned_store_inventory_manager.service.EmployeeService;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeController(EmployeeService employeeService, PasswordEncoder passwordEncoder) {
        this.employeeService = employeeService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/getAll")
    public List<Employee> getAll() {
        return employeeService.findAll();
    }

    @PostMapping("/save")
    public Employee save(@RequestBody Employee employee) {
        String hashed = employee.getPassword() == null
                ? null
                : passwordEncoder.encode(employee.getPassword());

        Employee toSave = new Employee.Builder()
                .copy(employee)
                .setId(null)
                .setPassword(hashed)
                .build();
        return employeeService.save(toSave);
    }

    @GetMapping("/read")
    public Employee read(@RequestParam Long id) {
        return employeeService.findById(id).orElse(null);
    }

    @PutMapping("/update")
    public Employee update(@RequestBody Employee employee) {
        Employee existing = employeeService.findById(employee.getId()).orElse(null);
        if (existing == null) {
            return null;
        }
        Employee updated = new Employee.Builder()
                .copy(employee)
                .setUsername(existing.getUsername())
                .setPassword(existing.getPassword())
                .build();
        return employeeService.save(updated);
    }
    @DeleteMapping("/delete")
    public void delete(@RequestParam Long id) {
        employeeService.deleteById(id);
    }
}
