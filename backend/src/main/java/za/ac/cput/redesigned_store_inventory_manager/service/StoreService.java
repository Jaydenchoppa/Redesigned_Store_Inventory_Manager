package za.ac.cput.redesigned_store_inventory_manager.service;

/**
 *
 * @author Zacharia Dipudi
 */
import org.springframework.stereotype.Service;
import za.ac.cput.redesigned_store_inventory_manager.domain.Store;
import za.ac.cput.redesigned_store_inventory_manager.repository.StoreRepository;

import java.util.List;
import java.util.Optional;
@Service
public class StoreService implements IStoreService {
    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }


    @Override
    public Store save(Store store) {
        return storeRepository.save(store);
    }

    @Override
    public Optional<Store> findById(String storeId) {
        if(storeId==null) return Optional.empty();
        return storeRepository.findById(storeId);
    }

    @Override
    public List<Store> findAll() {
        return storeRepository.findAll();
    }

    @Override
    public void deleteById(String storeId) {
        if(storeId==null)return;
        storeRepository.deleteById(storeId);
    }

    @Override
    public boolean existsById(String storeId) {
        if(storeId==null) return false;
        return storeRepository.existsById(storeId);
    }
}





