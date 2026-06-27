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

    @ManyToOne
    private Supplier supplier;

    private int quantityInStock;
    private int minimumStockLevel;
    private int maximumStockLevel;
    private double unitPrice;
    private LocalDate lastRestockedDate;
    private String location;

    protected Inventory() {}

    private Inventory(Builder builder) {
        this.inventoryId = builder.inventoryId;
        this.category = builder.category;
        this.product = builder.product;
        this.supplier = builder.supplier;
        this.quantityInStock = builder.quantityInStock;
        this.minimumStockLevel = builder.minimumStockLevel;
        this.maximumStockLevel = builder.maximumStockLevel;
        this.unitPrice = builder.unitPrice;
        this.lastRestockedDate = builder.lastRestockedDate;
        this.location = builder.location;
    }

    public Long getInventoryId() { return inventoryId; }
    public Category getCategory() { return category; }
    public Product getProduct() { return product; }
    public Supplier getSupplier() { return supplier; }
    public int getQuantityInStock() { return quantityInStock; }
    public int getMinimumStockLevel() { return minimumStockLevel; }
    public int getMaximumStockLevel() { return maximumStockLevel; }
    public double getUnitPrice() { return unitPrice; }
    public LocalDate getLastRestockedDate() { return lastRestockedDate; }
    public String getLocation() { return location; }

    @Override
    public String toString() {
        return "Inventory{" +
                "inventoryId=" + inventoryId +
                ", category=" + category +
                ", product=" + product +
                ", supplier=" + supplier +
                ", quantityInStock=" + quantityInStock +
                ", minimumStockLevel=" + minimumStockLevel +
                ", maximumStockLevel=" + maximumStockLevel +
                ", unitPrice=" + unitPrice +
                ", lastRestockedDate=" + lastRestockedDate +
                ", location='" + location + '\'' +
                '}';
    }

    public static class Builder {

        private Long inventoryId;
        private Product product;
        private Category category;
        private Supplier supplier;
        private int quantityInStock;
        private int minimumStockLevel;
        private int maximumStockLevel;
        private double unitPrice;
        private LocalDate lastRestockedDate;
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

        public Builder minimumStockLevel(int minimumStockLevel) {
            this.minimumStockLevel = minimumStockLevel;
            return this;
        }

        public Builder maximumStockLevel(int maximumStockLevel) {
            this.maximumStockLevel = maximumStockLevel;
            return this;
        }

        public Builder unitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }

        public Builder lastRestockedDate(LocalDate date) {
            this.lastRestockedDate = date;
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
