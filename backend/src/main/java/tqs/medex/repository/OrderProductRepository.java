package tqs.medex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tqs.medex.entity.PurchaseProduct;

@Repository
public interface OrderProductRepository extends JpaRepository<PurchaseProduct, Long> {
}
