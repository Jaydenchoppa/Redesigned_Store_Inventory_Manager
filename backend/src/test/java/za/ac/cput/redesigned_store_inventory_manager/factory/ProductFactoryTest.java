/* Product.java
ProductFactoryTest
Author: Zacharia Dipudi (230700659)
Date: 28 June 2026*/
package za.ac.cput.redesigned_store_inventory_manager.factory;

import org.junit.jupiter.api.Test;
import za.ac.cput.redesigned_store_inventory_manager.domain.Product;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProductFactoryTest {
    private static Product product,product2;

    @Test
    void createProduct() {
        Product product = ProductFactory.createProduct("1234","Iphone17","R29000");
        Product product2 = ProductFactory.createProduct("","Iphone17","R29000");

        assertNotNull(product);
        System.out.println(product);
    }
    @Test
    void testProductThatFails(){
        assertNotNull(product2);
        System.out.println(product2);
    }
}

