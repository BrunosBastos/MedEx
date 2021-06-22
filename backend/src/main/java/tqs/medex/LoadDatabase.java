package tqs.medex;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tqs.medex.entity.*;
import tqs.medex.repository.*;

import java.util.Arrays;
import java.util.List;

@Configuration
class LoadDatabase {

  @Bean
  CommandLineRunner initDatabase(
      UserRepository users,
      ProductRepository products,
      SupplierRepository suppliers,
      PurchaseRepository purchaseRepository,
      PurchaseProductRepository purchaseProductRepository) {

    return args -> {
      var encoder = new BCryptPasswordEncoder();
      var adminUser = new User(1L, "clara@gmail.com", encoder.encode("string"), true, "clara");
      users.save(adminUser);

      var clientUser =
          new User(2L, "henrique@gmail.com", encoder.encode("string"), false, "henrique");
      users.save(clientUser);
      var supplier = new Supplier(1L, "Pharmacy", 50, 50);
      var supplier2 = new Supplier(2L, "Pharmacy2", 60, 60);
      Arrays.asList(supplier, supplier2).forEach(suppliers::save);
      var product = new Product(1L, "ProductTest", "A description", 10, 4.99, null);
      var product2 = new Product(2L, "ProductTest2", "A description2", 20, 0.99, null);
      List<Product> productList = Arrays.asList(product, product2);
      productList.forEach(
          prod -> {
            prod.setSupplier(supplier);
            products.save(prod);
          });
      var order = new Purchase(10, 20);
      order.setDelivered(true);
      var order2 = new Purchase(30, 40);
      var order3 = new Purchase(30, 40);
      var order4 = new Purchase(30, 40);
      order.setDelivered(true);
      var order5 = new Purchase(20, 20);
      var op1 = new PurchaseProduct(order, product, 10);
      var op2 = new PurchaseProduct(order, product2, 20);
      var op3 = new PurchaseProduct(order2, product2, 20);
      var op4 = new PurchaseProduct(order3, product, 1);
      var op5 = new PurchaseProduct(order4, product, 1);
      var op6 = new PurchaseProduct(order5, product, 3);
      purchaseRepository.saveAll(Arrays.asList(order, order2, order3, order4, order5));
      purchaseProductRepository.saveAll(Arrays.asList(op1, op2, op3, op4, op5, op6));
      order.setUser(clientUser);
      order2.setUser(adminUser);
      order3.setUser(clientUser);
      order4.setUser(clientUser);
      order5.setUser(clientUser);
      purchaseRepository.saveAll(Arrays.asList(order, order2, order3, order4, order5));
    };
  }
}
