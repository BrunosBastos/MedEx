package tqs.medex.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.medex.entity.*;
import tqs.medex.pojo.CreateOrderPOJO;
import tqs.medex.repository.ClientRepository;
import tqs.medex.repository.OrderProductRepository;
import tqs.medex.repository.OrderRepository;
import tqs.medex.repository.ProductRepository;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock(lenient = true)
    private ProductRepository productRepository;

    @Mock(lenient = true)
    private OrderRepository orderRepository;

    @Mock(lenient = true)
    private OrderProductRepository orderProductRepository;

    @Mock(lenient = true)
    private ClientRepository clientRepository;

    @InjectMocks
    private OrderService orderService;

    private CreateOrderPOJO newOrder;
    private Product p1;
    private Product p2;
    private Order validOrder;
    private OrderProduct orderp1;
    private OrderProduct orderp2;

    @BeforeEach
    void setUp() {

        newOrder = new CreateOrderPOJO(50, 20, Map.of(1L, 10, 2L, 5));

        p1 = new Product("ProductTest1", "A description", 20, 4.99, "");
        p2 = new Product("ProductTest2", "A description", 10, 4.99, "");

        p1.setId(1L);
        p2.setId(2L);

        validOrder = new Order(newOrder.getLat(), newOrder.getLon());

        orderp1 = new OrderProduct(validOrder, p1, 10);
        orderp2 = new OrderProduct(validOrder, p2, 5);

        when(productRepository.findById(1L)).thenReturn(Optional.of(p1));
        when(productRepository.findById(2L)).thenReturn(Optional.of(p2));
        when(productRepository.findById(3L)).thenReturn(Optional.empty());

        when(orderRepository.save(validOrder)).thenReturn(validOrder);
        when(orderProductRepository.save(orderp1)).thenReturn(orderp1);
        when(orderProductRepository.save(orderp2)).thenReturn(orderp2);
        when(orderProductRepository.save(orderp1).getProduct().getId()).thenReturn(1L);
        when(orderProductRepository.save(orderp2).getProduct().getId()).thenReturn(2L);

    }

    @Test
    void whenAddNewOrderWithValidData_thenReturnOrder() {

        var order = orderService.addNewOrder(newOrder, 1L);
        assertThat(order.isDelivered()).isFalse();
        assertThat(order.getLat()).isEqualTo(newOrder.getLat());
        assertThat(order.getLon()).isEqualTo(newOrder.getLon());
        assertThat(order.getProducts()).hasSize(2);
        assertThat(order.getProducts()).hasOnlyElementsOfType(OrderProduct.class);

    }

    @Test
    void whenAddNewOrderWithInvalidProductAmount_thenReturnNull() {
        CreateOrderPOJO invalidOrderByProductAmount = new CreateOrderPOJO(10, 20, Map.of(1L, 300, 2L, 5));

        var order = orderService.addNewOrder(invalidOrderByProductAmount, 1L);
        assertThat(order).isNull();
    }

    @Test
    void whenAddNewProductWithInvalidProductId_thenReturnNull() {
        CreateOrderPOJO invalidOrderByProductId = new CreateOrderPOJO(10, 20, Map.of(3L, 10, 2L, 5));

        var order = orderService.addNewOrder(invalidOrderByProductId, 1L);
        assertThat(order).isNull();

    }

}
