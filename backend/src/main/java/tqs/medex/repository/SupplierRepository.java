package tqs.medex.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tqs.medex.entity.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
