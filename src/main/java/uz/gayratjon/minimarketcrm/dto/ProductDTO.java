package uz.gayratjon.minimarketcrm.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String barcode;
    private Long categoryId;
    private String category;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private Integer quantity;
    private String unit;
    private Integer minQuantity;
    private String imageUrl;
}

