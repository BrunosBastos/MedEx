package tqs.medex.controller;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import tqs.medex.entity.Product;
import tqs.medex.entity.Supplier;
import tqs.medex.pojo.ProductPOJO;
import tqs.medex.repository.ProductRepository;
import tqs.medex.repository.SupplierRepository;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductControllerIT {

  @Autowired private MockMvc mvc;
  @Autowired private ProductRepository productRepository;
  @Autowired private SupplierRepository supplierRepository;

  @BeforeEach
  void setUp() {
    RestAssuredMockMvc.mockMvc(mvc);
  }

  @Test
  @WithMockUser(value = "test")
  void whenGetProducts_thenReturnValidResponse() {
    Product product = getExistingObject();
    Product product2 = productRepository.findByName("ProductTest2").orElse(null);
    Supplier supplier = supplierRepository.findByName("Pharmacy").orElse(null);

    RestAssuredMockMvc.given()
        .when()
        .get("api/v1/products")
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .and()
        .body("", hasSize(2))
        .and()
        .body("[0].name", is(product.getName()))
        .and()
        .body("[0].price", is((float) product.getPrice()))
        .and()
        .body("[1].name", is(product2.getName()))
        .and()
        .body("[1].price", is((float) product2.getPrice()))
        .and()
        .body("[0].supplier.id", is(supplier.getId().intValue()))
        .and()
        .body("[1].supplier.id", is(supplier.getId().intValue()));
  }

  @Test
  @WithMockUser(value = "test")
  void whenGetProductById_thenReturnProduct() {
    Product product = getExistingObject();
    long id = product.getId();
    RestAssuredMockMvc.given()
        .when()
        .get("/api/v1/products/" + id)
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .and()
        .body("name", is(product.getName()))
        .and()
        .body("price", is((float) product.getPrice()));
  }

  @Test
  @WithMockUser(value = "test")
  void whenGetProductByInvalidId_thenReturnProduct() {
    RestAssuredMockMvc.given()
        .when()
        .get("/api/v1/products/" + -99)
        .then()
        .assertThat()
        .statusCode(400)
        .statusLine("400 Product Not Found");
  }

  @Test
  @WithMockUser(value = "test")
  void whenAddProduct_thenReturnProduct() {
    Product product = getExistingObject();

    ProductPOJO productPOJO =
        new ProductPOJO(
            "ProductTest", "A description", 1, 4.99, null, product.getSupplier().getId());
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .body(productPOJO)
        .post("api/v1/products")
        .then()
        .assertThat()
        .statusCode(201)
        .contentType(ContentType.JSON)
        .and()
        .body("name", is(product.getName()))
        .and()
        .body("stock", is(product.getStock()))
        .and()
        .body("price", is((float) product.getPrice()))
        .and()
        .body("supplier.id", is(product.getSupplier().getId().intValue()));
  }

  @Test
  @WithMockUser(value = "test")
  void whenAddInvalidProductSupplier_thenReturnBadRequest() {
    Product product = getExistingObject();
    ProductPOJO productPOJO =
        new ProductPOJO(
            product.getName(),
            product.getDescription(),
            product.getStock(),
            product.getPrice(),
            product.getImageUrl(),
            -2);
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .body(productPOJO)
        .post("api/v1/products")
        .then()
        .assertThat()
        .statusCode(400)
        .statusLine("400 Supplier Not Found");
  }

  @Test
  @WithMockUser(value = "test")
  void whenUpdateProduct_thenReturnValidResponse() {
    Product product = getExistingObject();
    long id = product.getId();
    ProductPOJO productPOJO = setUpObjectPojo();

    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .body(productPOJO)
        .put("api/v1/products/" + id)
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .and()
        .body("name", is(productPOJO.getName()));
  }

  @Test
  @WithMockUser(value = "test")
  void whenUpdateProductByInvalidId_thenReturnBadRequest() {
    ProductPOJO productPOJO = setUpObjectPojo();

    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .body(productPOJO)
        .put("api/v1/products/-99")
        .then()
        .assertThat()
        .statusCode(400)
        .statusLine("400 Product Not Found");
  }

  public ProductPOJO setUpObjectPojo() {
    return new ProductPOJO("ProductUpdated", "descriptionUpdated", 5, 2.99, null, 1L);
  }

  public Product getExistingObject() {
    return productRepository.findByName("ProductTest").orElse(null);
  }
}
