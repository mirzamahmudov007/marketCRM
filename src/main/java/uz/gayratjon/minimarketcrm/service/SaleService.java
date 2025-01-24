package uz.gayratjon.minimarketcrm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.gayratjon.minimarketcrm.dto.SaleDTO;
import uz.gayratjon.minimarketcrm.dto.SaleItemDTO;
import uz.gayratjon.minimarketcrm.exp.ResourceNotFoundException;
import uz.gayratjon.minimarketcrm.model.Product;
import uz.gayratjon.minimarketcrm.model.Sale;
import uz.gayratjon.minimarketcrm.model.SaleItem;
import uz.gayratjon.minimarketcrm.model.User;
import uz.gayratjon.minimarketcrm.reposiroty.SaleRepository;
import uz.gayratjon.minimarketcrm.reposiroty.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    public List<Sale> getAllSales(Long userId) {
        return saleRepository.findByUserId(userId);
    }

    public Sale getSaleById(Long id, Long userId) {
        return saleRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));
    }

    @Transactional
    public Sale createSale(Sale sale, Long userId) {
        log.info("Creating sale for user ID: {}", userId);

        if (sale == null) {
            throw new IllegalArgumentException("Sale cannot be null");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        User cashier;
        if (sale.getCashier() != null && sale.getCashier().getId() != null) {
            cashier = userRepository.findById(sale.getCashier().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cashier not found with id: " + sale.getCashier().getId()));
        } else {
            cashier = user;
        }

        sale.setUser(user);
        sale.setCashier(cashier);
        sale.setCreatedAt(LocalDateTime.now());

        if (sale.getSaleItems() == null || sale.getSaleItems().isEmpty()) {
            throw new IllegalArgumentException("Sale must have at least one item");
        }

        BigDecimal calculatedTotal = BigDecimal.ZERO;
        for (SaleItem item : sale.getSaleItems()) {
            if (item.getProduct() == null || item.getProduct().getId() == null) {
                throw new IllegalArgumentException("Product information is required for sale items");
            }
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Valid quantity is required for sale items");
            }

            item.setSale(sale);

            Product product = productService.getProductById(item.getProduct().getId(), userId);

            if (item.getPrice() == null) {
                item.setPrice(product.getSellingPrice());
            }

            BigDecimal itemTotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            calculatedTotal = calculatedTotal.add(itemTotal);

            productService.updateProductQuantity(product.getId(), -item.getQuantity(), userId);
        }

        if (sale.getTotalAmount() == null) {
            sale.setTotalAmount(calculatedTotal);
        } else if (sale.getTotalAmount().compareTo(calculatedTotal) != 0) {
            log.warn("Provided total amount {} differs from calculated amount {}",
                    sale.getTotalAmount(), calculatedTotal);
            sale.setTotalAmount(calculatedTotal);
        }

        Sale savedSale = saleRepository.save(sale);
        log.info("Sale created successfully with ID: {}", savedSale.getId());
        return savedSale;
    }

    public SaleDTO convertToDTO(Sale sale) {
        SaleDTO dto = new SaleDTO();
        dto.setId(sale.getId());
        dto.setTotalAmount(sale.getTotalAmount());
        dto.setPaymentMethod(sale.getPaymentMethod());
        dto.setCreatedAt(sale.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        dto.setCashierId(sale.getCashier().getId());

        List<SaleItemDTO> itemDTOs = sale.getSaleItems().stream()
                .map(this::convertToSaleItemDTO)
                .collect(Collectors.toList());
        dto.setSaleItems(itemDTOs);

        return dto;
    }

    private SaleItemDTO convertToSaleItemDTO(SaleItem item) {
        SaleItemDTO dto = new SaleItemDTO();
        dto.setProductId(item.getProduct().getId());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        return dto;
    }
}