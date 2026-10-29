package za.ac.cput.redesigned_store_inventory_manager.controller;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.redesigned_store_inventory_manager.domain.Inventory;
import za.ac.cput.redesigned_store_inventory_manager.domain.Product;
import za.ac.cput.redesigned_store_inventory_manager.domain.Supplier;
import za.ac.cput.redesigned_store_inventory_manager.repository.InventoryRepository;
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
    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryController(IInventoryService inventoryService,
                               ProductRepository productRepository,
                               SupplierRepository supplierRepository,
                               InventoryRepository inventoryRepository) {
        this.inventoryService = inventoryService;
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        this.inventoryRepository = inventoryRepository;
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
    public ResponseEntity<?> createInventory(@RequestBody Inventory inventory) {
        if (inventory.getProduct() == null
                || inventory.getProduct().getProductId() == null
                || inventory.getProduct().getProductId().isBlank()) {
            return ResponseEntity.badRequest().body("A Product ID is required.");
        }

        String productId = inventory.getProduct().getProductId().trim();

        // Resolve the product if it already exists, otherwise register it -
        // the Add Inventory form collects the product name for exactly this purpose.
        Product product = productRepository.findById(productId)
                .orElseGet(() -> {
                    Product newProduct = new Product();
                    newProduct.setProductId(productId);
                    newProduct.setProductName(inventory.getProduct().getProductName());
                    return productRepository.save(newProduct);
                });

        // Resolve the supplier by ID. The supplier must already exist -
        // it can be created on the Suppliers screen.
        Supplier supplier = null;
        if (inventory.getSupplier() != null
                && inventory.getSupplier().getSupplierId() != null
                && !inventory.getSupplier().getSupplierId().isBlank()) {

            String supplierId = inventory.getSupplier().getSupplierId().trim();

            Optional<Supplier> optionalSupplier = supplierRepository.findById(supplierId);
            if (optionalSupplier.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Supplier '" + supplierId
                                + "' was not found. Add the supplier on the Suppliers screen first.");
            }
            supplier = optionalSupplier.get();
        }

        // One inventory record per product.
        if (inventoryRepository.findByProduct_ProductId(productId).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("That Product ID already exists. Please use a different Product ID.");
        }

        Inventory resolvedInventory = new Inventory.Builder(inventory.getInventoryId())
                .product(product)
                .supplier(supplier)
                .category(inventory.getCategory())
                .quantityInStock(inventory.getQuantityInStock())
                .unitPrice(inventory.getUnitPrice())
                .location(inventory.getLocation())
                .build();

        Inventory saved = inventoryService.save(resolvedInventory);
        if (saved == null) {
            return ResponseEntity.badRequest().body("Could not save the inventory item.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInventory(
            @PathVariable Long id,
            @RequestBody Inventory inventory) {

        Optional<Inventory> optionalExisting = inventoryService.findById(id);

        if (optionalExisting.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Inventory existing = optionalExisting.get();

        // Update inventory fields without creating a new Inventory object
        existing.setQuantityInStock(inventory.getQuantityInStock());
        existing.setUnitPrice(inventory.getUnitPrice());
        existing.setLocation(inventory.getLocation());

        // Resolve the existing product instead of inserting a duplicate
        if (inventory.getProduct() != null
                && inventory.getProduct().getProductId() != null) {

            Optional<Product> optionalProduct =
                    productRepository.findById(inventory.getProduct().getProductId());

            if (optionalProduct.isEmpty()) {
                return ResponseEntity.badRequest().body("Product '"
                        + inventory.getProduct().getProductId() + "' was not found.");
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
                && inventory.getSupplier().getSupplierId() != null
                && !inventory.getSupplier().getSupplierId().isBlank()) {

            Optional<Supplier> optionalSupplier =
                    supplierRepository.findById(
                            inventory.getSupplier().getSupplierId().trim());

            if (optionalSupplier.isEmpty()) {
                return ResponseEntity.badRequest().body("Supplier '"
                        + inventory.getSupplier().getSupplierId()
                        + "' was not found. Add the supplier on the Suppliers screen first.");
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
