package za.ac.cput.redesigned_store_inventory_manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.redesigned_store_inventory_manager.domain.Inventory;
import za.ac.cput.redesigned_store_inventory_manager.domain.Order;
import za.ac.cput.redesigned_store_inventory_manager.repository.InventoryRepository;
import za.ac.cput.redesigned_store_inventory_manager.repository.OrderRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {

    private final InventoryRepository inventoryRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public PurchaseController(InventoryRepository inventoryRepository, OrderRepository orderRepository) {
        this.inventoryRepository = inventoryRepository;
        this.orderRepository = orderRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<String> purchase(@RequestBody PurchaseRequest request, Authentication authentication) {
        // Reject nonsense input: a negative quantity would otherwise ADD stock
        // (stock >= -5 is true, and stock - (-5) increases it).
        if (request.inventoryId == null || request.quantity <= 0) {
            return ResponseEntity.status(400).body("Quantity must be at least 1");
        }
        // The buyer is whoever is logged in, never whatever the request body claims.
        String buyer = authentication.getName();

        int updated = inventoryRepository.decrementStock(request.inventoryId, request.quantity);
        if (updated == 0) {
            return ResponseEntity.status(400).body("Not enough stock available");
        }

        Optional<Inventory> invOpt = inventoryRepository.findById(request.inventoryId);
        if (invOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Item not found");
        }
        Inventory inv = invOpt.get();
        double total = inv.getUnitPrice() * request.quantity;
        String productId = inv.getProduct() != null ? inv.getProduct().getProductId() : "Unknown";

        Order order = new Order.Builder()
                .setOrderNum(UUID.randomUUID().toString())
                .setCustomerId(buyer)
                .setItem(productId)
                .setQuantity(request.quantity)
                .setOrderDate(LocalDate.now().toString())
                .setStatus("Completed")
                .setTotalAmount(total)
                .build();

        orderRepository.save(order);
        return ResponseEntity.ok("Purchase successful");
    }

    public static class PurchaseRequest {
        public String customerId;
        public Long inventoryId;
        public int quantity;
    }
}
