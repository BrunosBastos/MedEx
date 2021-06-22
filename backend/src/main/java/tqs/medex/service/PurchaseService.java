package tqs.medex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tqs.medex.entity.Purchase;
import tqs.medex.entity.PurchaseProduct;
import tqs.medex.entity.User;
import tqs.medex.pojo.CreatePurchasePOJO;
import tqs.medex.repository.ProductRepository;
import tqs.medex.repository.PurchaseProductRepository;
import tqs.medex.repository.PurchaseRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseService {

  private static final int PAGE_SIZE = 10;
  @Autowired PurchaseRepository purchaseRepository;

  @Autowired PurchaseProductRepository purchaseProductRepository;

  @Autowired ProductRepository productRepository;

  @Autowired ExternalService externalService;

  public List<Purchase> getPurchases(User user, Integer page, Boolean recent) {
    Pageable pageable =
        PageRequest.of(
            page, PAGE_SIZE, recent ? Sort.by("id").descending() : Sort.by("id").ascending());
    if (!user.isSuperUser()) {
      return purchaseRepository.findAllByUser_UserId(user.getUserId(), pageable).getContent();
    }
    return purchaseRepository.findAll(pageable).getContent();
  }

  public Purchase getPurchaseDetails(User user, long purchaseid) {
    if (!user.isSuperUser()) {
      return purchaseRepository.findByIdAndUser_UserId(purchaseid, user.getUserId()).orElse(null);
    }
    return purchaseRepository.findById(purchaseid).orElse(null);
  }

  public Purchase addNewPurchase(CreatePurchasePOJO newPurchase, User user) {

    var purchase = new Purchase(newPurchase.getLat(), newPurchase.getLon());
    purchase.setUser(user);
    var purchaseProducts = new ArrayList<PurchaseProduct>();
    // iterates the provided products and checks if they exist and the stock is correct
    for (Long pId : newPurchase.getProducts().keySet()) {

      var product = productRepository.findById(pId);
      // checks if the product exists
      if (product.isEmpty()) {
        return null;
      }
      // checks if the product has enough stock
      if (product.get().getStock() < newPurchase.getProducts().get(pId)) {
        return null;
      }
      var purchaseProduct =
          new PurchaseProduct(
              purchase, product.get(), newPurchase.getProducts().get(product.get().getId()));
      purchaseProducts.add(purchaseProduct);
    }
    purchase = purchaseRepository.save(purchase);
    // creates PurchaseProduct entities and updates products stock
    for (PurchaseProduct op : purchaseProducts) {
      var product = op.getProduct();
      product.setStock(product.getStock() - op.getProductAmount());
      op.setProduct(product);
      productRepository.save(product);
      purchaseProductRepository.save(op);
    }
    purchase.setProducts(purchaseProducts);

    externalService.createDelivery(purchase);
    return purchase;
  }

  public List<PurchaseProduct> getPurchasedProductsBySupplier(
      Long supplierId, int page, boolean recent) {
    Pageable pageable =
        PageRequest.of(
            page,
            PAGE_SIZE,
            recent ? Sort.by("purchase.id").descending() : Sort.by("purchase.id").ascending());
    return purchaseProductRepository.findAllByProductSupplierId(supplierId, pageable).getContent();
  }

  public Purchase updatePurchase(Long id) {

    var purchasedb = purchaseRepository.findById(id);
    if (purchasedb.isEmpty()) {
      return null;
    }

    var purchase = purchasedb.get();
    purchase.setDelivered(true);
    return purchaseRepository.save(purchase);
  }
}
