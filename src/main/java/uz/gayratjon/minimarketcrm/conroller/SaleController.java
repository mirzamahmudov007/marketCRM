package uz.gayratjon.minimarketcrm.conroller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.gayratjon.minimarketcrm.conroller.BaseController;
import uz.gayratjon.minimarketcrm.dto.SaleDTO;
import uz.gayratjon.minimarketcrm.dto.SaleItemDTO;
import uz.gayratjon.minimarketcrm.model.Product;
import uz.gayratjon.minimarketcrm.model.Sale;
import uz.gayratjon.minimarketcrm.model.SaleItem;
import uz.gayratjon.minimarketcrm.model.User;
import uz.gayratjon.minimarketcrm.service.SaleService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/sales")
public class SaleController extends BaseController {

    @Autowired
    private SaleService saleService;

    @GetMapping
    public ResponseEntity<List<SaleDTO>> getAllSales() {
        Long userId = getCurrentUserId();
        List<Sale> sales = saleService.getAllSales(userId);
        List<SaleDTO> saleDTOs = sales.stream()
                .map(saleService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(saleDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDTO> getSaleById(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        Sale sale = saleService.getSaleById(id, userId);
        return ResponseEntity.ok(saleService.convertToDTO(sale));
    }

    @PostMapping
    public ResponseEntity<SaleDTO> createSale(@Valid @RequestBody SaleDTO saleDTO) {
        Long userId = getCurrentUserId();
        Sale sale = convertToEntity(saleDTO);
        Sale createdSale = saleService.createSale(sale, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(saleService.convertToDTO(createdSale));
    }

    private Sale convertToEntity(SaleDTO dto) {
        Sale sale = new Sale();
        sale.setPaymentMethod(dto.getPaymentMethod());

        if (dto.getCashierId() != null) {
            User cashier = new User();
            cashier.setId(dto.getCashierId());
            sale.setCashier(cashier);
        }

        List<SaleItem> items = dto.getSaleItems().stream()
                .map(this::convertToSaleItem)
                .collect(Collectors.toList());
        sale.setSaleItems(items);

        return sale;
    }

    private SaleItem convertToSaleItem(SaleItemDTO dto) {
        SaleItem item = new SaleItem();
        Product product = new Product();
        product.setId(dto.getProductId());
        item.setProduct(product);
        item.setQuantity(dto.getQuantity());
        item.setPrice(dto.getPrice());
        return item;
    }
}