package tqs.medex;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import tqs.medex.repository.*;
import tqs.medex.entity.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Configuration
class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(UserRepository users,ClientRepository clients,
                                  ProductRepository products, SupplierRepository suppliers) {

        return args -> {

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            User admin_user = new User(1L, "clara@gmail.com", encoder.encode("string"),  true);
            users.save(admin_user);
            Client clara = new Client(1L, "clara", admin_user);
            clients.save(clara);

            User client_user = new User(2L, "henrique@gmail.com", encoder.encode("string"),  false);
            users.save(client_user);
            Client henrique = new Client(2L, "henrique", client_user);
            clients.save(henrique);

            Supplier supplier = new Supplier("Pharmacy", 50, 50);
            Supplier supplier2 = new Supplier("Pharmacy2", 60, 60);
            Arrays.asList(supplier, supplier2)
                    .forEach(
                            sup -> {
                                suppliers.save(sup);
                            });
            Product product = new Product("ProductTest", "A description", 1, 4.99, null);
            Product product2 = new Product("ProductTest2", "A description2", 4, 0.99, null);
            List<Product> products_list = Arrays.asList(product, product2);
            products_list.forEach(
                    prod -> {
                        prod.setSupplier(supplier);
                        products.save(prod);
                    });

        };
    }
}