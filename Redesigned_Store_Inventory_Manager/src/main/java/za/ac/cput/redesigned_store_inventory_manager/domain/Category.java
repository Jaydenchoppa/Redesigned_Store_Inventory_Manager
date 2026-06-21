package za.ac.cput.redesigned_store_inventory_manager.domain;

/* Category.java
Category class
Author: Jayden Avontuur (222032278)
Date: 21 June 2026*/

import jakarta.persistence.*;

@Embeddable
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String categoryId;
    private String name;
    private String description;

    protected Category() {
    }


    public String getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    private Category(Builder builder) {
        this.categoryId = builder.categoryId;
        this.name = builder.name;
        this.description = builder.description;
    }

    public static class Builder {

        private String categoryId;
        private String name;
        private String description;

        public Builder(String categoryId, String name) {
            this.categoryId = categoryId;
            this.name = name;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Category build() {
            return new Category(this);
        }
    }
}
