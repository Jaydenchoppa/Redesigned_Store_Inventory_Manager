package za.ac.cput.redesigned_store_inventory_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import za.ac.cput.redesigned_store_inventory_manager.domain.Inventory;

import java.util.Optional;

/**
 *
 * @author Jayden
 */
public interface InventoryRepository extends JpaRepository<Inventory, Long>{
    @Modifying
    @Query("UPDATE Inventory i SET i.quantityInStock = i.quantityInStock - :qty " +
            "WHERE i.inventoryId = :id AND i.quantityInStock >= :qty")
    int decrementStock(@Param("id") Long id, @Param("qty") int qty);

    Optional<Inventory> findByProduct_ProductId(String productId);
}
