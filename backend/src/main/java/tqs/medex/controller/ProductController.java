package tqs.medex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tqs.medex.entity.Product;
import tqs.medex.pojo.ProductPOJO;
import tqs.medex.service.ProductService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ProductController {
  @Autowired ProductService productService;

  @GetMapping("/products")
  public ResponseEntity<Object> getProducts() {
    List<Product> productList = productService.listProducts();
    return ResponseEntity.status(HttpStatus.OK).body(productList);
  }

  @GetMapping("/products/{id}")
  public ResponseEntity<Object> getProduct(@PathVariable long id) {
    var product = productService.getProductDetails(id);
    if (product == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product Not Found");
    }
    return ResponseEntity.status(HttpStatus.OK).body(product);
  }

  @PostMapping("/products")
  public ResponseEntity<Object> addNewProduct(@Valid @RequestBody ProductPOJO product) {
    var product1 = productService.addNewProduct(product);
    if (product1 == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Supplier Not Found");
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(product1);
  }

  @PutMapping("/products/{id}")
  public ResponseEntity<Object> updateProduct(
      @Valid @RequestBody ProductPOJO productPOJO, @PathVariable long id) {
    var product = productService.updateProduct(id, productPOJO);
    if (product == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product Not Found");
    }
    return ResponseEntity.status(HttpStatus.OK).body(product);
  }
}
