package za.ac.cput.redesigned_store_inventory_manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import za.ac.cput.redesigned_store_inventory_manager.domain.Customer;
import za.ac.cput.redesigned_store_inventory_manager.domain.Employee;
import za.ac.cput.redesigned_store_inventory_manager.repository.CustomerRepository;
import za.ac.cput.redesigned_store_inventory_manager.repository.EmployeeRepository;

import java.util.Optional;

    @Service
    public class CustomUserDetailsService implements UserDetailsService {

        private final EmployeeRepository employeeRepository;
        private final CustomerRepository customerRepository;

        @Autowired
        public CustomUserDetailsService(EmployeeRepository employeeRepository,
                                        CustomerRepository customerRepository) {
            this.employeeRepository = employeeRepository;
            this.customerRepository = customerRepository;
        }

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Optional<Employee> employee = employeeRepository.findByUsername(username);
            if (employee.isPresent()) {
                return org.springframework.security.core.userdetails.User
                        .withUsername(employee.get().getUsername())
                        .password(employee.get().getPassword())
                        .roles("EMPLOYEE")
                        .build();
            }

            Customer customer = customerRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            return org.springframework.security.core.userdetails.User
                    .withUsername(customer.getUsername())
                    .password(customer.getPassword())
                    .roles("CUSTOMER")
                    .build();
        }
    }