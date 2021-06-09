package tqs.medex.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import tqs.medex.entity.Product;
import tqs.medex.entity.Purchase;
import tqs.medex.entity.PurchaseProduct;
import tqs.medex.entity.User;
import tqs.medex.pojo.CreatePurchasePOJO;
import tqs.medex.repository.ProductRepository;
import tqs.medex.repository.PurchaseProductRepository;
import tqs.medex.repository.PurchaseRepository;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceTest {

  @Mock(lenient = true)
  private ProductRepository productRepository;

  @Mock(lenient = true)
  private PurchaseRepository orderRepository;

  @Mock(lenient = true)
  private PurchaseProductRepository orderProductRepository;

  @InjectMocks private PurchaseService orderService;

  private CreatePurchasePOJO newOrder;
  private Product p1;
  private Product p2;
  private Purchase validOrder;
  private PurchaseProduct orderp1;
  private PurchaseProduct orderp2;

  @BeforeEach
  void setUp() {

    newOrder = new CreatePurchasePOJO(50, 20, Map.of(1L, 10, 2L, 5));

    p1 = new Product(1, "ProductTest1", "A description", 20, 4.99, "");
    p2 = new Product(2, "ProductTest2", "A description", 10, 4.99, "");

    p1.setId(1L);
    p2.setId(2L);

    validOrder = new Purchase(newOrder.getLat(), newOrder.getLon());

    orderp1 = new PurchaseProduct(validOrder, p1, 10);
    orderp2 = new PurchaseProduct(validOrder, p2, 5);

    when(productRepository.findById(1L)).thenReturn(Optional.of(p1));
    when(productRepository.findById(2L)).thenReturn(Optional.of(p2));
    when(productRepository.findById(3L)).thenReturn(Optional.empty());
  }

  @Test
  void whenAddNewOrderWithValidData_thenReturnOrder() {

    var order = orderService.addNewPurchase(newOrder, new User());
    assertThat(order.isDelivered()).isFalse();
    assertThat(order.getLat()).isEqualTo(newOrder.getLat());
    assertThat(order.getLon()).isEqualTo(newOrder.getLon());
    assertThat(order.getProducts()).hasSize(2);
    assertThat(order.getProducts()).hasOnlyElementsOfType(PurchaseProduct.class);

    for (PurchaseProduct purchaseProduct : order.getProducts()) {

      var product = purchaseProduct.getProduct();
      if (product.getId() == 1L) {
        assertThat(product.getStock()).isEqualTo(10);
      } else {
        assertThat(product.getStock()).isEqualTo(5);
      }
    }
  }

  @Test
  void whenAddNewOrderWithInvalidProductAmount_thenReturnNull() {
    CreatePurchasePOJO invalidOrderByProductAmount =
        new CreatePurchasePOJO(10, 20, Map.of(1L, 300, 2L, 5));

    var order = orderService.addNewPurchase(invalidOrderByProductAmount, new User());
    assertThat(order).isNull();
  }

  @Test
  @WithMockUser(value = "henrique@gmail.com")
  void whenAddNewProductWithInvalidProductId_thenReturnNull() {
    CreatePurchasePOJO invalidOrderByProductId =
        new CreatePurchasePOJO(10, 20, Map.of(3L, 10, 2L, 5));

    var order = orderService.addNewPurchase(invalidOrderByProductId, new User());
    assertThat(order).isNull();
  }
}
