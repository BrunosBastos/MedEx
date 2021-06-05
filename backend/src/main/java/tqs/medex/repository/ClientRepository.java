package tqs.medex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tqs.medex.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
