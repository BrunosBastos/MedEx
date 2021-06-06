package tqs.medex.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.medex.repository.OrderRepository;
import tqs.medex.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock(lenient = true)
    private ProductRepository productRepository;

    @Mock(lenient = true)
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {



    }

    @Test
    void whenAddNewOrderWithValidData_thenReturnOrder() {






    }




}
