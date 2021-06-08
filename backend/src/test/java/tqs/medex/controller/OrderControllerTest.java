package tqs.medex.controller;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import tqs.medex.entity.*;
import tqs.medex.pojo.CreateOrderPOJO;
import tqs.medex.repository.UserRepository;
import tqs.medex.security.CustomUserDetailsService;
import tqs.medex.service.OrderService;
import java.util.Arrays;
import java.util.Map;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class OrderControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mvc);
    }

    @Test
    @WithUserDetails(value = "test")
    void whenAddNewOrder_thenReturnOrder() {
        var newOrder = setUpAddOrderValid();

        var order = setUpValidOrder();

        when(orderService.addNewOrder(any(CreateOrderPOJO.class), anyLong())).thenReturn(order);
        RestAssuredMockMvc.given().body(newOrder).contentType(ContentType.JSON)
                .when()
                .post("api/v1/orders")
                .then()
                .assertThat()
                .statusCode(201).and().body("lat", is((float)10))
                .and().body("lon", is((float)20))
                .and().body("products", hasSize(2))
                .and().body("products[0].productAmount", is(10))
                .and().body("products[1].productAmount", is(20));

        verify(orderService, times(1)).addNewOrder(any(), 1);
    }

    @Test
    void whenAddOrderWithInvalidProducts_thenReturnBadRequest() {

        var newOrder = setUpAddOrderInvalid();

        when(orderService.addNewOrder(any(CreateOrderPOJO.class), anyLong())).thenReturn(null);
        RestAssuredMockMvc.given().body(newOrder).contentType(ContentType.JSON)
                .when()
                .post("api/v1/orders")
                .then()
                .assertThat()
                .statusCode(400).statusLine("400 Product Not Found");

        verify(orderService, times(1)).addNewOrder(any(), 1);


    }

    CreateOrderPOJO setUpAddOrderValid() {
        return new CreateOrderPOJO(10, 20, Map.of(1L, 10, 2L, 4));
    }

    CreateOrderPOJO setUpAddOrderInvalid() {
        return new CreateOrderPOJO(10, 20, Map.of(1000L, 0, 1L, 4));
    }

    Order setUpValidOrder() {
        Product p1 = new Product();
        p1.setId(1L);
        Product p2 = new Product();
        p2.setId(2L);

        Order order = new Order(10, 20);

        OrderProduct op1 = new OrderProduct(order, p1, 10);

        OrderProduct op2 = new OrderProduct(order, p2, 20);

        order.setProducts(Arrays.asList(op1, op2));

        return order;
    }


}
