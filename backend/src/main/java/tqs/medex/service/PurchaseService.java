package tqs.medex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.medex.entity.Purchase;
import tqs.medex.entity.PurchaseProduct;
import tqs.medex.pojo.CreatePurchasePOJO;
import tqs.medex.repository.OrderProductRepository;
import tqs.medex.repository.PurchaseRepository;
import tqs.medex.repository.ProductRepository;

import java.util.ArrayList;

@Service
public class PurchaseService {

    @Autowired
    PurchaseRepository orderRepository;

    @Autowired
    OrderProductRepository orderProductRepository;

    @Autowired
    ProductRepository productRepository;


    public Purchase addNewOrder(CreatePurchasePOJO newOrder, long userId) {

        var order = new Purchase(newOrder.getLat(), newOrder.getLon());
        var orderProducts = new ArrayList<PurchaseProduct>();
        // iterates the provided products and checks if they exist and the stock is correct
        for (Long pId : newOrder.getProducts().keySet()) {

            var product = productRepository.findById(pId);
            // checks if the product exists
            if (product.isEmpty()) {
                return null;
            }
            // checks if the product has enough stock
            if (product.get().getStock() < newOrder.getProducts().get(pId)) {
                return null;
            }
            var orderProduct = new PurchaseProduct(order, product.get(), newOrder.getProducts().get(product.get().getId()));
            orderProducts.add(orderProduct);
        }
        orderRepository.save(order);
        for (PurchaseProduct op : orderProducts) {
            orderProductRepository.save(op);
        }
        order.setProducts(orderProducts);
        return order;
    }
}
