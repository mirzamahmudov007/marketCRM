// File: src/main/java/uz/gayratjon/minimarketcrm/service/ReportService.java
package uz.gayratjon.minimarketcrm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.gayratjon.minimarketcrm.reposiroty.ProductRepository;
import uz.gayratjon.minimarketcrm.reposiroty.SaleRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
@Slf4j
public class ReportService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ProductRepository productRepository;

    public Map<String, Object> generateSalesReport(LocalDate startDate, LocalDate endDate, Long userId) {
        log.info("Generating sales report for user {} between {} and {}", userId, startDate, endDate);

        Map<String, Object> report = new HashMap<>();

        long totalSales = saleRepository.countSalesBetweenDates(startDate, endDate, userId);
        log.debug("Total sales count: {}", totalSales);
        report.put("totalSales", totalSales);

        BigDecimal salesAmount = saleRepository.sumSalesAmountBetweenDates(startDate, endDate, userId);
        log.debug("Total sales amount: {}", salesAmount);
        report.put("salesAmount", salesAmount);

        List<Map<String, Object>> salesByProduct = saleRepository.getSalesByProductBetweenDates(startDate, endDate, userId);
        log.debug("Sales by product: {}", salesByProduct);
        report.put("salesByProduct", salesByProduct);

        return report;
    }

    public Map<String, Object> generateInventoryReport(Long userId) {
        Map<String, Object> report = new HashMap<>();
        report.put("currentStock", productRepository.findAllWithCurrentStock(userId));
        report.put("lowStockItems", productRepository.findLowStockProducts(userId));
        report.put("totalProducts", productRepository.countByUserId(userId));
        return report;
    }

    public Map<String, Object> generateProfitReport(LocalDate startDate, LocalDate endDate, Long userId) {
        Map<String, Object> report = new HashMap<>();
        report.put("totalRevenue", saleRepository.sumSalesAmountBetweenDates(startDate, endDate, userId));
        report.put("totalCost", saleRepository.sumPurchaseCostBetweenDates(startDate, endDate, userId));
        report.put("grossProfit", saleRepository.calculateGrossProfitBetweenDates(startDate, endDate, userId));
        report.put("profitByProduct", saleRepository.getProfitByProductBetweenDates(startDate, endDate, userId));
        return report;
    }
}