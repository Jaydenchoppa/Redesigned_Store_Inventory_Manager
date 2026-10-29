package za.ac.cput.redesigned_store_inventory_manager.factory;

/* Product.java
ProductFactory
Author: Zacharia Dipudi (230700659)
Date: 28 June 2026*/

import za.ac.cput.redesigned_store_inventory_manager.domain.Product;


public class ProductFactory {
    public static Product createProduct(String productId, String productName) {

        if (productId == null || productId.isBlank())
            return null;

        if (productName == null || productName.isBlank())
            return null;

        return new Product.Builder()
                .setProductId(productId)
                .setProductName(productName)
                .build();
    }
}