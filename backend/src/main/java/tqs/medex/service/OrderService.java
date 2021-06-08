package tqs.medex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.medex.entity.Order;
import tqs.medex.entity.OrderProduct;
import tqs.medex.pojo.CreateOrderPOJO;
import tqs.medex.repository.OrderProductRepository;
import tqs.medex.repository.OrderRepository;
import tqs.medex.repository.ProductRepository;

import java.util.ArrayList;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderProductRepository orderProductRepository;

    @Autowired
    ProductRepository productRepository;


    public Order addNewOrder(CreateOrderPOJO newOrder, long userId) {

        var order = new Order(newOrder.getLat(), newOrder.getLon());
        var orderProducts = new ArrayList<OrderProduct>();
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
            var orderProduct = new OrderProduct(order, product.get(), newOrder.getProducts().get(product.get().getId()));
            orderProducts.add(orderProduct);
        }
        orderRepository.save(order);
        for(OrderProduct op : orderProducts){
            orderProductRepository.save(op);
        }
        order.setProducts(orderProducts);
        return order;
    }
}
