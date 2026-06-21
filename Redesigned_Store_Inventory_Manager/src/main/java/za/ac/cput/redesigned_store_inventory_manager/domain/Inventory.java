package za.ac.cput.redesigned_store_inventory_manager.domain;

/* Inventory.java
Inventory class
Author: Jayden Avontuur (222032278)
Date: 19 March 2026*/

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.function.Supplier;

@Entity
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String inventoryId;

    @Embedded
    private Category category;

    private int quantityInStock;
    private int minimumStockLevel;
    private int maximumStockLevel;
    private double unitPrice;
    private LocalDate lastRestockedDate;
    private String location;

    //private Product product;
    //private Supplier supplier;

    protected Inventory(){

    }

    private Inventory(Builder builder) {
        this.inventoryId = builder.inventoryId;
        this.category = builder.category;
        this.quantityInStock = builder.quantityInStock;
        this.minimumStockLevel = builder.minimumStockLevel;
        this.maximumStockLevel = builder.maximumStockLevel;
        this.unitPrice = builder.unitPrice;
        this.lastRestockedDate = builder.lastRestockedDate;
        this.location = builder.location;

       // this.product = builder.product;
        //this.supplier = builder.supplier;
    }

    public String getInventoryId() {
        return inventoryId;
    }

   // public Product getProduct() {
   //     return product;
   // }

    public Category getCategory() {
        return category;
    }

   // public Supplier getSupplier() {
    //    return supplier;
   // }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public int getMinimumStockLevel() {
        return minimumStockLevel;
    }

    public int getMaximumStockLevel() {
        return maximumStockLevel;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public LocalDate getLastRestockedDate() {
        return lastRestockedDate;
    }

    public String getLocation() {
        return location;
    }

    public static class Builder {
        private String inventoryId;
        Product product;
        private Category category;
        Supplier supplier;
        private int quantityInStock;
        private int minimumStockLevel;
        private int maximumStockLevel;
        private double unitPrice;
        private LocalDate lastRestockedDate;
        private String location;

        public Builder(String inventoryId, Product product) {
            this.inventoryId = inventoryId;
            this.product = product;
        }

        public Builder setInventoryId(String inventoryId) {
            this.inventoryId = inventoryId;
            return this;
        }

        public Builder setCategory(Category category) {
            this.category = category;
            return this;
        }

        public Builder setSupplier(Supplier supplier) {
            this.supplier = supplier;
            return this;
        }

        public Builder setQuantityInStock(int quantityInStock) {
            this.quantityInStock = quantityInStock;
            return this;
        }

        public Builder setMinimumStockLevel(int minimumStockLevel) {
            this.minimumStockLevel = minimumStockLevel;
            return this;
        }

        public Builder setMaximumStockLevel(int maximumStockLevel) {
            this.maximumStockLevel = maximumStockLevel;
            return this;
        }

        public Builder setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }

        public Builder setLastRestockedDate(LocalDate date) {
            this.lastRestockedDate = date;
            return this;
        }

        public Builder setLocation(String location) {
            this.location = location;
            return this;
        }

        public Inventory build() {
            return new Inventory(this);
        }
    }
}
