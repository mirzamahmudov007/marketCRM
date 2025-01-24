package uz.gayratjon.minimarketcrm.reposiroty;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.gayratjon.minimarketcrm.model.SaleItem;

public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {
}