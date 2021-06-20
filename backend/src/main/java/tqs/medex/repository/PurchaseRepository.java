package tqs.medex.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tqs.medex.entity.Purchase;

import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
  Page<Purchase> findAllByUser_UserId(Long id, Pageable pageable);

  Optional<Purchase> findByIdAndUser_UserId(Long purchaseId, Long userId);
}
