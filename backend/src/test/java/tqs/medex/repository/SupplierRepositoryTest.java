package tqs.medex.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tqs.medex.entity.Supplier;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SupplierRepositoryTest {
  @Autowired private TestEntityManager entityManager;
  @Autowired private SupplierRepository supplierRepository;

  @Test
  void givenSetOfSuppliers_whenFindAll_thenReturnSet() {
    Supplier supplier = new Supplier("Pharmacy", 50, 50);
    Supplier supplier2 = new Supplier("AnotherPharmacy", 60, 60);
    Arrays.asList(supplier, supplier2)
        .forEach(
            sup -> {
              entityManager.persistAndFlush(sup);
            });
    List<Supplier> supplierList = supplierRepository.findAll();
    assertThat(supplierList)
        .hasSize(2)
        .extracting(Supplier::getName)
        .contains(supplier.getName(), supplier2.getName());
  }

  @Test
  void whenFindSupplierByExistingId_thenReturnSupplier() {
    Supplier supplier = new Supplier();
    entityManager.persistAndFlush(supplier);
    Supplier suppliedb = supplierRepository.findById(supplier.getId()).orElse(null);
    assertThat(suppliedb).isNotNull();
    assertThat(suppliedb.getId()).isEqualTo(supplier.getId());
    assertThat(suppliedb.getName()).isEqualTo(supplier.getName());
  }

  @Test
  void whenInvalidId_thenReturnNull() {
    Supplier suppliedb = supplierRepository.findById(-99L).orElse(null);
    assertThat(suppliedb).isNull();
  }

  @Test
  void whenInvalidName_thenReturnNull() {
    Supplier suppliedb = supplierRepository.findByName("----").orElse(null);
    assertThat(suppliedb).isNull();
  }

  @Test
  void whenFindSupplierByExistingName_thenReturnSupplier() {
    Supplier supplier = new Supplier("Pharmacy", 50, 50);
    entityManager.persistAndFlush(supplier);
    Supplier suppliedb = supplierRepository.findByName(supplier.getName()).orElse(null);
    assertThat(suppliedb).isNotNull();
    assertThat(suppliedb.getId()).isEqualTo(supplier.getId());
    assertThat(suppliedb.getName()).isEqualTo(supplier.getName());
  }
}
