package uz.gayratjon.minimarketcrm.conroller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.gayratjon.minimarketcrm.conroller.BaseController;
import uz.gayratjon.minimarketcrm.service.ReportService;

import java.time.LocalDate;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/reports")
public class ReportController extends BaseController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/sales")
    public ResponseEntity<Map<String, Object>> getSalesReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(reportService.generateSalesReport(startDate, endDate, userId));
    }


    @GetMapping("/inventory")
    public ResponseEntity<Map<String, Object>> getInventoryReport() {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(reportService.generateInventoryReport(userId));
    }

    @GetMapping("/profit")
    public ResponseEntity<Map<String, Object>> getProfitReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Long userId = getCurrentUserId();
        Map<String, Object> report = reportService.generateProfitReport(startDate, endDate, userId);
        return ResponseEntity.ok(report);
    }
}