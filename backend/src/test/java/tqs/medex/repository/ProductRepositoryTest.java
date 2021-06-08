package tqs.medex.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tqs.medex.entity.Product;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {
    private static final String IMAGE_URL =
            "https://lh3.googleusercontent.com/proxy/LAOk1qdvF1vC926xeHgL_PqHkc3c7rog4LcvcAgPVTCjYc8megOXU6NUY1jl_Fy3dHntjQwyhrDobmMT7XY-itIMLcjue6_QHWqhcM44hLnBJMaIpiQ96-fqzufr0CC2hrXm3tezCm1yhsUvlk63";
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ProductRepository productRepository;

    @Test
    void whenFindProductByExistingId_thenReturnProduct() {
        Product product = new Product();
        entityManager.persistAndFlush(product);
        Product productdb = productRepository.findById(product.getId()).orElse(null);
        assertThat(productdb).isNotNull();
        assertThat(productdb.getId()).isEqualTo(product.getId());
        assertThat(productdb.getName()).isEqualTo(product.getName());
    }

    @Test
    void whenFindProductByExistingName_thenReturnProduct() {
        Product product = new Product();
        entityManager.persistAndFlush(product);
        Product productdb = productRepository.findByName(product.getName()).orElse(null);
        assertThat(productdb).isNotNull();
        assertThat(productdb.getId()).isEqualTo(product.getId());
        assertThat(productdb.getName()).isEqualTo(product.getName());
    }

    @Test
    void whenInvalidId_thenReturnNull() {
        Product productdb = productRepository.findById(-99L).orElse(null);
        assertThat(productdb).isNull();
    }

    @Test
    void whenInvalidName_thenReturnNull() {
        Product productdb = productRepository.findByName("Non-Existing Product").orElse(null);
        assertThat(productdb).isNull();
    }

    @Test
    void givenSetOfProducts_whenFindAll_thenReturnSet() {
        Product product = new Product();
        product.setName("ProductTest");
        Product product2 = new Product();
        product2.setName("SecondProduct");
        Arrays.asList(product, product2)
                .forEach(
                        prod -> {
                            entityManager.persistAndFlush(prod);
                        });
        List<Product> productList = productRepository.findAll();
        assertThat(productList)
                .hasSize(2)
                .extracting(Product::getName)
                .contains(product.getName(), product2.getName());
    }
}
