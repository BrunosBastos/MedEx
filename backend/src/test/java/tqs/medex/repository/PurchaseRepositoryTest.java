package tqs.medex.repository;


import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tqs.medex.entity.Product;
import tqs.medex.entity.Purchase;
import tqs.medex.entity.PurchaseProduct;
import tqs.medex.entity.User;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PurchaseRepositoryTest {

    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired private TestEntityManager entityManager;
    private User user;

    @Test
    void givenSetOfPurchases_whenFindAllByUserId_thenReturnSet(){
        var purchases = setUpPurchases();
        var purchases_db = purchaseRepository.findAllByUser_UserId(user.getUserId());
        assertThat(purchases_db).hasSize(2)
                .extracting(Purchase::getId).containsAll(
                purchases.stream().map( Purchase::getId).collect( Collectors.toList() ));

    }

    @Test
    void givenSetOfPurchases_whenFindAllByIdAndUserId(){
        var purchases = setUpPurchases();
        var purchases_db = purchaseRepository.findByIdAndUser_UserId(1L,1L);
        assertThat(purchases_db).isPresent();
        assertThat(purchases_db.get().getId()).isEqualTo(1L);
        assertThat(purchases_db.get().getUser().getUserId()).isEqualTo(1L);
    }


    List<Purchase> setUpPurchases() {
        user = new User();
        entityManager.persistAndFlush(user);
        System.out.println(user.getUserId());
        Product p1 = new Product();
        Product p2 = new Product();
        entityManager.persistAndFlush(p1);
        entityManager.persistAndFlush(p2);
        Purchase order = new Purchase(10, 20);
        order.setUser(user);
        entityManager.persistAndFlush(order);
        Purchase order2 = new Purchase(30,40);
        order2.setUser(user);
        entityManager.persistAndFlush(order2);
        PurchaseProduct op1 = new PurchaseProduct(order, p1, 10);
        PurchaseProduct op2 = new PurchaseProduct(order, p2, 20);
        entityManager.persistAndFlush(op1);
        entityManager.persistAndFlush(op2);
        return Arrays.asList(order,order2);
    }
}
