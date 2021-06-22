package tqs.medex.controller;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import tqs.medex.entity.Purchase;
import tqs.medex.entity.PurchaseProduct;
import tqs.medex.entity.User;
import tqs.medex.pojo.CreatePurchasePOJO;
import tqs.medex.repository.ProductRepository;
import tqs.medex.repository.PurchaseRepository;
import tqs.medex.repository.UserRepository;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PurchaseControllerIT {

  @Autowired private MockMvc mvc;

  @Autowired private ProductRepository productRepository;

  @Autowired private PurchaseRepository purchaseRepository;

  @Autowired private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    RestAssuredMockMvc.mockMvc(mvc);
  }

  @Test
  @WithMockUser(value = "henrique@gmail.com")
  void whenGetPurchasesAndIsNotSuperUser_thenReturnUserPurchases() {
    User user = userRepository.findByEmail("henrique@gmail.com").orElse(null);
    assertThat(user).isNotNull();
    RestAssuredMockMvc.given()
        .get("api/v1/purchases")
        .then()
        .assertThat()
        .statusCode(200)
        .body("$.size()", is(1))
        .and()
        .body("user.userId", everyItem(is(user.getUserId().intValue())));
  }

  @Test
  @WithMockUser(value = "clara@gmail.com")
  void whenGetPurchasesAndIsSuperUser_thenReturnAllPurchases() {
    User user = userRepository.findByEmail("clara@gmail.com").orElse(null);
    assertThat(user).isNotNull();
    var purchases = purchaseRepository.findAll();
    RestAssuredMockMvc.given()
        .get("api/v1/purchases")
        .then()
        .assertThat()
        .statusCode(200)
        .and()
        .body("", hasSize(purchases.size()));
  }

  @Test
  @WithMockUser(value = "henrique@gmail.com")
  void whenGetPurchaseByIdAndIsNotSuperUser_thenReturnUserPurchase() {
    User user = userRepository.findByEmail("henrique@gmail.com").orElse(null);
    assertThat(user).isNotNull();
    Purchase purchase =
        purchaseRepository.findByIdAndUser_UserId(1L, user.getUserId()).orElse(null);
    assertThat(purchase).isNotNull();
    RestAssuredMockMvc.given()
        .get("api/v1/purchases/" + 1L)
        .then()
        .assertThat()
        .statusCode(200)
        .and()
        .and()
        .body("id", is(purchase.getId().intValue()))
        .body("products.product.id", hasSize(purchase.getProducts().size()))
        .and()
        .body("user.email", is(user.getEmail()));
  }

  @Test
  @WithMockUser(value = "clara@gmail.com")
  void whenGetPurchaseByIdAndIsSuperUser_thenReturnPurchase() {
    User user = userRepository.findByEmail("clara@gmail.com").orElse(null);
    assertThat(user).isNotNull();
    Purchase purchase = purchaseRepository.findById(1L).orElse(null);
    assertThat(purchase).isNotNull();
    RestAssuredMockMvc.given()
        .get("api/v1/purchases/" + 1L)
        .then()
        .assertThat()
        .statusCode(200)
        .and()
        .and()
        .body("id", is(purchase.getId().intValue()))
        .body("products.product.id", hasSize(purchase.getProducts().size()));
  }

  @Test
  @WithMockUser(value = "henrique@gmail.com")
  void whenGetNonUserPurchaseByIdAndIsNotSuperUser_thenReturnBadRequest() {
    User user = userRepository.findByEmail("henrique@gmail.com").orElse(null);
    assertThat(user).isNotNull();
    RestAssuredMockMvc.given()
        .get("api/v1/purchases/" + 2L)
        .then()
        .assertThat()
        .statusCode(400)
        .statusLine("400 Purchase not found");
  }

  @Test
  @Disabled
  @WithMockUser(value = "henrique@gmail.com")
  void whenAddPurchaseWithValidData_thenReturnValidResponse() {

    CreatePurchasePOJO purchasePOJO = new CreatePurchasePOJO(10, 20, Map.of(1L, 1, 2L, 2));

    RestAssuredMockMvc.given()
        .contentType(ContentType.JSON)
        .body(purchasePOJO)
        .when()
        .post("api/v1/purchases")
        .then()
        .assertThat()
        .statusCode(201)
        .body("lat", is((float) 10))
        .body("lon", is((float) 20))
        .body("products", hasSize(2))
        .and()
        .body("user.email", is("henrique@gmail.com"));

    var purchase = purchaseRepository.findById(1L).get();
    assertThat(purchase.getProducts()).hasSize(2);
  }

  @Test
  @WithMockUser(value = "henrique@gmail.com")
  void whenAddPurchaseWithInvalidProducts_thenReturnBadRequest() {
    CreatePurchasePOJO purchasePOJO = new CreatePurchasePOJO(10, 20, Map.of(10L, 10, 2L, 5));

    RestAssuredMockMvc.given()
        .contentType(ContentType.JSON)
        .body(purchasePOJO)
        .when()
        .post("api/v1/purchases")
        .then()
        .assertThat()
        .statusCode(400);
  }

  @Test
  @WithMockUser(value = "henrique@gmail.com")
  void whenAddPurchaseWithInvalidProductAmount_thenReturnBadRequest() {
    CreatePurchasePOJO purchasePOJO = new CreatePurchasePOJO(10, 20, Map.of(1L, 30, 2L, 5));

    RestAssuredMockMvc.given()
        .contentType(ContentType.JSON)
        .body(purchasePOJO)
        .when()
        .post("api/v1/purchases")
        .then()
        .assertThat()
        .statusCode(400);
  }

  @Test
  @WithMockUser(value = "henrique@gmail.com")
  void whenGetPurchaseHistoryBySupplierWithInvalidSupplier_thenReturnEmptyList() {

    RestAssuredMockMvc.given()
        .contentType(ContentType.JSON)
        .when()
        .get("api/v1/purchases/products/1000?page=0&recent=true")
        .then()
        .assertThat()
        .statusCode(200)
        .body("$.size()", is(0));
  }

  @Test
  @WithMockUser(value = "henrique@gmail.com")
  void whenGetPurchaseHistoryBySupplierWithValidSupplierDescending_thenReturnEmptyList() {

    var history =
        RestAssuredMockMvc.given()
            .contentType(ContentType.JSON)
            .when()
            .get("api/v1/purchases/products/1?page=0&recent=true")
            .then()
            .assertThat()
            .statusCode(200)
            .body("$.size()", is(3))
            .extract()
            .jsonPath()
            .getList("", PurchaseProduct.class);

    assertThat(history.get(0).getPurchase().getId())
        .isGreaterThanOrEqualTo(history.get(1).getPurchase().getId());
    assertThat(history.get(1).getPurchase().getId())
        .isGreaterThanOrEqualTo(history.get(2).getPurchase().getId());
  }

  @Test
  @WithMockUser(value = "henrique@gmail.com")
  void whenGetPurchaseHistoryBySupplierWithValidSupplierAscending_thenReturnEmptyList() {

    var history =
        RestAssuredMockMvc.given()
            .contentType(ContentType.JSON)
            .when()
            .get("api/v1/purchases/products/1?page=0&recent=false")
            .then()
            .assertThat()
            .statusCode(200)
            .body("$.size()", is(3))
            .extract()
            .jsonPath()
            .getList("", PurchaseProduct.class);

    assertThat(history.get(0).getPurchase().getId())
        .isLessThanOrEqualTo(history.get(1).getPurchase().getId());
    assertThat(history.get(1).getPurchase().getId())
        .isLessThanOrEqualTo(history.get(2).getPurchase().getId());
  }

  @Test
  @WithMockUser(value = "henrique@gmail.com")
  void whenUpdatePurchaseWithInvalidId_thenReturnError() {

    RestAssuredMockMvc.given()
        .contentType(ContentType.JSON)
        .when()
        .put("api/v1/purchases/1000")
        .then()
        .assertThat()
        .statusCode(400)
        .statusLine("400 Purchase does not exist");
  }

  @Test
  @WithMockUser(value = "henrique@gmail.com")
  void whenUpdatePurchase_thenReturnPurchase() {

    RestAssuredMockMvc.given()
        .contentType(ContentType.JSON)
        .when()
        .put("api/v1/purchases/2")
        .then()
        .assertThat()
        .statusCode(200)
        .body("delivered", is(true));
  }
}
