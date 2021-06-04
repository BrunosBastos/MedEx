package tqs.medex.controller;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tqs.medex.entity.Product;
import tqs.medex.entity.Supplier;
import tqs.medex.pojo.ProductPOJO;
import tqs.medex.service.ProductService;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
  private static final String IMAGE_URL =
      "https://lh3.googleusercontent.com/proxy/LAOk1qdvF1vC926xeHgL_PqHkc3c7rog4LcvcAgPVTCjYc8megOXU6NUY1jl_Fy3dHntjQwyhrDobmMT7XY-itIMLcjue6_QHWqhcM44hLnBJMaIpiQ96-fqzufr0CC2hrXm3tezCm1yhsUvlk63";
  @Autowired private MockMvc mvc;
  @MockBean private ProductService productService;

  @BeforeEach
  void setUp() {
    RestAssuredMockMvc.mockMvc(mvc);
  }

  @Test
  void whenGetProducts_thenReturnProducts() {
    Product product = new Product("ProductTest", "A description", 1, 4.99, IMAGE_URL);
    Product product2 = new Product("ProductTest2", "A description2", 4, 0.99, IMAGE_URL);
    when(productService.listProducts()).thenReturn(Arrays.asList(product, product2));
    RestAssuredMockMvc.given()
        .when()
        .get("api/v1/products")
        .then()
        .assertThat()
        .statusCode(200)
        .and()
        .body("", hasSize(2))
        .and()
        .body("[0].name", is(product.getName()))
        .and()
        .body("[0].price", is((float) product.getPrice()))
        .and()
        .body("[1].name", is(product2.getName()))
        .and()
        .body("[1].price", is((float) product2.getPrice()));
    verify(productService, times(1)).listProducts();
  }

  @Test
  void whenGetProductById_thenReturnProduct() {
    Product product = setUpObject();
    when(productService.getProductDetails(product.getId())).thenReturn(product);
    RestAssuredMockMvc.given()
        .when()
        .get("/api/v1/products/1")
        .then()
        .assertThat()
        .statusCode(200)
        .and()
        .body("name", is(product.getName()))
        .and()
        .body("price", is((float) product.getPrice()));
    verify(productService, times(1)).getProductDetails(product.getId());
  }

  @Test
  void whenGetProductByInvalidId_thenReturnBadRequest() {
    when(productService.getProductDetails(-99L)).thenReturn(null);
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .get("api/v1/products/-99")
        .then()
        .assertThat()
        .statusCode(400)
        .statusLine("400 Product Not Found");
    verify(productService, times(1)).getProductDetails(-99L);
  }

  @Test
  void whenAddProduct_thenReturnProduct() {
    Product product = setUpObject();
    ProductPOJO productPOJO = setUpObjectPOJO();
    when(productService.addNewProduct(Mockito.any(ProductPOJO.class))).thenReturn(product);
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .body(productPOJO)
        .post("api/v1/products")
        .then()
        .assertThat()
        .statusCode(201)
        .and()
        .body("name", is(product.getName()))
        .and()
        .body("stock", is(product.getStock()))
        .and()
        .body("price", is((float) product.getPrice()))
        .and()
        .body("supplier.id", is(product.getSupplier().getId()));
    verify(productService, times(1)).addNewProduct(productPOJO);
  }

  @Test
  void whenAddInvalidProductSupplier_thenReturnBadRequest() {
    ProductPOJO productPOJO = setUpInvalidObjectPOJO();
    when(productService.addNewProduct(productPOJO)).thenReturn(null);
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .body(productPOJO)
        .post("api/v1/products")
        .then()
        .assertThat()
        .statusCode(400)
        .statusLine("400 Supplier Not Found");
    verify(productService, times(1)).addNewProduct(Mockito.any(ProductPOJO.class));
  }

  public Product setUpObject() {
    Supplier supplier = new Supplier();
    Product product = new Product("ProductTest", "A description", 1, 4.99, IMAGE_URL);
    product.setId(1L);
    product.setSupplier(supplier);
    return product;
  }

  public ProductPOJO setUpObjectPOJO() {
    return new ProductPOJO("ProductTest", "A description", 1, 4.99, IMAGE_URL, 1L);
  }

  public ProductPOJO setUpInvalidObjectPOJO() {
    return new ProductPOJO("ProductTest", "A description", 1, 4.99, IMAGE_URL, -99L);
  }
}
