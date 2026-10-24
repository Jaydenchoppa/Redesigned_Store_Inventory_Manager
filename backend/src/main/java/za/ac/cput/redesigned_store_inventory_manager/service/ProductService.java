package za.ac.cput.redesigned_store_inventory_manager.service;
/**
 *
 * @author Zacharia Dipudi
 */
import org.springframework.stereotype.Service;
import za.ac.cput.redesigned_store_inventory_manager.domain.Product;
import za.ac.cput.redesigned_store_inventory_manager.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
@Service
public class ProductService implements IProductService{
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> findById(String productId) {
        if(productId==null) return Optional.empty();
        return productRepository.findById(productId);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public void deleteById(String productId) {
        if(productId==null)return;
        productRepository.deleteById(productId);

    }

    @Override
    public boolean existsById(String productId) {
        if(productId==null)return  false;
        return productRepository.existsById(productId);
    }
}



