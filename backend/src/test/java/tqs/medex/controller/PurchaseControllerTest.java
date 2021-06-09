package tqs.medex.controller;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class PurchaseControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private PurchaseService orderService;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mvc);

        User user = new User();
        user.setUserId(1L);
        user.setEmail("henrique@gmail.com");

        when(userRepository.findByEmail("henrique@gmail.com")).thenReturn(Optional.of(user));
    }

    @Test
    @WithMockUser(value = "henrique@gmail.com")
    void whenAddNewOrder_thenReturnOrder() {
        var newOrder = setUpAddOrderValid();

        var order = setUpValidOrder();

        when(orderService.addNewPurchase(any(CreatePurchasePOJO.class), any(User.class))).thenReturn(order);
        RestAssuredMockMvc.given().body(newOrder).contentType(ContentType.JSON)
                .when()
                .post("api/v1/purchases")
                .then()
                .assertThat()
                .statusCode(201).and().body("lat", is((float) 10))
                .and().body("lon", is((float) 20))
                .and().body("products", hasSize(2))
                .and().body("products[0].productAmount", is(10))
                .and().body("products[1].productAmount", is(20));

        verify(userRepository, times(1)).findByEmail("henrique@gmail.com");
        verify(orderService, times(1)).addNewPurchase(any(), any(User.class));
    }

    @Test
    @WithMockUser(value = "henrique@gmail.com")
    void whenAddOrderWithInvalidProducts_thenReturnBadRequest() {

        var newOrder = setUpAddOrderInvalid();

        when(orderService.addNewPurchase(any(CreatePurchasePOJO.class), any(User.class))).thenReturn(null);
        RestAssuredMockMvc.given().body(newOrder).contentType(ContentType.JSON)
                .when()
                .post("api/v1/purchases")
                .then()
                .assertThat()
                .statusCode(400).statusLine("400 Invalid Product Quantity");

        verify(orderService, times(1)).addNewPurchase(any(), any(User.class));


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

        return order;
    }


}
