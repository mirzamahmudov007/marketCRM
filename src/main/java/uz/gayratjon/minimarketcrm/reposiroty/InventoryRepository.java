package uz.gayratjon.minimarketcrm.reposiroty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.gayratjon.minimarketcrm.model.Inventory;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByUserId(Long userId);
    Optional<Inventory> findByIdAndUserId(Long id, Long userId);
}