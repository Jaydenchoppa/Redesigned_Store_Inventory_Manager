package za.ac.cput.redesigned_store_inventory_manager.factory;

/* OrderFactory.java
Order Factory class
Author: Matthew Ferreira (230048870)
Date: 28 June 2026*/

import za.ac.cput.redesigned_store_inventory_manager.domain.Order;

public class OrderFactory {
    public static Order createOrder(
            String orderNum,
            String customerId,
            String orderDate,
            double totalAmount,
            String status,
            String item){
        return new Order.Builder()
                .setOrderNum(orderNum)
                .setCustomerId(customerId)
                .setOrderDate(orderDate)
                .setTotalAmount(totalAmount)
                .setStatus(status)
                .setItem(item)
                .build();
    }
}
