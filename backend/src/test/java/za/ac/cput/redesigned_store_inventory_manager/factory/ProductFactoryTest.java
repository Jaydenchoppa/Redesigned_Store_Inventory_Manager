/* Product.java
ProductFactoryTest
Author: Zacharia Dipudi (230700659)
Date: 28 June 2026*/
package za.ac.cput.redesigned_store_inventory_manager.factory;

import org.junit.jupiter.api.Test;
import za.ac.cput.redesigned_store_inventory_manager.domain.Product;

import static org.junit.jupiter.api.Assertions.*;

public class ProductFactoryTest {

    @Test
    void createProduct() {
        Product product = ProductFactory.createProduct("1234", "Iphone17");

        assertNotNull(product);
        assertEquals("1234", product.getProductId());
        assertEquals("Iphone17", product.getProductName());
        System.out.println(product);
    }

    @Test
    void testProductThatFails() {
        // a blank product ID is invalid, so the factory must refuse to create it
        Product product2 = ProductFactory.createProduct("", "Iphone17");

        assertNull(product2);
    }
}