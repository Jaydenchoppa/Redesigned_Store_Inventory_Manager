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
                "Lex8Fridman@gmail.com","+27 84 797 2480", "28 Space road",
                "Cape Town","7599","South Africa") ;

        assertEquals("CUST001", customer.getCustomerId());
        assertEquals("Lex", customer.getFirstName());
        assertEquals("Fridman", customer.getSurname());
        assertEquals("Lex8Fridman@gmail.com", customer.getEmail());
        assertEquals("+27 84 797 2480", customer.getPhoneNumber());
        assertEquals("28 Space road", customer.getAddress());
        assertEquals("Cape Town", customer.getCity());
        assertEquals("7599", customer.getPostalCode());
        assertEquals("South Africa", customer.getCountry());

        assertNotNull(customer);

    }

    @Test
    public void factoryCreatesValidCustomerTest(){
        Customer customer = factory.createCustomer("CUST002","David","Du Plessis",
                "DavidDuPlessis@gmail.com","+27 79 604 8119", "12 Star road",
                "Cape Town","7401","South Africa");

        assertTrue(customer.getCustomerId().equals("CUST002"));
        assertNotNull(customer);
    }
}