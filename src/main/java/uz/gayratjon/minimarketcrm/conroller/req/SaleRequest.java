package uz.gayratjon.minimarketcrm.conroller.req;

import uz.gayratjon.minimarketcrm.model.Sale;
import uz.gayratjon.minimarketcrm.model.SaleItem;

import java.util.List;

public class SaleRequest {
    private Sale sale;
    private List<SaleItem> saleItems;

    // Getters and Setters
    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public List<SaleItem> getSaleItems() {
        return saleItems;
    }

    public void setSaleItems(List<SaleItem> saleItems) {
        this.saleItems = saleItems;
    }
}