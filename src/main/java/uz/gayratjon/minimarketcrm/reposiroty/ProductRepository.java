package uz.gayratjon.minimarketcrm.reposiroty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.gayratjon.minimarketcrm.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByUserId(Long userId);

    Optional<Product> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT p FROM Product p WHERE p.user.id = :userId AND p.quantity < p.minQuantity")
    List<Product> findLowStockProducts(@Param("userId") Long userId);

    @Query("SELECT p FROM Product p WHERE p.user.id = :userId ORDER BY p.quantity DESC")
    List<Product> findAllWithCurrentStock(@Param("userId") Long userId);

    long countByUserId(Long userId);
}

