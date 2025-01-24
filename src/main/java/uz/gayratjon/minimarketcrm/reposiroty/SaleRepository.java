package uz.gayratjon.minimarketcrm.reposiroty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.gayratjon.minimarketcrm.model.Sale;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findByUserId(Long userId);

    Optional<Sale> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT COUNT(s) FROM Sale s WHERE DATE(s.createdAt) BETWEEN :startDate AND :endDate AND s.user.id = :userId")
    long countSalesBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s WHERE DATE(s.createdAt) BETWEEN :startDate AND :endDate AND s.user.id = :userId")
    BigDecimal sumSalesAmountBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("userId") Long userId);

    @Query("SELECT NEW map(p.name as productName, COALESCE(SUM(si.quantity), 0) as totalQuantity, COALESCE(SUM(si.price * si.quantity), 0) as totalAmount) " +
            "FROM Sale s JOIN s.saleItems si JOIN si.product p " +
            "WHERE DATE(s.createdAt) BETWEEN :startDate AND :endDate AND s.user.id = :userId " +
            "GROUP BY p.name")
    List<Map<String, Object>> getSalesByProductBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(si.quantity * p.purchasePrice), 0) FROM Sale s JOIN s.saleItems si JOIN si.product p " +
            "WHERE DATE(s.createdAt) BETWEEN :startDate AND :endDate AND s.user.id = :userId")
    BigDecimal sumPurchaseCostBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(si.price * si.quantity) - SUM(si.quantity * p.purchasePrice), 0) " +
            "FROM Sale s JOIN s.saleItems si JOIN si.product p " +
            "WHERE DATE(s.createdAt) BETWEEN :startDate AND :endDate AND s.user.id = :userId")
    BigDecimal calculateGrossProfitBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("userId") Long userId);

    @Query("SELECT NEW map(p.name as productName, " +
            "COALESCE(SUM(si.price * si.quantity), 0) as revenue, " +
            "COALESCE(SUM(si.quantity * p.purchasePrice), 0) as cost, " +
            "COALESCE(SUM(si.price * si.quantity) - SUM(si.quantity * p.purchasePrice), 0) as profit) " +
            "FROM Sale s JOIN s.saleItems si JOIN si.product p " +
            "WHERE DATE(s.createdAt) BETWEEN :startDate AND :endDate AND s.user.id = :userId " +
            "GROUP BY p.name")
    List<Map<String, Object>> getProfitByProductBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("userId") Long userId);
}
