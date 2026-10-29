package za.ac.cput.redesigned_store_inventory_manager.factory;

/* CustomerFactoryTest.java
CustomerFactoryTest class
Author: Matthew Ferreira (230048870)
Date: 28 June 2026*/

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import za.ac.cput.redesigned_store_inventory_manager.domain.Customer;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerFactoryTest {
    private CustomerFactory factory;
    private Customer customer;

    @BeforeEach
    public void setup(){
        factory = new CustomerFactory();
    }

    @Test
    public void createCustomerAllFieldsTest(){
        Customer customer = factory.createCustomer("CUST001","Lex","Fridman",
                "Lex8Fridman@gmail.com","Lex", "Lex123") ;

        assertEquals("CUST001", customer.getCustomerId());
        assertEquals("Lex", customer.getFirstName());
        assertEquals("Fridman", customer.getSurname());
        assertEquals("Lex8Fridman@gmail.com", customer.getEmail());
        assertEquals("Lex", customer.getUsername());
        assertEquals("Lex123", customer.getPassword());

        assertNotNull(customer);

    }

    @Test
    public void factoryCreatesValidCustomerTest(){
        Customer customer = factory.createCustomer("CUST002","David","Du Plessis",
                "DavidDuPlessis@gmail.com", "davidduplessis", "password123");

        assertTrue(customer.getCustomerId().equals("CUST002"));
        assertNotNull(customer);
    }
}