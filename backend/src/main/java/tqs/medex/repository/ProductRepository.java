package tqs.medex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tqs.medex.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {}
