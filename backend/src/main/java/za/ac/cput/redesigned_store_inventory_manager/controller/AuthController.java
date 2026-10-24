package za.ac.cput.redesigned_store_inventory_manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.redesigned_store_inventory_manager.domain.Employee;
import za.ac.cput.redesigned_store_inventory_manager.repository.CustomerRepository;
import za.ac.cput.redesigned_store_inventory_manager.repository.EmployeeRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(EmployeeRepository employeeRepository,
                          CustomerRepository customerRepository,
                          PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        if (employeeRepository.findByUsername(request.username).isPresent()
                || customerRepository.findByUsername(request.username).isPresent()) {
            return ResponseEntity.status(409).body("Username already exists");
        }

        Employee employee = new Employee.Builder()
                .setEmployeeName(request.username)
                .setPosition("Staff")
                .setSalary(0.0)
                .setUsername(request.username)
                .setPassword(passwordEncoder.encode(request.password))
                .build();

        employeeRepository.save(employee);
        return ResponseEntity.status(201).body("Registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(Authentication authentication) {
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        return ResponseEntity.ok(role);
    }

    public static class RegisterRequest {
        public String username;
        public String email;
        public String password;
    }
}