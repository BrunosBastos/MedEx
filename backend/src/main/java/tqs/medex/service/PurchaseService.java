package tqs.medex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.medex.entity.Purchase;
import tqs.medex.entity.PurchaseProduct;
import tqs.medex.entity.User;
import tqs.medex.pojo.CreatePurchasePOJO;
import tqs.medex.repository.ProductRepository;
import tqs.medex.repository.PurchaseProductRepository;
import tqs.medex.repository.PurchaseRepository;

import java.util.ArrayList;

@Service
public class PurchaseService {

  @Autowired PurchaseRepository purchaseRepository;

  @Autowired PurchaseProductRepository purchaseProductRepository;

  @Autowired ProductRepository productRepository;

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
    purchaseRepository.save(purchase);
    // creates PurchaseProduct entities and updates products stock
    for (PurchaseProduct op : purchaseProducts) {
      var product = op.getProduct();
      product.setStock(product.getStock() - op.getProductAmount());
      op.setProduct(product);
      productRepository.save(product);
      purchaseProductRepository.save(op);
    }
    purchase.setProducts(purchaseProducts);
    return purchase;
  }
}
