package uz.gayratjon.minimarketcrm.reposiroty;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.gayratjon.minimarketcrm.model.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserId(Long userId);
    Optional<Category> findByIdAndUserId(Long id, Long userId);
    List<Category> findByUserIdAndParentIsNull(Long userId);
    List<Category> findByIsPublic(boolean isPublic);
}

