package za.ac.cput.redesigned_store_inventory_manager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import za.ac.cput.redesigned_store_inventory_manager.domain.Employee;
import za.ac.cput.redesigned_store_inventory_manager.repository.EmployeeRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final String ADMIN_EMPLOYEE_ID = "EMP-ADMIN";

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Employee existing = employeeRepository.findByUsername("admin").orElse(null);

        if (existing == null) {
            Employee admin = new Employee.Builder()
                    .setEmployeeId(ADMIN_EMPLOYEE_ID)
                    .setEmployeeName("Admin")
                    .setUsername("admin")
                    .setPassword(passwordEncoder.encode("admin123"))
                    .build();
            employeeRepository.save(admin);

        } else if (existing.getEmployeeId() == null || existing.getEmployeeId().isBlank()) {
            // admin was created before it had an employee ID - fill it in
            employeeRepository.save(new Employee.Builder()
                    .copy(existing)
                    .setEmployeeId(ADMIN_EMPLOYEE_ID)
                    .build());
        }
    }
}