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
        String firstName = clean(request.firstName);
        String surname = clean(request.surname);
        String email = clean(request.email);
        String username = clean(request.username);

        if (firstName.isEmpty() || surname.isEmpty()) {
            return ResponseEntity.badRequest().body("First name and surname are required.");
        }
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            return ResponseEntity.badRequest().body("Please enter a valid email address.");
        }
        if (!username.matches("[A-Za-z0-9._-]{3,30}")) {
            return ResponseEntity.badRequest()
                    .body("Username must be 3-30 characters: letters, numbers, . _ or -");
        }
        if (request.password == null || request.password.length() < 6) {
            return ResponseEntity.badRequest().body("Password must be at least 6 characters.");
        }
        if (customerRepository.findByUsername(username).isPresent()
                || employeeRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.status(409).body("Username already exists");
        }
        Customer customer = new Customer.Builder()
                .setCustomerId(UUID.randomUUID().toString())
                .setFirstName(firstName)
                .setSurname(surname)
                .setEmail(email)
                .setUsername(username)
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

    private static String clean(String s) {
        return s == null ? "" : s.trim();
    }

    public static class RegisterRequest {
        public String firstName, surname, email, username, password;
    }
}