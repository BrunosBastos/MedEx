package tqs.medex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tqs.medex.entity.Product;
import tqs.medex.pojo.ProductPOJO;
import tqs.medex.repository.ProductRepository;
import tqs.medex.repository.SupplierRepository;

import java.util.List;

@Service
public class ProductService {
  public static final int PAGE_SIZE = 10;
  @Autowired private SupplierRepository supplierRepository;
  @Autowired private ProductRepository productrepository;

  public List<Product> listProducts(String name, int page, boolean recent) {
    Pageable pageable =
        PageRequest.of(
            page, PAGE_SIZE, recent ? Sort.by("id").descending() : Sort.by("id").ascending());
    return productrepository.findAllByNameIgnoreCaseContaining(name, pageable).getContent();
  }

  public Product getProductDetails(long id) {
    return productrepository.findById(id).orElse(null);
  }

  public Product addNewProduct(ProductPOJO productPOJO) {
    var product = new Product();
    product.setName(productPOJO.getName());
    product.setDescription(productPOJO.getDescription());
    product.setStock(productPOJO.getStock());
    product.setPrice(productPOJO.getPrice());
    product.setImageUrl(product.getImageUrl());
    var supplier = supplierRepository.findById(productPOJO.getSupplier()).orElse(null);
    if (supplier == null) {
      return null;
    }
    product.setSupplier(supplier);
    return productrepository.save(product);
  }

  public Product updateProduct(long productid, ProductPOJO productPOJO) {
    var product = productrepository.findById(productid).orElse(null);
    if (product == null) {
      return null;
    }
    if (productPOJO.getName() != null) {
      product.setName(productPOJO.getName());
    }
    if (productPOJO.getDescription() != null) {
      product.setDescription(productPOJO.getDescription());
    }
    if (productPOJO.getPrice() != 0) {
      product.setPrice(productPOJO.getPrice());
    }
    if (productPOJO.getStock() != null) {
      product.setStock(productPOJO.getStock());
    }
    if (productPOJO.getImageUrl() != null) {
      product.setImageUrl(productPOJO.getImageUrl());
    }
    return productrepository.save(product);
  }
}
