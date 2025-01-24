package uz.gayratjon.minimarketcrm.dto;

import lombok.Data;
import uz.gayratjon.minimarketcrm.model.PaymentMethod;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Data
public class SaleDTO {
    private Long id;
    private BigDecimal totalAmount;
    private PaymentMethod paymentMethod;
    private String createdAt;
    private Long cashierId;
    private List<SaleItemDTO> saleItems = new ArrayList<>();
}