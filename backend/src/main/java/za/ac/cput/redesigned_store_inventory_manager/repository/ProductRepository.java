package za.ac.cput.redesigned_store_inventory_manager.repository;
/**
 *
 * @author Zacharia Dipudi
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.redesigned_store_inventory_manager.domain.Product;
@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
}
