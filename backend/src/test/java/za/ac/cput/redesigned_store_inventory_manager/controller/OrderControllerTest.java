package za.ac.cput.redesigned_store_inventory_manager.controller;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.http.ResponseEntity;
import za.ac.cput.redesigned_store_inventory_manager.domain.Order;
import za.ac.cput.redesigned_store_inventory_manager.service.IOrderService;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/* OrderControllerTest.java
OrderController unit tests
Author: Matthew Ferreira (230048870)
Date: 17 August 2026*/

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {
    @Mock
    private IOrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private Order order;

    @BeforeEach
    public void setUp() {
        order = new Order.Builder()
                .setOrderNum("ORD001")
                .setOrderDate("18/9/26")
                .setTotalAmount(100.00)
                .setStatus("Pending")
                .setItem("ITEM001")
                .build();
    }

    @Test
    public void getAllOrdersTest() {
        when(orderService.findAll()).thenReturn(List.of(order));

        ResponseEntity<List<Order>> response = orderController.getAllOrders();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(orderService, times(1)).findAll();
    }

    @Test
    public void getOrderByIdFoundTest() {
        when(orderService.findById("ORD001")).thenReturn(Optional.of(order));

        ResponseEntity<Order> response = orderController.getOrderById("ORD001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ORD001", response.getBody().getOrderNum());
    }

    @Test
    public void createOrderTest() {
        when(orderService.save(order)).thenReturn(order);
        ResponseEntity<Order> response = orderController.createOrder(order);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("ORD001", response.getBody().getOrderNum());
        verify(orderService, times(1)).save(order);
    }

    @Test
    public void getOrderByIdNotFoundTest() {
        when(orderService.findById("ORD099")).thenReturn(Optional.empty());

        ResponseEntity<Order> response = orderController.getOrderById("ORD099");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void createOrderFailsTest() {
        when(orderService.save(order)).thenReturn(null);

        ResponseEntity<Order> response = orderController.createOrder(order);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void deleteByOrderNotFoundTest() {
        when(orderService.existsById("ORD099")).thenReturn(false);

        ResponseEntity<Void> response = orderController.deleteOrder("ORD099");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(orderService, never()).deleteById(anyString());
    }

    @Test
    public void deleteOrderExistsTest(){
        when(orderService.existsById("ORD001")).thenReturn(true);

        ResponseEntity<Void> response = orderController.deleteOrder("ORD001");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(orderService, times(1)).deleteById("ORD001");
    }
}