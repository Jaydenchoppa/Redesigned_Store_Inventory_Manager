package za.ac.cput.redesigned_store_inventory_manager.controller;

/* CustomerControllerTest.java
CustomerController unit tests (Mockito)
Author: Matthew Ferreira (230048870)
Date: 17 August 2026*/

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.redesigned_store_inventory_manager.domain.Customer;
import za.ac.cput.redesigned_store_inventory_manager.service.ICustomerService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    @Mock
    private ICustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private Customer customer;

    @BeforeEach
    public void setup() {
        customer = new Customer.Builder()
                .setCustomerId("CUST001")
                .setFirstName("Lex")
                .setSurname("Fridman")
                .setEmail("Lex8Fridman@gmail.com")
                .build();
    }

    @Test
    public void getAllCustomersTest() {
        when(customerService.findAll()).thenReturn(List.of(customer));

        ResponseEntity<List<Customer>> response = customerController.getAllCustomers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(customerService, times(1)).findAll();
    }

    @Test
    public void getCustomerByIdFoundTest() {
        when(customerService.findById("CUST001")).thenReturn(Optional.of(customer));

        ResponseEntity<Customer> response = customerController.getCustomerById("CUST001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("CUST001", response.getBody().getCustomerId());
    }

    @Test
    public void getCustomerByIdNotFoundTest() {
        when(customerService.findById("CUST099")).thenReturn(Optional.empty());

        ResponseEntity<Customer> response = customerController.getCustomerById("CUST099");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void createCustomerTest() {
        when(customerService.save(customer)).thenReturn(customer);

        ResponseEntity<Customer> response = customerController.createCustomer(customer);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("CUST001", response.getBody().getCustomerId());
        verify(customerService, times(1)).save(customer);
    }

    @Test
    public void createCustomerFailsTest() {
        when(customerService.save(customer)).thenReturn(null);

        ResponseEntity<Customer> response = customerController.createCustomer(customer);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void deleteCustomerExistsTest() {
        when(customerService.existsById("CUST001")).thenReturn(true);

        ResponseEntity<Void> response = customerController.deleteCustomer("CUST001");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(customerService, times(1)).deleteById("CUST001");
    }

    @Test
    public void deleteCustomerNotFoundTest() {
        when(customerService.existsById("CUST099")).thenReturn(false);

        ResponseEntity<Void> response = customerController.deleteCustomer("CUST099");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(customerService, never()).deleteById(anyString());
    }
}