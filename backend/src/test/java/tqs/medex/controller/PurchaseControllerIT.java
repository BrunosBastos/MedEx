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
import tqs.medex.entity.Purchase;
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
  void whenGetPurchasesAndisNotSuperUser_thenReturnUserPurchases(){
    User user = userRepository.findByEmail("henrique@gmail.com").orElse(null);
    assertThat(user).isNotNull();
    var purchases = purchaseRepository.findAllByUser_UserId(user.getUserId());
    RestAssuredMockMvc.given()
            .get("api/v1/purchases")
            .then()
            .assertThat()
            .statusCode(200)
            .body("", hasSize(purchases.size()))
            .and()
            .body("user.userId", everyItem(is(user.getUserId().intValue())));
  }

  @Test
  @WithMockUser(value = "clara@gmail.com")
  void whenGetPurchasesAndisSuperUser_thenReturnAllPurchases(){
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
}
