package za.ac.cput.redesigned_store_inventory_manager.factory;

import za.ac.cput.redesigned_store_inventory_manager.domain.Category;

public class CategoryFactory {

    public static Category createCategory(String name, String description) {
        return new Category.Builder(name)
                .description(description)
                .build();
    }
}
