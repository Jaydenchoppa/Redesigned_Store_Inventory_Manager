package za.ac.cput.redesigned_store_inventory_manager.factory;

import org.junit.jupiter.api.Test;
import za.ac.cput.redesigned_store_inventory_manager.domain.Category;

import static org.junit.jupiter.api.Assertions.*;

class CategoryFactoryTest {

    @Test
    void createCategory() {

        Category category = CategoryFactory.createCategory(
                "Electronics",
                "Devices and gadgets"
        );

        assertNotNull(category);
        assertEquals("Electronics", category.getName());
        assertEquals("Devices and gadgets", category.getDescription());
    }

    @Test
    void createCategory_InvalidName() {

        Category category = CategoryFactory.createCategory(
                "",
                "Invalid category"
        );

        assertNull(category);
    }
}