package za.ac.cput.redesigned_store_inventory_manager.domain;

/* Category.java
Category class
Author: Jayden Avontuur (222032278)
Date: 21 June 2026*/

import jakarta.persistence.Embeddable;

@Embeddable
public class Category {

    private String name;
    private String description;

    protected Category() {}

    private Category(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public static class Builder {

        private String name;
        private String description;

        public Builder(String name) {
            this.name = name;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Category build() {

            if (name == null || name.isEmpty()) return null;

            return new Category(this);
        }
    }
}
