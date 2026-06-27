package za.ac.cput.redesigned_store_inventory_manager.factory;

import org.junit.jupiter.api.Test;
import za.ac.cput.redesigned_store_inventory_manager.domain.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InventoryFactoryTest {

    @Test
    void createInventory() {

        Product product = new Product();
        Category category = CategoryFactory.createCategory(
                "Electronics",
                "Devices and gadgets"
        );
        Supplier supplier = new Supplier();

        Inventory inventory = InventoryFactory.createInventory(
                product,
                category,
                supplier,
                50,
                10,
                100,
                29.99,
                LocalDate.now(),
                "Warehouse A"
        );

        assertNotNull(inventory);
        assertEquals(50, inventory.getQuantityInStock());
        assertEquals("Electronics", inventory.getCategory().getName());
    }

    @Test
    void createInventory_InvalidQuantity() {

        Product product = new Product();
        Category category = CategoryFactory.createCategory("Electronics", "Devices");
        Supplier supplier = new Supplier();

        Inventory inventory = InventoryFactory.createInventory(
                product,
                category,
                supplier,
                -5, // invalid
                10,
                100,
                29.99,
                LocalDate.now(),
                "Warehouse A"
        );

        assertNull(inventory);
    }
}