package za.ac.cput.redesigned_store_inventory_manager.domain;

/* Inventory.java
Inventory class
Author: Jayden Avontuur (222032278)
Date: 19 March 2026*/

import jakarta.persistence.*;
import java.time.LocalDate;
import za.ac.cput.redesigned_store_inventory_manager.util.Helper;

@Entity
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventoryId;

    @Embedded
    private Category category;

    @ManyToOne
    private Product product;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Supplier supplier;

    private int quantityInStock;
    private double unitPrice;
    private String location;

    protected Inventory() {}

    private Inventory(Builder builder) {
        this.inventoryId = builder.inventoryId;
        this.category = builder.category;
        this.product = builder.product;
        this.supplier = builder.supplier;
        this.quantityInStock = builder.quantityInStock;
        this.unitPrice = builder.unitPrice;
        this.location = builder.location;
    }

    public Long getInventoryId() { return inventoryId; }
    public Category getCategory() { return category; }
    public Product getProduct() { return product; }
    public Supplier getSupplier() { return supplier; }
    public int getQuantityInStock() { return quantityInStock; }
    public double getUnitPrice() { return unitPrice; }
    public String getLocation() { return location; }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "inventoryId=" + inventoryId +
                ", category=" + category +
                ", product=" + product +
                ", supplier=" + supplier +
                ", quantityInStock=" + quantityInStock +
                ", unitPrice=" + unitPrice +
                ", location='" + location + '\'' +
                '}';
    }

    public static class Builder {

        private Long inventoryId;
        private Product product;
        private Category category;
        private Supplier supplier;
        private int quantityInStock;
        private double unitPrice;
        private String location;

        public Builder(Long inventoryId) {
            this.inventoryId = inventoryId;
        }

        public Builder category(Category category) {
            this.category = category;
            return this;
        }

        public Builder product(Product product) {
            this.product = product;
            return this;
        }

        public Builder supplier(Supplier supplier) {
            this.supplier = supplier;
            return this;
        }

        public Builder quantityInStock(int quantityInStock) {
            this.quantityInStock = quantityInStock;
            return this;
        }


        public Builder unitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Inventory build() {
            return new Inventory(this);
        }
    }
}
