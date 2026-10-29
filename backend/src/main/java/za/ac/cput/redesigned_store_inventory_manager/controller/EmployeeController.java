package za.ac.cput.redesigned_store_inventory_manager.controller;

/*
 EmployeeController.java
 Employee Controller class
 Author: Templeton Liyabona Dyantyi (222623047)
 Date: 19 July 2026
*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.redesigned_store_inventory_manager.domain.Employee;
import za.ac.cput.redesigned_store_inventory_manager.repository.CustomerRepository;
import za.ac.cput.redesigned_store_inventory_manager.repository.EmployeeRepository;
import za.ac.cput.redesigned_store_inventory_manager.service.EmployeeService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/employee")
public class EmployeeController {

    private static final String USERNAME_RULE = "[A-Za-z0-9._-]{3,30}";

    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeController(EmployeeService employeeService,
                              EmployeeRepository employeeRepository,
                              CustomerRepository customerRepository,
                              PasswordEncoder passwordEncoder) {
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/getAll")
    public List<Employee> getAll() {
        return employeeService.findAll();
    }

    /** Creates a login-capable employee. This is the single way employees are created. */
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Employee employee) {
        String username = clean(employee.getUsername());
        String name = clean(employee.getEmployeeName());
        String password = employee.getPassword();

        if (!username.matches(USERNAME_RULE)) {
            return ResponseEntity.badRequest()
                    .body("Username must be 3-30 characters: letters, numbers, . _ or -");
        }
        if (password == null || password.length() < 6) {
            return ResponseEntity.badRequest().body("Password must be at least 6 characters.");
        }
        if (name.isEmpty()) {
            return ResponseEntity.badRequest().body("Employee name is required.");
        }
        if (employeeRepository.findByUsername(username).isPresent()
                || customerRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.status(409).body("Username already exists");
        }

        String position = clean(employee.getPosition());
        String employeeId = clean(employee.getEmployeeId());

        Employee toSave = new Employee.Builder()
                .setEmployeeId(employeeId.isEmpty()
                        ? "EMP-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase()
                        : employeeId)
                .setEmployeeName(name)
                .setPosition(position.isEmpty() ? "Staff" : position)
                .setUsername(username)
                .setPassword(passwordEncoder.encode(password))
                .build();
        return ResponseEntity.status(201).body(employeeService.save(toSave));
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
    public ResponseEntity<String> delete(@RequestParam Long id, Authentication authentication) {
        Employee existing = employeeService.findById(id).orElse(null);
        if (existing == null) {
            return ResponseEntity.status(404).body("Employee not found");
        }
        if (existing.getUsername() != null && existing.getUsername().equals(authentication.getName())) {
            return ResponseEntity.status(409).body("You can't delete the account you are logged in with.");
        }
        employeeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private static String clean(String s) {
        return s == null ? "" : s.trim();
    }
}
