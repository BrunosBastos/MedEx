package tqs.medex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import tqs.medex.entity.Product;
import tqs.medex.pojos.ProductPOJO;
import tqs.medex.service.ProductService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class ProductController {
  @Autowired ProductService productService;

  @PostMapping("/products")
  public ResponseEntity<Object> addNewProduct(@Valid @RequestBody ProductPOJO product) {
    Product product1 = productService.addNewProduct(product);
    if(product1 == null){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Supplier Not Found");
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(product1);
  }
}
