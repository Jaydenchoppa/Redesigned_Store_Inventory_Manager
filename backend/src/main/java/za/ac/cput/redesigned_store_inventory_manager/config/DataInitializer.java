package za.ac.cput.redesigned_store_inventory_manager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import za.ac.cput.redesigned_store_inventory_manager.domain.Employee;
import za.ac.cput.redesigned_store_inventory_manager.repository.EmployeeRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (employeeRepository.findByUsername("admin").isEmpty()) {
            Employee admin = new Employee.Builder()
                    .setEmployeeName("Admin")
                    .setPosition("Manager")
                    .setSalary(0.0)
                    .setUsername("admin")
                    .setPassword(passwordEncoder.encode("admin123"))
                    .build();
            employeeRepository.save(admin);
        }
    }
}