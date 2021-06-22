package tqs.medex.controller;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import tqs.medex.entity.Product;
import tqs.medex.entity.Purchase;
import tqs.medex.entity.PurchaseProduct;
import tqs.medex.entity.User;
import tqs.medex.pojo.CreatePurchasePOJO;
import tqs.medex.repository.UserRepository;
import tqs.medex.service.PurchaseService;

import java.util.*;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class PurchaseControllerTest {
  @Autowired private MockMvc mvc;

  @MockBean private PurchaseService orderService;

  @MockBean private UserRepository userRepository;

  private User user;
  private User admin;

  @BeforeEach
  void setUp() {
    RestAssuredMockMvc.mockMvc(mvc);

    user = new User();
    user.setUserId(1L);
    user.setEmail("henrique@gmail.com");
    admin = new User();
    admin.setEmail("clara@gmail.com");
    admin.setSuperUser(true);
    when(userRepository.findByEmail("henrique@gmail.com")).thenReturn(Optional.of(user));
    when(userRepository.findByEmail("clara@gmail.com")).thenReturn(Optional.of(admin));
  }

  @Test
  @WithMockUser(value = "clara@gmail.com")
  void whenGetPurchasesAndisSuperUser_thenReturnUserPurchases() {
    var orders = setupMultipleValidOrdersVisibleforUser();
    when(orderService.getPurchases(any(User.class), any(), any())).thenReturn(orders);
    RestAssuredMockMvc.given()
        .when()
        .get("api/v1/purchases")
        .then()
        .assertThat()
        .statusCode(200)
        .and()
        .body("", hasSize(1))
        .and()
        .body("[0].lat", is((float) orders.get(0).getLat()))
        .and()
        .body("[0].lon", is((float) orders.get(0).getLon()))
        .and()
        .body("[0].products", hasSize(orders.get(0).getProducts().size()))
        .body("[0].user.email", is(orders.get(0).getUser().getEmail()));
    verify(userRepository, times(1)).findByEmail("clara@gmail.com");
    verify(orderService, times(1)).getPurchases(any(User.class), anyInt(), any());
  }

  @Test
  @WithMockUser(value = "henrique@gmail.com")
  void whenGetPurchasesAndisNotSuperUser_thenReturnUserPurchases() {
    var orders = setupMultipleValidOrdersVisibleforSuperUser();
    when(orderService.getPurchases(any(User.class), anyInt(), anyBoolean())).thenReturn(orders);
    RestAssuredMockMvc.given()
        .when()
        .get("api/v1/purchases")
        .then()
        .assertThat()
        .statusCode(200)
        .and()
        .body("", hasSize(2))
        .and()
        .body("[0].lat", is((float) orders.get(0).getLat()))
        .and()
        .body("[0].lon", is((float) orders.get(0).getLon()))
        .and()
        .body("[0].products", hasSize(orders.get(0).getProducts().size()))
        .and()
        .body("[1].products", hasSize(orders.get(1).getProducts().size()))
        .and()
        .body("[0].user.email", is(orders.get(0).getUser().getEmail()))
        .and()
        .body("[1].user.email", is(orders.get(1).getUser().getEmail()));
    verify(userRepository, times(1)).findByEmail("henrique@gmail.com");
    verify(orderService, times(1)).getPurchases(any(User.class), anyInt(), any());
  }

  @Test
  @WithMockUser(value = "henrique@gmail.com")
  void whenGetPurchaseByIdAndisNotSuperUser_thenReturnPurchase() {
    Purchase purchase = setUpValidOrder();
    purchase.setId(1L);
    when(orderService.getPurchaseDetails(Mockito.any(User.class), Mockito.anyLong()))
        .thenReturn(purchase);
    RestAssuredMockMvc.given()
        .when()
        .get("api/v1/purchases/" + 1L)
        .then()
        .assertThat()
        .statusCode(200)
        .and()
        .body("products.product.id", hasSize(2))
        .and()
        .body("user.email", is(user.getEmail()));
    verify(userRepository, times(1)).findByEmail("henrique@gmail.com");
    verify(orderService, times(1)).getPurchaseDetails(any(User.class), Mockito.anyLong());
  }

  @Test
  @WithMockUser(value = "henrique@gmail.com")
  void whenGetPurchaseByInvalidId_thenReturnBadRequest() {
    when(orderService.getPurchaseDetails(Mockito.any(User.class), Mockito.anyLong()))
        .thenReturn(null);
    RestAssuredMockMvc.given()
        .when()
        .get("api/v1/purchases/" + -99L)
        .then()
        .assertThat()
        .statusCode(400)
        .statusLine("400 Purchase not found");
    verify(userRepository, times(1)).findByEmail("henrique@gmail.com");
    verify(orderService, times(1)).getPurchaseDetails(any(User.class), Mockito.anyLong());
  }

  @Test
  @WithMockUser("clara@gmail.com")
  void whenGetPurchaseByIdAndisSuperUser_thenReturnPurchase() {
    Purchase purchase = setUpValidOrder();
    purchase.setId(1L);
    when(orderService.getPurchaseDetails(Mockito.any(User.class), Mockito.anyLong()))
        .thenReturn(purchase);
    RestAssuredMockMvc.given()
        .when()
        .get("api/v1/purchases/" + 1L)
        .then()
        .assertThat()
        .statusCode(200)
        .body("products.product.id", hasSize(2))
        .and()
        // checking that the admin can retrieve every users purchases
        .body("user.email", not(is(admin.getEmail())));
    verify(userRepository, times(1)).findByEmail("clara@gmail.com");
    verify(orderService, times(1)).getPurchaseDetails(any(User.class), Mockito.anyLong());
  }

  @Test
  @WithMockUser(value = "henrique@gmail.com")
  void whenAddNewOrder_thenReturnOrder() {
    var newOrder = setUpAddOrderValid();

    var order = setUpValidOrder();

    when(orderService.addNewPurchase(any(CreatePurchasePOJO.class), any(User.class)))
        .thenReturn(order);
    RestAssuredMockMvc.given()
        .body(newOrder)
        .contentType(ContentType.JSON)
        .when()
        .post("api/v1/purchases")
        .then()
        .assertThat()
        .statusCode(201)
        .and()
        .body("lat", is((float) 10))
        .and()
        .body("lon", is((float) 20))
        .and()
        .body("products", hasSize(2))
        .and()
        .body("products[0].productAmount", is(10))
        .and()
        .body("products[1].productAmount", is(20));

    verify(userRepository, times(1)).findByEmail("henrique@gmail.com");
    verify(orderService, times(1)).addNewPurchase(any(), any(User.class));
  }

  @Test
  @WithMockUser(value = "henrique@gmail.com")
  void whenAddOrderWithInvalidProducts_thenReturnBadRequest() {

    var newOrder = setUpAddOrderInvalid();

    when(orderService.addNewPurchase(any(CreatePurchasePOJO.class), any(User.class)))
        .thenReturn(null);
    RestAssuredMockMvc.given()
        .body(newOrder)
        .contentType(ContentType.JSON)
        .when()
        .post("api/v1/purchases")
        .then()
        .assertThat()
        .statusCode(400)
        .statusLine("400 Invalid Product Quantity");

    verify(orderService, times(1)).addNewPurchase(any(), any(User.class));
  }

  @Test
  @WithMockUser(value = "henrique@gmail.com")
  void whenGetPurchaseHistoryBySupplierWithInvalidSupplier_thenReturnEmptyList() {

    when(orderService.getPurchasedProductsBySupplier(1000L, 0, true))
        .thenReturn(Collections.emptyList());
    RestAssuredMockMvc.given()
        .contentType(ContentType.JSON)
        .when()
        .get("api/v1/purchases/products/1000?page=0&recent=true")
        .then()
        .assertThat()
        .statusCode(200)
        .body("$.size()", is(0));
    ;
    verify(orderService, times(1)).getPurchasedProductsBySupplier(1000L, 0, true);
  }

  @Test
  @WithMockUser(value = "henrique@gmail.com")
  void whenGetPurchaseHistoryBySupplierWithValidSupplier_thenReturnPurchaseList() {

    var purchaseProduct1 = new PurchaseProduct();
    var purchaseProduct2 = new PurchaseProduct();
    var purchaseProduct3 = new PurchaseProduct();

    when(orderService.getPurchasedProductsBySupplier(1L, 0, true))
        .thenReturn(Arrays.asList(purchaseProduct1, purchaseProduct2, purchaseProduct3));
    RestAssuredMockMvc.given()
        .contentType(ContentType.JSON)
        .when()
        .get("api/v1/purchases/products/1?page=0&recent=true")
        .then()
        .assertThat()
        .statusCode(200)
        .body("$.size()", is(3));
    ;
    verify(orderService, times(1)).getPurchasedProductsBySupplier(1L, 0, true);
  }

  @Test
  @WithMockUser(value = "henrique@gmail.com")
  void whenUpdatePurchaseWithInvalidId_thenReturnError() {

    when(orderService.updatePurchase(1000L)).thenReturn(null);
    RestAssuredMockMvc.given()
        .contentType(ContentType.JSON)
        .when()
        .put("api/v1/purchases/1000")
        .then()
        .assertThat()
        .statusCode(400);
    verify(orderService, times(1)).updatePurchase(1000L);
  }

  @Test
  @WithMockUser(value = "henrique@gmail.com")
  void whenUpdatePurchase_thenReturnPurchase() {

    when(orderService.updatePurchase(1L)).thenReturn(setUpValidOrder());
    RestAssuredMockMvc.given()
        .contentType(ContentType.JSON)
        .when()
        .put("api/v1/purchases/1")
        .then()
        .assertThat()
        .statusCode(200);
    verify(orderService, times(1)).updatePurchase(1L);
  }

  CreatePurchasePOJO setUpAddOrderValid() {
    return new CreatePurchasePOJO(10, 20, Map.of(1L, 10, 2L, 4));
  }

  CreatePurchasePOJO setUpAddOrderInvalid() {
    return new CreatePurchasePOJO(10, 20, Map.of(1000L, 0, 1L, 4));
  }

  Purchase setUpValidOrder() {
    Product p1 = new Product();
    p1.setId(1L);
    Product p2 = new Product();
    p2.setId(2L);

    Purchase order = new Purchase(10, 20);

    PurchaseProduct op1 = new PurchaseProduct(order, p1, 10);

    PurchaseProduct op2 = new PurchaseProduct(order, p2, 20);

    order.setProducts(Arrays.asList(op1, op2));
    order.setUser(user);
    return order;
  }

  List<Purchase> setupMultipleValidOrdersVisibleforSuperUser() {
    Product p1 = new Product();
    p1.setId(1L);
    Product p2 = new Product();
    p2.setId(2L);
    Purchase order = new Purchase(10, 20);
    Purchase order2 = new Purchase(30, 40);
    PurchaseProduct op1 = new PurchaseProduct(order, p1, 10);

    PurchaseProduct op2 = new PurchaseProduct(order, p2, 20);
    order.setUser(user);
    order2.setUser(admin);
    order.setProducts(Arrays.asList(op1, op2));
    return Arrays.asList(order, order2);
  }

  List<Purchase> setupMultipleValidOrdersVisibleforUser() {
    Product p1 = new Product();
    p1.setId(1L);
    Product p2 = new Product();
    p2.setId(2L);
    Purchase order = new Purchase(10, 20);
    Purchase order2 = new Purchase(30, 40);
    PurchaseProduct op1 = new PurchaseProduct(order, p1, 10);

    PurchaseProduct op2 = new PurchaseProduct(order, p2, 20);
    order.setUser(user);
    order2.setUser(admin);
    order.setProducts(Arrays.asList(op1, op2));
    return Collections.singletonList(order);
  }
}
