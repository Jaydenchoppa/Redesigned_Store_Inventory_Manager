package za.ac.cput.redesigned_store_inventory_manager.domain;

/* Order.java
Order POJO
Author: Matthew Ferreira (230048870)
Date: 21 March 2026*/

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Order {
    @Id
    private String orderNum;
    private String customerId;
    private String orderDate;
    private String deliveryDate;
    private double totalAmount;
    private String status;
    private String item;

    protected Order() {}

    public String getOrderNum() {
        return orderNum;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public String getItem() {
        return item;
    }

    private Order(Builder builder) {
        this.orderNum = builder.orderNum;
        this.customerId = builder.customerId;
        this.orderDate = builder.orderDate;
        this.deliveryDate = builder.deliveryDate;
        this.totalAmount = builder.totalAmount;
        this.status = builder.status;
        this.item = builder.item;
    }

    public static class Builder {
        private String orderNum;
        private String customerId;
        private String orderDate;
        private String deliveryDate;
        private double totalAmount;
        private String status;
        private String item;

        public Builder setOrderNum(String orderNum) {
            this.orderNum = orderNum;
            return this;
        }

        public Builder setCustomerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder setOrderDate(String orderDate) {
            this.orderDate = orderDate;
            return this;
        }

        public Builder setDeliveryDate(String deliveryDate) {
            this.deliveryDate = deliveryDate;
            return this;
        }

        public Builder setTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Builder setStatus(String status) {
            this.status = status;
            return this;
        }

        public Builder setItem(String item) {
            this.item = item;
            return this;
        }

        public Builder copy(Order order) {
            this.orderNum = order.getOrderNum();
            this.customerId = order.getCustomerId();
            this.orderDate = order.getOrderDate();
            this.deliveryDate = order.getDeliveryDate();
            this.totalAmount = order.getTotalAmount();
            this.status = order.getStatus();
            this.item = order.getItem();
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
