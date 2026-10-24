package za.ac.cput.redesigned_store_inventory_manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.redesigned_store_inventory_manager.domain.Customer;
import za.ac.cput.redesigned_store_inventory_manager.repository.CustomerRepository;
import za.ac.cput.redesigned_store_inventory_manager.repository.EmployeeRepository;

import java.util.UUID;

@RestController
@RequestMapping("/api/customer-auth")
public class CustomerAuthController {

    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerAuthController(CustomerRepository customerRepository,
                                  EmployeeRepository employeeRepository,
                                  PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        if (customerRepository.findByUsername(request.username).isPresent()
                || employeeRepository.findByUsername(request.username).isPresent()) {
            return ResponseEntity.status(409).body("Username already exists");
        }
        Customer customer = new Customer.Builder()
                .setCustomerId(UUID.randomUUID().toString())
                .setFirstName(request.firstName)
                .setSurname(request.surname)
                .setEmail(request.email)
                .setUsername(request.username)
                .setPassword(passwordEncoder.encode(request.password))
                .build();
        customerRepository.save(customer);
        return ResponseEntity.status(201).body("Registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(Authentication authentication) {
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        return ResponseEntity.ok(role);
    }

    public static class RegisterRequest {
        public String firstName, surname, email, username, password;
    }
}