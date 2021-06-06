package tqs.medex.controller;

import com.shapesecurity.salvation2.Values.Hash;
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
import tqs.medex.entity.Order;
import tqs.medex.entity.OrderProduct;
import tqs.medex.entity.Product;
import tqs.medex.pojo.CreateOrderPOJO;
import tqs.medex.service.OrderService;
import tqs.medex.service.ProductService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class OrderControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mvc);
    }

    @Test
    @WithMockUser(value = "test")
    void whenAddNewOrder_thenReturnOrder() {

        var newOrder = setUpAddOrderValid();

        var order = setUpValidOrder();

        when(orderService.addNewOrder(any(CreateOrderPOJO.class))).thenReturn(order);
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

        verify(orderService, times(1)).addNewOrder(any());
    }

    @Test
    @WithMockUser(value = "test")
    void whenAddOrderWithInvalidProducts_thenReturnBadRequest() {

        var newOrder = setUpAddOrderInvalid();

        when(orderService.addNewOrder(any(CreateOrderPOJO.class))).thenReturn(null);
        RestAssuredMockMvc.given().body(newOrder).contentType(ContentType.JSON)
                .when()
                .post("api/v1/orders")
                .then()
                .assertThat()
                .statusCode(400).statusLine("400 Product Not Found");

        verify(orderService, times(1)).addNewOrder(any());


    }

    CreateOrderPOJO setUpAddOrderValid() {
        CreateOrderPOJO newOrder = new CreateOrderPOJO();
        newOrder.setLat(10);
        newOrder.setLon(20);
        newOrder.setProducts(Map.of(1, 10, 2, 4));

        return newOrder;
    }

    CreateOrderPOJO setUpAddOrderInvalid() {
        CreateOrderPOJO newOrder = new CreateOrderPOJO();
        newOrder.setLat(10);
        newOrder.setLon(20);
        newOrder.setProducts(Map.of(1000, 0, 1, 4));

        return newOrder;
    }

    Order setUpValidOrder() {
        Product p1 = new Product();
        p1.setId(1L);
        Product p2 = new Product();
        p2.setId(2L);

        OrderProduct op1 = new OrderProduct();
        op1.setProduct(p1);
        op1.setProductAmount(10);

        OrderProduct op2 = new OrderProduct();
        op2.setProduct(p2);
        op2.setProductAmount(20);

        Order order = new Order();
        order.setLat(10);
        order.setLon(20);
        order.setProducts(Arrays.asList(op1, op2));

        return order;
    }


}
