package org.example.ecommerce.service;

import org.example.ecommerce.Model.DTO.OrderItemRequest;
import org.example.ecommerce.Model.DTO.OrderItemResponse;
import org.example.ecommerce.Model.DTO.OrderRequest;
import org.example.ecommerce.Model.DTO.OrderResponse;
import org.example.ecommerce.Model.Order;
import org.example.ecommerce.Model.OrderItem;
import org.example.ecommerce.Model.Product;
import org.example.ecommerce.Repo.OrderRepo;
import org.example.ecommerce.Repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private OrderRepo orderRepo;

    public OrderResponse placeOrder(OrderRequest request) {
        Order order = new Order();
        String orderId = "ORD"+UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        order.setOrderId(orderId);
        order.setCustomerName(request.customerName());
        order.setEmail(request.email());
        order.setStatus("PLACED!");
        order.setOrderDate(LocalDate.now());

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest itemReq : request.item()) {

            Product product = productRepo.findById(itemReq.productId())
                    .orElseThrow(() -> new RuntimeException("Product Not Found"));

            product.setStockQuantity(product.getStockQuantity() - itemReq.quantity());
            productRepo.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemReq.quantity())
                    .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(itemReq.quantity())))
                    .order(order)
                    .build();
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);
        Order saveOrder=orderRepo.save(order);

        List<OrderItemResponse >itemResponses = new ArrayList<>();
        for(OrderItem item:order.getOrderItems()){
            OrderItemResponse orderItemResponse=new OrderItemResponse(
                    item.getProduct().getName()
                    ,item.getQuantity()
                    ,item.getTotalPrice());
            itemResponses.add(orderItemResponse);
        }
        OrderResponse orderResponse =new OrderResponse(
                saveOrder.getOrderId(),
                saveOrder.getCustomerName(),
                saveOrder.getEmail(),
                saveOrder.getStatus(),
                saveOrder.getOrderDate(),
                itemResponses);
        return orderResponse;
    }
//    public OrderResponse placeOrder(OrderRequest request) {
//
//        // ✅ Validate request
//        if (request.item() == null || request.item().isEmpty()) {
//            throw new RuntimeException("Order must contain at least one item");
//        }
//
//        Order order = new Order();
//
//        // ✅ Generate Order ID
//        String orderId = "ORD" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
//
//        order.setOrderId(orderId);
//        order.setCustomerName(request.customerName());
//        order.setEmail(request.email());
//        order.setStatus("PLACED");
//        order.setOrderDate(LocalDate.now());
//
//        List<OrderItem> orderItems = new ArrayList<>();
//
//        for (OrderItemRequest itemReq : request.item()) {
//
//            // ✅ Fetch product
//            Product product = productRepo.findById(itemReq.productId())
//                    .orElseThrow(() -> new RuntimeException("Product Not Found"));
//
//            // ✅ Check stock
//            if (product.getStockQuantity() < itemReq.quantity()) {
//                throw new RuntimeException("Insufficient stock for product: " + product.getName());
//            }
//
//            // ✅ Update stock
//            product.setStockQuantity(product.getStockQuantity() - itemReq.quantity());
//            productRepo.save(product);
//
//            // ✅ Create OrderItem
//            OrderItem orderItem = OrderItem.builder()
//                    .product(product)
//                    .quantity(itemReq.quantity())
//                    .totalPrice(product.getPrice()
//                            .multiply(BigDecimal.valueOf(itemReq.quantity())))
//                    .order(order)
//                    .build();
//
//            // 🔥 IMPORTANT FIX (was missing)
//            orderItems.add(orderItem);
//        }
//
//        // ✅ Set items to order
//        order.setOrderItems(orderItems);
//
//        // ✅ Save order (cascade saves items)
//        Order savedOrder = orderRepo.save(order);
//
//        // ✅ Prepare response
//        List<OrderItemResponse> itemResponses = new ArrayList<>();
//
//        for (OrderItem item : savedOrder.getOrderItems()) {
//            OrderItemResponse responseItem = new OrderItemResponse(
//                    item.getProduct().getName(),
//                    item.getQuantity(),
//                    item.getTotalPrice()
//            );
//            itemResponses.add(responseItem);
//        }
//
//        // ✅ Final response
//        return new OrderResponse(
//                savedOrder.getOrderId(),
//                savedOrder.getCustomerName(),
//                savedOrder.getEmail(),
//                savedOrder.getStatus(),
//                savedOrder.getOrderDate(),
//                itemResponses
//        );
//    }
    public List<OrderResponse> getAllOrderResponse() {
        List<Order> orders=orderRepo.findAll();
        List<OrderResponse> orderResponses= new ArrayList<>();

        for(Order order:orders){

            List<OrderItemResponse> itemResponse=new ArrayList<>();
            for (OrderItem item :order.getOrderItems()){
                OrderItemResponse orderItemResponse=new OrderItemResponse(
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getTotalPrice()
                );
                itemResponse.add(orderItemResponse);
            }

            OrderResponse orderResponse=new OrderResponse(
                    order.getOrderId(),
                    order.getCustomerName(),
                    order.getEmail(),
                    order.getStatus(),
                    order.getOrderDate(),
                    itemResponse
            );
            orderResponses.add(orderResponse);
        }
        return orderResponses;
    }
}
