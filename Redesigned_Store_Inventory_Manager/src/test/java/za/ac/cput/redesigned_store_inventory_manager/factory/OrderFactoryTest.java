package za.ac.cput.redesigned_store_inventory_manager.factory;

/* OrderFactoryTest.java
OrderFactoryTest class
Author: Matthew Ferreira (230048870)
Date: 28 June 2026*/

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import za.ac.cput.redesigned_store_inventory_manager.domain.Order;

import static org.junit.jupiter.api.Assertions.*;

public class OrderFactoryTest {
    private Order order;
    private OrderFactory factory;

    @BeforeEach
    public void setup(){
        factory = new OrderFactory();
    }

    @Test
    public void createOrderAllFieldsTest(){
        Order order = factory.createOrder("ORD001","CUST001","1/Jan/2026",
                "6/Jan/2026",10000.0, "Delivered",
                "Signed Baseball bat MLB");

        assertEquals("ORD001", order.getOrderNum());
        assertEquals("CUST001", order.getCustomerId());
        assertEquals("1/Jan/2026", order.getOrderDate());
        assertEquals("6/Jan/2026", order.getDeliveryDate());
        assertEquals(10000.0, order.getTotalAmount());
        assertEquals("Delivered", order.getStatus());
        assertEquals("Signed Baseball bat MLB", order.getItem());

        assertNotNull(order);
    }

    @Test
    public void factoryCreatesValidCustomerTest(){
        Order order = factory.createOrder("ORD002","CUST002","10/Mar/2026",
                "13/Mar/2026",1000.0, "Delivered",
                "Signed Baseball bat MLB");

        assertTrue(order.getOrderNum().equals("ORD002"));
        assertNotNull(order);
    }
}