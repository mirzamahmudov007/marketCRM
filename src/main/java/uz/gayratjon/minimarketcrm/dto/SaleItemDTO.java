package uz.gayratjon.minimarketcrm.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleItemDTO {
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
}