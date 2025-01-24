package uz.gayratjon.minimarketcrm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.gayratjon.minimarketcrm.exp.ResourceNotFoundException;
import uz.gayratjon.minimarketcrm.model.Inventory;
import uz.gayratjon.minimarketcrm.model.User;
import uz.gayratjon.minimarketcrm.reposiroty.InventoryRepository;

import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductService productService;

    public List<Inventory> getAllInventoryRecords(Long userId) {
        return inventoryRepository.findByUserId(userId);
    }

    public Inventory getInventoryRecordById(Long id, Long userId) {
        return inventoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory record not found"));
    }

    public Inventory createInventoryRecord(Inventory inventory, Long userId) {
        inventory.setUser(new User(userId));
        productService.updateProductQuantity(inventory.getProduct().getId(),
                inventory.getNewQuantity() - inventory.getOldQuantity(),
                userId);
        return inventoryRepository.save(inventory);
    }
}