package tqs.medex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.medex.entity.Product;
import tqs.medex.entity.Supplier;
import tqs.medex.pojos.ProductPOJO;
import tqs.medex.repository.ProductRepository;
import tqs.medex.repository.SupplierRepository;


@Service
public class ProductService {
  @Autowired
  private SupplierRepository supplierRepository;
  @Autowired
  private ProductRepository productrepository;
  public Product addNewProduct(ProductPOJO productPOJO) {
    Product product = new Product();
    product.setPrice(productPOJO.getPrice());
    product.setName(productPOJO.getName());
    Supplier supplier = supplierRepository.findById(productPOJO.getSupplier()).orElse(null);
    if (supplier == null) {
      return null;
    }
    product.setSupplier( supplier);
    return productrepository.save(product);
  }
}
