package uz.gayratjon.minimarketcrm.conroller;// File: src/main/java/uz/gayratjon/minimarketcrm/controller/InventoryController.java

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.gayratjon.minimarketcrm.conroller.BaseController;
import uz.gayratjon.minimarketcrm.model.Inventory;
import uz.gayratjon.minimarketcrm.service.InventoryService;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/inventory")
public class InventoryController extends BaseController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventoryRecords() {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(inventoryService.getAllInventoryRecords(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventoryRecordById(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(inventoryService.getInventoryRecordById(id, userId));
    }

    @PostMapping
    public ResponseEntity<Inventory> createInventoryRecord(@RequestBody Inventory inventory) {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(inventoryService.createInventoryRecord(inventory, userId));
    }
}