package za.ac.cput.redesigned_store_inventory_manager.controller;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.redesigned_store_inventory_manager.domain.Inventory;
import za.ac.cput.redesigned_store_inventory_manager.domain.Product;
import za.ac.cput.redesigned_store_inventory_manager.domain.Supplier;
import za.ac.cput.redesigned_store_inventory_manager.repository.ProductRepository;
import za.ac.cput.redesigned_store_inventory_manager.repository.SupplierRepository;
import za.ac.cput.redesigned_store_inventory_manager.service.IInventoryService;

import java.util.List;
import java.util.Optional;

/* InventoryController.java
Inventory Controller class
Author: Jayden Avontuur (222032278)
Date: 18 July 2026*/

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    private final IInventoryService inventoryService;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;

    @Autowired
    public InventoryController(IInventoryService inventoryService,
                               ProductRepository productRepository,
                               SupplierRepository supplierRepository) {
        this.inventoryService = inventoryService;
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
    }

    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventory() {
        List<Inventory> inventory = inventoryService.findAll();
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable Long id) {
        Optional<Inventory> inventory = inventoryService.findById(id);
        return inventory.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Inventory> createInventory(@RequestBody Inventory inventory) {
        Inventory.Builder builder = new Inventory.Builder(inventory.getInventoryId())
                .quantityInStock(inventory.getQuantityInStock())
                .minimumStockLevel(inventory.getMinimumStockLevel())
                .maximumStockLevel(inventory.getMaximumStockLevel())
                .unitPrice(inventory.getUnitPrice())
                .lastRestockedDate(inventory.getLastRestockedDate())
                .location(inventory.getLocation())
                .category(inventory.getCategory());

        if (inventory.getSupplier() != null && inventory.getSupplier().getSupplierId() != null) {
            Supplier resolvedSupplier = supplierRepository.findById(inventory.getSupplier().getSupplierId())
                    .orElse(inventory.getSupplier());
            builder.supplier(resolvedSupplier);
        } else {
            builder.supplier(inventory.getSupplier());
        }

        if (inventory.getProduct() != null && inventory.getProduct().getProductId() != null) {
            Product resolvedProduct = productRepository.findById(inventory.getProduct().getProductId())
                    .orElse(inventory.getProduct());
            builder.product(resolvedProduct);
        } else {
            builder.product(inventory.getProduct());
        }

        Inventory resolvedInventory = builder.build();

        Inventory saved = inventoryService.save(resolvedInventory);
        if (saved == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventory> updateInventory(
            @PathVariable Long id,
            @RequestBody Inventory inventory) {

        Optional<Inventory> optionalExisting = inventoryService.findById(id);

        if (optionalExisting.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Inventory existing = optionalExisting.get();

        // Update inventory fields without creating a new Inventory object
        existing.setQuantityInStock(inventory.getQuantityInStock());
        existing.setMinimumStockLevel(inventory.getMinimumStockLevel());
        existing.setMaximumStockLevel(inventory.getMaximumStockLevel());
        existing.setUnitPrice(inventory.getUnitPrice());
        existing.setLastRestockedDate(inventory.getLastRestockedDate());
        existing.setLocation(inventory.getLocation());

        // Resolve the existing product instead of inserting a duplicate
        if (inventory.getProduct() != null
                && inventory.getProduct().getProductId() != null) {

            Optional<Product> optionalProduct =
                    productRepository.findById(inventory.getProduct().getProductId());

            if (optionalProduct.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            Product existingProduct = optionalProduct.get();

            // Product name belongs to the Product record
            if (inventory.getProduct().getProductName() != null) {
                existingProduct.setProductName(
                        inventory.getProduct().getProductName());
                productRepository.save(existingProduct);
            }

            existing.setProduct(existingProduct);
        }

        // Reuse the existing supplier if one was supplied
        if (inventory.getSupplier() != null
                && inventory.getSupplier().getSupplierId() != null) {

            Optional<Supplier> optionalSupplier =
                    supplierRepository.findById(
                            inventory.getSupplier().getSupplierId());

            if (optionalSupplier.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            existing.setSupplier(optionalSupplier.get());
        }

        // Preserve the existing category to avoid inserting a duplicate category
        Inventory updated = inventoryService.save(existing);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        if (!inventoryService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        inventoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}