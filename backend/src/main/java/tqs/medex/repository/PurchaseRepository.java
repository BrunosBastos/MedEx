package tqs.medex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tqs.medex.entity.Purchase;
import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findAllByUser_UserId(Long id);
    Optional<Purchase> findByIdAndUser_UserId(Long purchaseId, Long userId);
}
