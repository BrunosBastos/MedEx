package tqs.medex;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tqs.medex.entity.Product;
import tqs.medex.entity.Supplier;
import tqs.medex.entity.User;
import tqs.medex.repository.ProductRepository;
import tqs.medex.repository.SupplierRepository;
import tqs.medex.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

@Configuration
class LoadDatabase {

  @Bean
  CommandLineRunner initDatabase(
      UserRepository users, ProductRepository products, SupplierRepository suppliers) {

    return args -> {
      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
      User adminUser = new User(1L, "clara@gmail.com", encoder.encode("string"), true, "clara");
      users.save(adminUser);

      User clientUser =
          new User(2L, "henrique@gmail.com", encoder.encode("string"), false, "henrique");
      users.save(clientUser);

      Supplier supplier = new Supplier(1L, "Pharmacy", 50, 50);
      Supplier supplier2 = new Supplier(2L, "Pharmacy2", 60, 60);
      Arrays.asList(supplier, supplier2)
          .forEach(suppliers::save);
      Product product = new Product(1L, "ProductTest", "A description", 1, 4.99, null);
      Product product2 = new Product(2L, "ProductTest2", "A description2", 4, 0.99, null);
      List<Product> productList = Arrays.asList(product, product2);
      productList.forEach(
          prod -> {
            prod.setSupplier(supplier);
            products.save(prod);
          });
    };
  }
}
