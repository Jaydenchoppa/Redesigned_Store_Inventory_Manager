package za.ac.cput.redesigned_store_inventory_manager.factory;

import org.junit.jupiter.api.Test;
import za.ac.cput.redesigned_store_inventory_manager.domain.Category;

import static org.junit.jupiter.api.Assertions.*;
/* CategoryFactoryTest.java
Category class
Author: Jayden Avontuur (222032278)
Date: 30 October 2026*/

class CategoryFactoryTest {

    @Test
    void createCategory() {

        Category category = CategoryFactory.createCategory("Electronics");

        assertNotNull(category);
        assertEquals("Electronics", category.getName());
    }

    @Test
    void createCategory_InvalidName() {

        Category category = CategoryFactory.createCategory("");

        assertNull(category);
    }
}