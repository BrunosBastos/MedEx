package tqs.medex.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tqs.medex.entity.PurchaseProduct;

@Repository
public interface PurchaseProductRepository extends JpaRepository<PurchaseProduct, Long> {
  Page<PurchaseProduct> findAllByProductSupplierId(Long supplierId, Pageable pageable);
}
