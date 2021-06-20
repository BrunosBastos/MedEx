package tqs.medex.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import tqs.medex.entity.Product;
import tqs.medex.entity.Purchase;
import tqs.medex.entity.PurchaseProduct;
import tqs.medex.entity.User;
import tqs.medex.pojo.CreatePurchasePOJO;
import tqs.medex.repository.ProductRepository;
import tqs.medex.repository.PurchaseProductRepository;
import tqs.medex.repository.PurchaseRepository;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceTest {

  @Mock(lenient = true)
  private ProductRepository productRepository;

  @Mock(lenient = true)
  private PurchaseRepository purchaseRepository;

  @Mock(lenient = true)
  private PurchaseProductRepository purchaseProductRepository;

  @InjectMocks private PurchaseService orderService;

  private CreatePurchasePOJO newOrder;
  private User user;
  private User superuser;
  private PurchaseProduct orderp1;
  private PurchaseProduct orderp2;

  @BeforeEach
  void setUp() {
    user = new User();
    superuser = new User();
    user.setUserId(1L);
    superuser.setUserId(2L);
    superuser.setSuperUser(true);
    newOrder = new CreatePurchasePOJO(50, 20, Map.of(1L, 10, 2L, 5));

    Product p1 = new Product(1, "ProductTest1", "A description", 20, 4.99, "");
    Product p2 = new Product(2, "ProductTest2", "A description", 10, 4.99, "");

    p1.setId(1L);
    p2.setId(2L);

    Purchase validOrder = new Purchase(newOrder.getLat(), newOrder.getLon());

    orderp1 = new PurchaseProduct(validOrder, p1, 10);
    orderp2 = new PurchaseProduct(validOrder, p2, 5);

    when(productRepository.findById(1L)).thenReturn(Optional.of(p1));
    when(productRepository.findById(2L)).thenReturn(Optional.of(p2));
    when(productRepository.findById(3L)).thenReturn(Optional.empty());
  }

  @Test
  void whenGetPurchasesWithNonSuperUser_thenReturnUserPurchases() {
    var purchases = setupPurchasesVisibleforUser();
    Page<Purchase> page = new PageImpl<>(purchases);

    when(purchaseRepository.findAllByUser_UserId(Mockito.anyLong(), any())).thenReturn(page);
    var purchase_mocked_list = orderService.getPurchases(user, 0, true);
    assertThat(purchase_mocked_list)
        .hasSize(1)
        .extracting(Purchase::getId)
        .containsAll(purchases.stream().map(Purchase::getId).collect(Collectors.toList()));
    // all purchases retrieved  should only  be of the normal client passed as argument
    assertThat(purchase_mocked_list)
        .extracting(Purchase::getUser)
        .containsAll(purchases.stream().map(Purchase::getUser).collect(Collectors.toList()));
    verifyFindAllByUserIdIsCalledOnce();
  }

  @Test
  void whenGetPurchaseByIdWithSuperUser_thenReturnPurchase() {

    var purchase = setupPurchaseVisibleforUserAndSuperUser();
    when(purchaseRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(purchase));
    assertThat(purchase).isNotNull();
    var purchase_mocked_object = orderService.getPurchaseDetails(superuser, purchase.getId());
    assertThat(purchase_mocked_object).isNotNull();
    assertThat(purchase_mocked_object.getId()).isEqualTo(purchase.getId());
    verifyFindByIdIsCalledOnce();
  }

  @Test
  void whenGetPurchaseByIdWithNonSuperUser_thenReturnUserPurchase() {
    var purchase = setupPurchaseVisibleforUserAndSuperUser();
    when(purchaseRepository.findByIdAndUser_UserId(Mockito.anyLong(), Mockito.anyLong()))
        .thenReturn(Optional.ofNullable(purchase));
    assertThat(purchase).isNotNull();
    var purchase_mocked_object = orderService.getPurchaseDetails(user, purchase.getId());
    assertThat(purchase_mocked_object).isNotNull();
    assertThat(purchase_mocked_object.getId()).isEqualTo(purchase.getId());
    assertThat(purchase_mocked_object.getUser().getUserId()).isEqualTo(user.getUserId());
    verifyFindAllByPurchaseIdAndUserIdisCalledOnce();
  }

  @Test
  void whenGetNonUserPurchaseById_thenReturnNull() {
    var purchase = setupPurchaseVisibleforSuperUser();
    when(purchaseRepository.findByIdAndUser_UserId(Mockito.anyLong(), Mockito.anyLong()))
        .thenReturn(Optional.empty());
    var purchase_mocked_object = orderService.getPurchaseDetails(user, purchase.getId());
    assertThat(purchase_mocked_object).isNull();
    verifyFindAllByPurchaseIdAndUserIdisCalledOnce();
  }

  @Test
  void whenGetPurchasesWithSuperUser_thenReturnAllPurchases() {
    var purchases = setupPurchasesVisibleforSuperUser();
    Page<Purchase> page = new PageImpl<>(purchases);
    when(purchaseRepository.findAll(any(Pageable.class))).thenReturn(page);
    var purchase_mocked_list = orderService.getPurchases(superuser, 0, true);
    assertThat(purchase_mocked_list)
        .hasSize(2)
        .extracting(Purchase::getId)
        .containsAll(purchases.stream().map(Purchase::getId).collect(Collectors.toList()));

    verifyFindAllisCalledOnce();
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

  @Test
  @WithMockUser(value = "clara@gmail.com")
  void whenGetPurchaseHistoryBySupplier_thenReturnPurchaseList() {

    Page<PurchaseProduct> page = new PageImpl<>(Arrays.asList(orderp1, orderp2));
    when(purchaseProductRepository.findAllByProductSupplierId(any(), any())).thenReturn(page);
    var orderProducts = orderService.getPurchasedProductsBySupplier(1L, 0, true);
    assertThat(orderProducts.size()).isEqualTo(2);
  }

  Purchase setupPurchaseVisibleforUserAndSuperUser() {
    Product p1 = new Product();
    p1.setId(1L);
    Product p2 = new Product();
    p2.setId(2L);
    Purchase order = new Purchase(10, 20);
    PurchaseProduct op1 = new PurchaseProduct(order, p1, 10);
    order.setUser(user);
    order.setId(1L);
    return order;
  }

  Purchase setupPurchaseVisibleforSuperUser() {
    Product p1 = new Product();
    p1.setId(1L);
    Product p2 = new Product();
    p2.setId(2L);
    Purchase order = new Purchase(10, 20);
    PurchaseProduct op1 = new PurchaseProduct(order, p1, 10);
    order.setUser(superuser);
    order.setId(1L);
    return order;
  }

  List<Purchase> setupPurchasesVisibleforUser() {
    Product p1 = new Product();
    p1.setId(1L);
    Product p2 = new Product();
    p2.setId(2L);
    Purchase order = new Purchase(10, 20);
    PurchaseProduct op1 = new PurchaseProduct(order, p1, 10);
    order.setUser(user);
    PurchaseProduct op2 = new PurchaseProduct(order, p2, 20);
    order.setProducts(Arrays.asList(op1, op2));
    return Collections.singletonList(order);
  }

  List<Purchase> setupPurchasesVisibleforSuperUser() {
    Product p1 = new Product();
    p1.setId(1L);
    Product p2 = new Product();
    p2.setId(2L);
    Purchase order = new Purchase(10, 20);
    Purchase order2 = new Purchase(30, 40);
    PurchaseProduct op1 = new PurchaseProduct(order, p1, 10);

    PurchaseProduct op2 = new PurchaseProduct(order, p2, 20);
    order.setUser(user);
    order2.setUser(superuser);
    order.setProducts(Arrays.asList(op1, op2));
    return Arrays.asList(order, order2);
  }

  void verifyFindAllByUserIdIsCalledOnce() {
    Mockito.verify(purchaseRepository, VerificationModeFactory.times(1))
        .findAllByUser_UserId(Mockito.anyLong(), any());
  }

  void verifyFindByIdIsCalledOnce() {
    Mockito.verify(purchaseRepository, VerificationModeFactory.times(1))
        .findById(Mockito.anyLong());
  }

  void verifyFindAllByPurchaseIdAndUserIdisCalledOnce() {
    Mockito.verify(purchaseRepository, VerificationModeFactory.times(1))
        .findByIdAndUser_UserId(Mockito.anyLong(), Mockito.anyLong());
  }

  void verifyFindAllisCalledOnce() {
    Mockito.verify(purchaseRepository, VerificationModeFactory.times(1))
        .findAll(any(Pageable.class));
  }
}
